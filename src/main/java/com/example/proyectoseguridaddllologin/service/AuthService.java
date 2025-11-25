package com.example.proyectoseguridaddllologin.service;

import com.example.proyectoseguridaddllologin.config.JwtUtil;
import com.example.proyectoseguridaddllologin.dto.LoginRequest;
import com.example.proyectoseguridaddllologin.dto.LoginResponse;
import com.example.proyectoseguridaddllologin.model.User;
import com.example.proyectoseguridaddllologin.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public LoginResponse login(LoginRequest request) {
        log.info("Login attempt for user: {}", request.getUsername());

        // Buscar usuario por username
        User user = userRepository.findByUsername(request.getUsername())
                .orElse(null);

        if (user == null) {
            log.warn("User not found: {}", request.getUsername());
            return LoginResponse.failure("Invalid username or password");
        }

        // Verificar si el usuario está habilitado
        if (!user.getEnabled()) {
            log.warn("User account disabled: {}", request.getUsername());
            return LoginResponse.failure("User account is disabled");
        }

        // Validar contraseña
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            log.warn("Invalid password for user: {}", request.getUsername());
            return LoginResponse.failure("Invalid username or password");
        }

        log.info("Login successful for user: {}", request.getUsername());

        // Generar JWT token con claims adicionales
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", user.getRole());
        claims.put("userId", user.getId());

        String jwtToken = jwtUtil.generateToken(claims, user.getUsername());

        // Crear respuesta exitosa con token
        LoginResponse.UserInfo userInfo = new LoginResponse.UserInfo(
                user.getId(),
                user.getUsername(),
                user.getFullName(),
                user.getRace(),
                user.getPowerLevel(),
                user.getRole());

        return LoginResponse.success(jwtToken, userInfo);
    }
}
