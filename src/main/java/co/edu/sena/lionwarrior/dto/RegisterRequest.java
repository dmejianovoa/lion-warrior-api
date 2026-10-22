package co.edu.sena.lionwarrior.dto;

import jakarta.validation.constraints.*;

/** Datos para registrarse. El rol no viene aquí: siempre se crea como client.*/

public record RegisterRequest(
        @NotBlank(message = "El nombre es obligatorio")
        @Size(max = 50, message = "El nombre no puede superar los 50 caracteres")
        String userName,

        @NotBlank(message = "El apellido es obligatorio")
        @Size(max = 50, message = "El apellido no puede superar los 50 caracteres")
        String lastName,

        @NotBlank(message = "El email es obligatorio")
        @Email(message = "El email no tiene un formato válido")
        @Size(max = 100, message = "El email no puede superar los 100 caracteres")
        String email,

        @NotBlank(message = "El telefono es obligatorio")
        @Pattern(regexp = "^\\d{3}-\\d{3}-\\d{4}$", message = "Formato 300-000-0000")
        String phoneNumber,

        @NotBlank(message = "La contraseña es incorrecta")
        @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d).{8,72}$",
                            message = "La contraseña debe tener minimo 8 caracteres")
        String password
) {
}
