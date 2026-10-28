package co.edu.sena.lionwarrior.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.security.Provider;

/** Un servicio dentro de una cita. Tabla appointment_services. */
@Entity
@Table(name = "appointment_services")
public class AppointmentItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_appointment_service")
    private Integer idAppointmentServices;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_appointment", nullable = false)
    private Appointment appointment;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_service", nullable = false)
    private ServiceItem service;

    // Precio del servicio al momento de agendar
    @Column(name = "subtotal", nullable = false, precision = 10, scale = 2)
    private BigDecimal subtotal;

    // Getters y Setters

    public Integer getIdAppointmentServices() {
        return idAppointmentServices;
    }

    public void setIdAppointmentServices(Integer idAppointmentServices) {
        this.idAppointmentServices = idAppointmentServices;
    }

    public Appointment getAppointment() {
        return appointment;
    }

    public void setAppointment(Appointment appointment) {
        this.appointment = appointment;
    }

    public ServiceItem getService() {
        return service;
    }

    public void setService(ServiceItem service) {
        this.service = service;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }
}
