package co.edu.sena.lionwarrior.repository;

import co.edu.sena.lionwarrior.model.Appointment;
import co.edu.sena.lionwarrior.model.AppointmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

/** Acceso a la tabla appointments. */
public interface AppointmentRepository extends JpaRepository<Appointment, Integer> {

    // Cita del cliente
    List<Appointment> findByUserIdUser(Integer idUser);

    // Cita del barbero
    List<Appointment> findByBarberIdBarber(Integer idBarber);

    // ¿El barbero ya tiene una cita a esa fecha?
    // ¿Que NO esté en el estado dado (cancelled)?
    boolean existsByBarberIdBarberAndAppointmentDateAndAppointmentTimeAndAppointmentStatusNot (
            Integer idBarber, LocalDate date, LocalTime time, AppointmentStatus status
    );
}
