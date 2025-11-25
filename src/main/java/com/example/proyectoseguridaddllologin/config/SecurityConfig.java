package com.example.proyectoseguridaddllologin.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // ✅ CSRF DESHABILITADO: Justificación para API REST con JWT
                // - Esta es una API REST stateless (sin sesiones ni cookies de sesión)
                // - Usamos JWT tokens en headers Authorization (no cookies automáticas)
                // - JWT debe ser incluido manualmente por el cliente en cada request
                // - Los tokens JWT no son vulnerables a CSRF porque:
                // 1. Se almacenan en localStorage/sessionStorage (no en cookies)
                // 2. No son enviados automáticamente por el navegador
                // 3. JavaScript de otro dominio no puede acceder al token
                .csrf(csrf -> csrf.disable())

                .authorizeHttpRequests(auth -> auth
                        // ✅ Rutas públicas (sin autenticación)
                        .requestMatchers("/api/auth/login").permitAll() // Login público
                        .requestMatchers("/h2-console/**").permitAll() // H2 Console
                        .requestMatchers("/*.html").permitAll() // Páginas HTML demo
                        .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll() // Swagger

                        // ✅ Rutas protegidas por ROL ADMIN
                        .requestMatchers(HttpMethod.DELETE, "/api/users/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/api/users/*/power").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/users/*/power").hasRole("ADMIN")

                        // ✅ Rutas protegidas (requieren autenticación con JWT)
                        .requestMatchers(HttpMethod.GET, "/api/users").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/users/*").authenticated()

                        // ✅ Cualquier otra ruta requiere autenticación
                        .anyRequest().authenticated())
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // Sin sesiones
                )
                .httpBasic(basic -> {
                }); // Basic Auth solo para login inicial

        // ✅ Agregar filtro JWT ANTES del filtro de autenticación estándar
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        // Permitir frames para H2 Console
        http.headers(headers -> headers.frameOptions(frame -> frame.sameOrigin()));

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
