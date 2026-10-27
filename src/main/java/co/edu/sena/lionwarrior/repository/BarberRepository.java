package co.edu.sena.lionwarrior.repository;

import co.edu.sena.lionwarrior.model.Barber;
import co.edu.sena.lionwarrior.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/** Acceso a la tabla barbers. */
public interface BarberRepository extends JpaRepository<Barber, Integer> {

    // ¿Ya existe un perfil de barbero para ese usuario?
    boolean existsByUserIdUser(Integer idUser);

    // Barberos activos o inactivos
    List<Barber> findByActive(boolean active);
}
