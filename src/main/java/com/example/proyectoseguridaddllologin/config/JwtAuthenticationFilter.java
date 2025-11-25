package com.example.proyectoseguridaddllologin.config;

import com.example.proyectoseguridaddllologin.service.CustomUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Filtro JWT que se ejecuta en cada petición HTTP.
 * 
 * Funcionalidad:
 * 1. Extrae el token JWT del header "Authorization"
 * 2. Valida el token
 * 3. Si es válido, autentica al usuario en el contexto de seguridad
 * 4. Si no, pasa la petición sin autenticar
 */
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private final JwtUtil jwtUtil;
  private final CustomUserDetailsService userDetailsService;

  @Override
  protected void doFilterInternal(
      @NonNull HttpServletRequest request,
      @NonNull HttpServletResponse response,
      @NonNull FilterChain filterChain) throws ServletException, IOException {

    // Obtener el header Authorization
    final String authHeader = request.getHeader("Authorization");
    final String jwt;
    final String username;

    // Verificar si el header contiene un Bearer token
    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
      // No hay token JWT, continuar con la cadena de filtros
      filterChain.doFilter(request, response);
      return;
    }

    // Extraer el token (remover "Bearer " del inicio)
    jwt = authHeader.substring(7);

    try {
      // Extraer username del token
      username = jwtUtil.extractUsername(jwt);

      // Si tenemos username y el usuario no está ya autenticado
      if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

        // Cargar los detalles del usuario desde la base de datos
        UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

        // Validar el token
        if (jwtUtil.validateToken(jwt, userDetails)) {
          // Token válido - Crear objeto de autenticación
          UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
              userDetails,
              null,
              userDetails.getAuthorities());

          authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

          // Establecer la autenticación en el contexto de seguridad
          SecurityContextHolder.getContext().setAuthentication(authToken);
        }
      }
    } catch (Exception e) {
      // Token inválido o expirado - log y continuar sin autenticar
      logger.error("Error al procesar token JWT: " + e.getMessage());
    }

    // Continuar con la cadena de filtros
    filterChain.doFilter(request, response);
  }
}
