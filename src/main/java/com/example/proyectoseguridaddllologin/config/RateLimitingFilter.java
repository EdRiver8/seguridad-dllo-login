package com.example.proyectoseguridaddllologin.config;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

/**
 * Filtro de Rate Limiting que limita el número de peticiones por IP.
 * 
 * Funcionalidad:
 * - Usa Bucket4j (algoritmo Token Bucket) para rate limiting
 * - Almacena buckets por IP en cache (Caffeine)
 * - Configurable por endpoint
 * - Retorna 429 Too Many Requests cuando se excede el límite
 */
@Slf4j
@Component
public class RateLimitingFilter extends OncePerRequestFilter {

  @Value("${rate.limit.login.capacity:5}")
  private int loginCapacity; // Máximo de requests permitidos

  @Value("${rate.limit.login.refill.tokens:5}")
  private int loginRefillTokens; // Tokens que se reponen

  @Value("${rate.limit.login.refill.duration:15}")
  private int loginRefillDuration; // Duración en minutos

  // Cache para almacenar buckets por IP (expira después de 1 hora de inactividad)
  private final Cache<String, Bucket> cache = Caffeine.newBuilder()
      .expireAfterAccess(1, TimeUnit.HOURS)
      .maximumSize(10000)
      .build();

  @Override
  protected void doFilterInternal(
      @NonNull HttpServletRequest request,
      @NonNull HttpServletResponse response,
      @NonNull FilterChain filterChain) throws ServletException, IOException {

    String requestURI = request.getRequestURI();

    // ✅ Solo aplicar rate limiting al endpoint de login
    if (requestURI.equals("/api/auth/login") && request.getMethod().equals("POST")) {

      String clientIp = getClientIP(request);
      String key = "login:" + clientIp;

      // Obtener o crear bucket para esta IP
      Bucket bucket = cache.get(key, k -> createBucket());

      if (bucket != null && bucket.tryConsume(1)) {
        // ✅ Permitir request - tiene tokens disponibles
        long remainingTokens = bucket.getAvailableTokens();
        log.debug("Rate limit OK para IP: {} - Tokens restantes: {}", clientIp, remainingTokens);

        // Agregar headers informativos
        response.addHeader("X-Rate-Limit-Remaining", String.valueOf(remainingTokens));
        response.addHeader("X-Rate-Limit-Limit", String.valueOf(loginCapacity));

        filterChain.doFilter(request, response);
      } else {
        // ❌ Bloquear request - límite excedido
        log.warn("🔴 RATE LIMIT EXCEDIDO para IP: {} en endpoint: {}", clientIp, requestURI);

        response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String jsonResponse = String.format(
            "{\"success\": false, \"message\": \"Demasiados intentos de login. " +
                "Por favor, espera %d minutos antes de intentar nuevamente.\", " +
                "\"error\": \"RATE_LIMIT_EXCEEDED\"}",
            loginRefillDuration);

        response.getWriter().write(jsonResponse);
      }
    } else {
      // No aplicar rate limiting a otros endpoints
      filterChain.doFilter(request, response);
    }
  }

  /**
   * Crea un nuevo bucket con la configuración de rate limiting
   */
  private Bucket createBucket() {
    // Configuración: 5 requests cada 15 minutos
    Bandwidth limit = Bandwidth.classic(
        loginCapacity,
        Refill.intervally(loginRefillTokens, Duration.ofMinutes(loginRefillDuration)));
    return Bucket.builder()
        .addLimit(limit)
        .build();
  }

  /**
   * Obtiene la IP real del cliente, considerando proxies y balanceadores
   */
  private String getClientIP(HttpServletRequest request) {
    String xfHeader = request.getHeader("X-Forwarded-For");
    if (xfHeader == null || xfHeader.isEmpty() || "unknown".equalsIgnoreCase(xfHeader)) {
      return request.getRemoteAddr();
    }
    // X-Forwarded-For puede contener múltiples IPs, tomar la primera
    return xfHeader.split(",")[0].trim();
  }
}
