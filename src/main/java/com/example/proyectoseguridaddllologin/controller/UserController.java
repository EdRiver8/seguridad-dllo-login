package com.example.proyectoseguridaddllologin.controller;
import com.example.proyectoseguridaddllologin.model.User;
import com.example.proyectoseguridaddllologin.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;

    /**
     * 🔴 ENDPOINT VULNERABLE: Devuelve TODOS los usuarios sin autenticación
     * Este endpoint debería estar protegido y solo accesible por administradores
     */
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userRepository.findAll());
    }

    /**
     * 🔴 ENDPOINT VULNERABLE: Cualquiera puede obtener información de cualquier usuario
     */
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        return userRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * 🔴 ENDPOINT VULNERABLE: Cualquiera puede eliminar usuarios
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }

    /**
     * 🔴 ENDPOINT VULNERABLE: Cualquiera puede modificar el nivel de poder
     */
    @PatchMapping("/{id}/power")
    public ResponseEntity<User> updatePowerLevel(@PathVariable Long id, @RequestParam Long newPower) {
        return userRepository.findById(id)
                .map(user -> {
                    user.setPowerLevel(newPower);
                    return ResponseEntity.ok(userRepository.save(user));
                })
                .orElse(ResponseEntity.notFound().build());
    }
}