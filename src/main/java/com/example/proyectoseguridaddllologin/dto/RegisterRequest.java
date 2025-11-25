package com.example.proyectoseguridaddllologin.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {

  @NotBlank(message = "El username no puede estar vacío")
  @Size(min = 3, max = 50, message = "El username debe tener entre 3 y 50 caracteres")
  @Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "El username solo puede contener letras, números y guion bajo")
  private String username;

  @NotBlank(message = "El password no puede estar vacío")
  @Size(min = 8, max = 100, message = "El password debe tener entre 8 y 100 caracteres")
  @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=]).*$", message = "El password debe contener al menos: 1 mayúscula, 1 minúscula, 1 número y 1 carácter especial")
  private String password;

  @NotBlank(message = "El nombre completo no puede estar vacío")
  @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
  @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+$", message = "El nombre solo puede contener letras y espacios")
  private String fullName;

  @NotBlank(message = "La raza no puede estar vacía")
  @Size(min = 2, max = 50, message = "La raza debe tener entre 2 y 50 caracteres")
  private String race;

  @NotNull(message = "El nivel de poder es requerido")
  @Min(value = 1, message = "El poder debe ser al menos 1")
  @Max(value = 999999999999L, message = "El poder es demasiado alto")
  private Long powerLevel;
}
