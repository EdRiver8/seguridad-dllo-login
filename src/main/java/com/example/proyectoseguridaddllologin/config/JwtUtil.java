package com.example.proyectoseguridaddllologin.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Utilidad para generar y validar tokens JWT.
 * 
 * Funcionalidades:
 * - Generar token JWT con información del usuario
 * - Validar tokens JWT
 * - Extraer información del token (username, expiración, etc.)
 */
@Component
public class JwtUtil {

  @Value("${jwt.secret}")
  private String secret;

  @Value("${jwt.expiration}")
  private Long expiration;

  /**
   * Genera la clave secreta para firmar tokens JWT
   */
  private SecretKey getSigningKey() {
    byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
    return Keys.hmacShaKeyFor(keyBytes);
  }

  /**
   * Extrae el username del token JWT
   */
  public String extractUsername(String token) {
    return extractClaim(token, Claims::getSubject);
  }

  /**
   * Extrae la fecha de expiración del token
   */
  public Date extractExpiration(String token) {
    return extractClaim(token, Claims::getExpiration);
  }

  /**
   * Extrae un claim específico del token
   */
  public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
    final Claims claims = extractAllClaims(token);
    return claimsResolver.apply(claims);
  }

  /**
   * Extrae todos los claims del token
   */
  private Claims extractAllClaims(String token) {
    return Jwts.parser()
        .verifyWith(getSigningKey())
        .build()
        .parseSignedClaims(token)
        .getPayload();
  }

  /**
   * Verifica si el token ha expirado
   */
  private Boolean isTokenExpired(String token) {
    return extractExpiration(token).before(new Date());
  }

  /**
   * Genera un token JWT para el usuario
   * 
   * @param username El nombre de usuario
   * @return Token JWT generado
   */
  public String generateToken(String username) {
    Map<String, Object> claims = new HashMap<>();
    return createToken(claims, username);
  }

  /**
   * Genera un token JWT con claims adicionales
   * 
   * @param extraClaims Claims adicionales (rol, email, etc.)
   * @param username    El nombre de usuario
   * @return Token JWT generado
   */
  public String generateToken(Map<String, Object> extraClaims, String username) {
    return createToken(extraClaims, username);
  }

  /**
   * Crea el token JWT con los claims y el username
   */
  private String createToken(Map<String, Object> claims, String subject) {
    Date now = new Date();
    Date expirationDate = new Date(now.getTime() + expiration);

    return Jwts.builder()
        .claims(claims)
        .subject(subject)
        .issuedAt(now)
        .expiration(expirationDate)
        .signWith(getSigningKey(), Jwts.SIG.HS256)
        .compact();
  }

  /**
   * Valida el token JWT
   * 
   * @param token       Token a validar
   * @param userDetails Detalles del usuario
   * @return true si el token es válido
   */
  public Boolean validateToken(String token, UserDetails userDetails) {
    final String username = extractUsername(token);
    return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
  }

  /**
   * Valida que el token no esté expirado y tenga el formato correcto
   */
  public Boolean validateToken(String token) {
    try {
      return !isTokenExpired(token);
    } catch (Exception e) {
      return false;
    }
  }
}
