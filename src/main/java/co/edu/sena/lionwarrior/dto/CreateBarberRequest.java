package co.edu.sena.lionwarrior.dto;


import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/** Datos para crear el perfil de barbero de un usuario existente. */
public record CreateBarberRequest(

        @NotNull(message = "El id del usuario es obligatorio")
        Integer idUser,

        @Size(max = 100, message = "Especialidad maxima de 100 caracteres")
        String specialty,

        @Size(max = 100, message = "Horario maximo a 100 caracteres")
        String availableSchedule
) {
}
