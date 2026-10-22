package co.edu.sena.lionwarrior.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/** Credenciales para inicio de sesion */
public record LoginRequest(
        @NotBlank(message = "El email es obligatorio")
        @Email(message = "El email no tiene un formato valido")
        String email,

        @NotBlank(message = "La contraseña es obligatoria")
        String password
) {
}
