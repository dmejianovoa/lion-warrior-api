package co.edu.sena.lionwarrior.dto;

import co.edu.sena.lionwarrior.model.Appointment;
import co.edu.sena.lionwarrior.model.AppointmentStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

/** Cita con el detalle de sus servicios y el total. */
public record AppointmentResponse(
        Integer idAppointment,
        Integer idUser,
        String userName,
        Integer idBarber,
        String barberName,
        LocalDate appointmentDate,
        LocalTime appointmentTime,
        AppointmentStatus status,
        LocalDateTime createdAt,
        List<ServiceLine> services,
        BigDecimal total
) {

    /** Una línea de servicio dentro de la cita. */
    public record ServiceLine(Integer idService, String serviceName, BigDecimal subtotal) {
    }

    public static AppointmentResponse from(Appointment a) {
        List<ServiceLine> lines = a.getItems().stream()
                .map(i -> new ServiceLine(i.getService().getIdService(),
                        i.getService().getServiceName(), i.getSubtotal()))
                .toList();

        // El total se calcula sumando los subtotales
        BigDecimal total = lines.stream()
                .map(ServiceLine::subtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return  new AppointmentResponse(a.getIdAppointment(),
                a.getUser().getIdUser(), a.getUser().getUserName(),
                a.getBarber().getIdBarber(), a.getBarber().getUser().getUserName(),
                a.getAppointmentDate(), a.getAppointmentTime(), a.getAppointmentStatus(),
                a.getCreatedAt(), lines, total);
    }
}
