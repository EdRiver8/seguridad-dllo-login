package com.example.proyectoseguridaddllologin.service;

import com.example.proyectoseguridaddllologin.dto.LoginRequest;
import com.example.proyectoseguridaddllologin.dto.LoginResponse;
import com.example.proyectoseguridaddllologin.model.User;
import com.example.proyectoseguridaddllologin.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

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

        // Crear respuesta exitosa
        LoginResponse.UserInfo userInfo = new LoginResponse.UserInfo(
                user.getId(),
                user.getUsername(),
                user.getFullName(),
                user.getRace(),
                user.getPowerLevel(),
                user.getRole()
        );

        return LoginResponse.success(userInfo);
    }
}
