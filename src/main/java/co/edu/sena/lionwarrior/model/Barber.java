package co.edu.sena.lionwarrior.model;

import jakarta.persistence.*;

/** Perfil de barbero: un usuario con rol barber más sus datos profesionales.
public class Barber {
} */

@Entity
@Table(name = "barbers")
public class Barber {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_barber")
    private Integer idBarber;

    // Cada barbero es exactamente un usuario (id_user es único en la tabla)
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_user", nullable = false, unique = true)
    private User user;

    // Columna is_active: un barbero se desactiva, no se borra
    @Column(name = "is_active", nullable = false)
    private Boolean isActive;

    @Column(name = "specialty", length = 100)
    private String specialty;

    @Column(name = "available_schedule", length = 100)
    private String availableSchedule;

    // (getters y setters)

    public Integer getIdBarber() {
        return idBarber;
    }

    public void setIdBarber(Integer idBarber) {
        this.idBarber = idBarber;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public String getSpecialty() {
        return specialty;
    }

    public void setSpecialty(String specialty) {
        this.specialty = specialty;
    }

    public String getAvailableSchedule() {
        return availableSchedule;
    }

    public void setAvailableSchedule(String availableSchedule) {
        this.availableSchedule = availableSchedule;
    }
}
