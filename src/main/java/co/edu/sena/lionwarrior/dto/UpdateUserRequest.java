package co.edu.sena.lionwarrior.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/** Datos editables de un usuario. Los campos null se ignoran. */
public record UpdateUserRequest(
        @Size(min = 1, max = 50, message = "El nombre debe tener entre 1 y 50 caracteres")
        String userName,

        @Size(min = 1, max = 50, message = "El nombre debe tener entre 1 y 50 caracteres")
        String lastName,

        @Email(message = "El email no tiene un formato válido")
        @Size(max = 100, message = "El email no puede superar 100 caracteres")
        String email,

        @Pattern(regexp = "^\\d{3}-\\d{3}-\\d{4}$", message = "El formato debe ser 300-000-0000")
        String phoneNumber
) {
}
