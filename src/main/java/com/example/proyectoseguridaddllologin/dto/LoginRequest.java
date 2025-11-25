package com.example.proyectoseguridaddllologin.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {

    @NotBlank(message = "El username no puede estar vacío")
    @Size(min = 3, max = 50, message = "El username debe tener entre 3 y 50 caracteres")
    @Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "El username solo puede contener letras, números y guion bajo")
    private String username;

    @NotBlank(message = "El password no puede estar vacío")
    @Size(min = 6, max = 100, message = "El password debe tener entre 6 y 100 caracteres")
    private String password;
}
