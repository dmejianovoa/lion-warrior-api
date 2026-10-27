package co.edu.sena.lionwarrior.dto;

import jakarta.validation.constraints.Size;

/** Datos editables de un barbero. Para retirarlo se manda active en false. */
public record UpdateBarberRequest(

        @Size(max = 100, message = "La especialidad no debe superar los 100 caracteres")
        String specialty,

        @Size(max = 100, message = "El horario no debe superar los 100 caracteres")
        String availableSchedule,

        Boolean active
) {
}
