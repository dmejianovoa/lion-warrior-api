package co.edu.sena.lionwarrior.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

/** Datos para agendar una cita. Fecha: "2026-11-05". Hora: "10:30". */
public record CreateAppointmentRequest(
        @NotNull(message = "El id del usuario es obligatorio")
        Integer idUser,

        @NotNull(message = "El id del barbero es obligatorio")
        Integer idBarber,

        @NotNull(message = "La fecha es obligatoria")
        LocalDate appointmentDate,

        @NotNull(message = "La hora es obligatoria")
        LocalTime appointmentTime,
        @NotEmpty(message = "Debe elegir al menos un servicio")
        List<Integer> serviceIds
) {
}
