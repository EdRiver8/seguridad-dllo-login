package com.example.proyectoseguridaddllologin.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    @NotBlank(message = "El username es requerido")
    @Size(min = 3, max = 50, message = "El username debe tener entre 3 y 50 caracteres")
    @Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "El username solo puede contener letras, números y guion bajo")
    private String username;

    @Column(nullable = false, length = 255)
    @JsonIgnore // no se expone la contraseña en las respuestas JSON
    @NotBlank(message = "El password es requerido")
    private String password;

    @Column(name = "full_name", nullable = false, length = 100)
    @NotBlank(message = "El nombre completo es requerido")
    @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
    private String fullName;

    @Column(length = 50)
    @Size(max = 50, message = "La raza no puede exceder 50 caracteres")
    private String race;

    @Column(name = "power_level")
    @Min(value = 0, message = "El nivel de poder no puede ser negativo")
    private Long powerLevel;

    @Column(nullable = false, length = 20)
    @NotBlank(message = "El rol es requerido")
    @Pattern(regexp = "^(USER|ADMIN)$", message = "El rol debe ser USER o ADMIN")
    private String role;

    @Column(nullable = false)
    @NotNull(message = "El campo enabled es requerido")
    private Boolean enabled = true;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }
}
