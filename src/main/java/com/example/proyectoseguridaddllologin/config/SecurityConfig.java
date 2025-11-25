package com.example.proyectoseguridaddllologin.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**", "/h2-console/**", "/v3/api-docs/**",
                                "/swagger-ui/**", "/swagger-ui.html").permitAll()
                        .anyRequest().authenticated())
                .headers(headers -> headers.frameOptions(frame -> frame.disable()));

        return http.build();
    }

    // @Bean
    // public SecurityFilterChain securityFilterChain(HttpSecurity http) throws
    // Exception {
    // http
    // .csrf(csrf -> csrf.disable())
    // .authorizeHttpRequests(auth -> auth
    // // ✅ Rutas públicas - Solo login
    // .requestMatchers("/api/auth/login").permitAll()
    // .requestMatchers("/h2-console/**").permitAll()

    // // ✅ PROTECCIÓN POR ROL - Solo administradores pueden eliminar
    // .requestMatchers(HttpMethod.DELETE, "/api/users/**").hasRole("ADMIN")

    // // ✅ PROTECCIÓN POR ROL - Solo administradores pueden modificar poder
    // .requestMatchers(HttpMethod.PATCH, "/api/users/*/power").hasRole("ADMIN")
    // .requestMatchers(HttpMethod.PUT, "/api/users/*/power").hasRole("ADMIN")

    // // ✅ Usuarios autenticados pueden ver la lista
    // .requestMatchers(HttpMethod.GET, "/api/users").authenticated()
    // .requestMatchers(HttpMethod.GET, "/api/users/*").authenticated()

    // // ✅ Cualquier otra ruta requiere autenticación
    // .anyRequest().authenticated())
    // .sessionManagement(session -> session
    // .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
    // .httpBasic(basic -> {
    // }); // Habilitamos HTTP Basic Auth temporalmente

    // // Permitir frames para H2 Console
    // http.headers(headers -> headers.frameOptions(frame -> frame.sameOrigin()));

    // return http.build();
    // }

    @Bean
    public PasswordEncoder passwordEncoder() {
        String encodingId = "bcrypt";
        Map<String, PasswordEncoder> encoders = new HashMap<>();
        encoders.put(encodingId, new BCryptPasswordEncoder());

        return new DelegatingPasswordEncoder(encodingId, encoders);
    }

    // @Bean
    // public PasswordEncoder passwordEncoder() {
    // return new BCryptPasswordEncoder();
    // }
}
