package co.edu.sena.lionwarrior.service;

import co.edu.sena.lionwarrior.dto.AppointmentResponse;
import co.edu.sena.lionwarrior.dto.CreateAppointmentRequest;
import co.edu.sena.lionwarrior.model.*;
import co.edu.sena.lionwarrior.repository.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

/** Lógica de negocio de las citas. Cada método es una transacción. */
@Service
@Transactional
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final UserRepository userRepository;
    private final BarberRepository barberRepository;
    private final ServiceItemRepository serviceItemRepository;

    public AppointmentService(AppointmentRepository appointmentRepository,
                              UserRepository userRepository,
                              BarberRepository barberRepository,
                              ServiceItemRepository serviceItemRepository) {
        this.appointmentRepository = appointmentRepository;
        this.userRepository = userRepository;
        this.barberRepository = barberRepository;
        this.serviceItemRepository = serviceItemRepository;
    }

    /** Lista citas. Se puede filtrar por cliente o por barbero. */
    @Transactional(readOnly = true)
    public List<AppointmentResponse> findAll(Integer idUser, Integer idBarber) {
        List<Appointment> appointments;
        if (idUser != null) {
            appointments = appointmentRepository.findByUserIdUser(idUser);
        } else if (idBarber != null) {
            appointments= appointmentRepository.findByBarberIdBarber(idBarber);
        } else {
            appointments = appointmentRepository.findAll();
        }
        return appointments.stream().map(AppointmentResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public AppointmentResponse findById(Integer id) {
        return AppointmentResponse.from(getOrThrow(id));
    }

    /** Agenda una cita con sus servicios. */
    public AppointmentResponse create(CreateAppointmentRequest request) {
        User user = userRepository.findById(request.idUser())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Usuario no encontrado con el id " + request.idUser()
                ));

        Barber barber = barberRepository.findById(request.idBarber())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Barbero no encontrado con el id " + request.idBarber()
                ));

        if (!barber.getActive()) {
            throw  new ResponseStatusException(HttpStatus.CONFLICT, "El barbero no esta activo");
        }

        // La cita no puede quedar en el pasado
        if (LocalDateTime.of(request.appointmentDate(), request.appointmentTime())
                .isBefore(LocalDateTime.now())) {
            throw  new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Bad Request: La cita no puede tener fecha vencida");
        }

        // El barbero no puede tener dos citas a la misma hora
        if (appointmentRepository
                .existsByBarberIdBarberAndAppointmentDateAndAppointmentTimeAndAppointmentStatusNot(
                        barber.getIdBarber(), request.appointmentDate(),
                        request.appointmentTime(), AppointmentStatus.cancelled
                )) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "El barbero ya tiene una cita con esa fecha y hora");
        }


        // Quita ids repetidos y verifica que todos los servicios existan
        List<Integer> serviceIds = request.serviceIds().stream().distinct().toList();
        List<ServiceItem> services = serviceItemRepository.findAllById(serviceIds);
        if (services.size() != serviceIds.size()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Uno o mas servicios no existen");
        }

        Appointment appointment = new Appointment();
        appointment.setUser(user);
        appointment.setBarber(barber);
        appointment.setAppointmentDate(request.appointmentDate());
        appointment.setAppointmentTime(request.appointmentTime());
        appointment.setAppointmentStatus(AppointmentStatus.pending);
        appointment.setCreatedAt(LocalDateTime.now());

        // Cada servicio guarda el precio vigente como subtotal
        for (ServiceItem service : services) {
            AppointmentItem item = new AppointmentItem();
            item.setService(service);
            item.setSubtotal(service.getServicePrice());
            appointment.addItem(item);
        }

        return AppointmentResponse.from(appointmentRepository.save(appointment));
    }

    /** Cambia el estado de la cita respetando las transiciones permitidas. */
    public AppointmentResponse updateStatus(Integer id, AppointmentStatus newStatus) {
        Appointment appointment = getOrThrow(id);
        AppointmentStatus current = appointment.getAppointmentStatus();

        if (!isValidTransition(current, newStatus)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "No se puede pasar una cita de " + current + " a " + newStatus);
        }

        appointment.setAppointmentStatus(newStatus);
        return AppointmentResponse.from(appointmentRepository.save(appointment));
    }

    // Transiciones permitidas entre estados
    private boolean isValidTransition(AppointmentStatus from, AppointmentStatus to) {
        return switch (from) {
            case pending -> to == AppointmentStatus.confirmed || to == AppointmentStatus.cancelled;
            case confirmed -> to == AppointmentStatus.in_progress || to == AppointmentStatus.cancelled;
            case in_progress -> to == AppointmentStatus.completed;
            case completed, cancelled -> false;
        };
    }


    private Appointment getOrThrow(Integer id) {
        return appointmentRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Cita no encontrada con el id " + id));
    }

}














