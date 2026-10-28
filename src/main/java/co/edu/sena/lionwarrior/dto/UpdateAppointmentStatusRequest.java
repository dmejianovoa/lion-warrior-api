package co.edu.sena.lionwarrior.dto;

import co.edu.sena.lionwarrior.model.AppointmentStatus;
import jakarta.validation.constraints.NotNull;

/** Nuevo estado de una cita */
public record UpdateAppointmentStatusRequest(
        @NotNull(message = "El estado es obligatorio")
        AppointmentStatus status
) {
}
