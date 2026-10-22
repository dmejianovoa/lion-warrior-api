package co.edu.sena.lionwarrior.repository;

import co.edu.sena.lionwarrior.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/** Acceso a la tabla users. Spring Data genera las consultas a partir del nombre del método. */
public interface UserRepository extends JpaRepository<User, String> {

    //Busca por Email(Usado por login)
    Optional<User> findByUser(String email);

    // Indica si ya existe un usuario con el mismo email (usado por registro)
    boolean existsByEmail(String email);
}
