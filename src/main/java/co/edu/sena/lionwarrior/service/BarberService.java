package co.edu.sena.lionwarrior.service;

import co.edu.sena.lionwarrior.dto.BarberResponse;
import co.edu.sena.lionwarrior.dto.CreateBarberRequest;
import co.edu.sena.lionwarrior.dto.UpdateBarberRequest;
import co.edu.sena.lionwarrior.dto.UpdateUserRequest;
import co.edu.sena.lionwarrior.model.Barber;
import co.edu.sena.lionwarrior.model.User;
import co.edu.sena.lionwarrior.model.UserRole;
import co.edu.sena.lionwarrior.repository.BarberRepository;
import co.edu.sena.lionwarrior.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;


/**
 * Lógica de negocio de los barberos.
 * @Transactional: cada método es una sola operación; si algo falla, se deshace todo.
 */
@Service
@Transactional
public class BarberService {

    private final BarberRepository barberRepository;
    private final UserRepository userRepository;

    public BarberService(BarberRepository barberRepository, UserRepository userRepository) {
        this.barberRepository = barberRepository;
        this.userRepository = userRepository;
    }

    /** Lista los barberos. Si active viene, filtra por activos o inactivos.*/

    @Transactional(readOnly = true)
    public List<BarberResponse> findAll(Boolean active) {
        List<Barber> barbers = (active == null)
                ? barberRepository.findAll()
                : barberRepository.findByActive(active);
        return barbers.stream().map(BarberResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public BarberResponse findById(Integer id) {
        return BarberResponse.from(getOrThrow(id));
    }

    /** Crea el perfil de barbero de un usuario y le asigna el rol barber. */
    public BarberResponse create(CreateBarberRequest request) {
        User user = userRepository.findById(request.idUser())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Usuario no encontrado con el id" + request.idUser()));

        if (user.getUserRole() == UserRole.admin) {
            throw  new ResponseStatusException(HttpStatus.CONFLICT,
                    "Un administrador no puede registrarse como barbero");
        }
        if (barberRepository.existsByUserIdUser(user.getIdUser())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Ese usuario ya tiene perfil de barbero");
        }

        Barber barber = new Barber();
        barber.setUser(user);
        barber.setActive(true);
        barber.setSpecialty(request.specialty());
        barber.setAvailableSchedule(request.availableSchedule());

        // El usuario pasa a ser barbero; ambos cambios se guardan juntos
        user.setUserRole(UserRole.barber);
        userRepository.save(user);

        return BarberResponse.from(barberRepository.save(barber));
    }

    /** Actualiza solo los campos que lleguen con valor. */
    public BarberResponse update(Integer id, UpdateBarberRequest request) {
        Barber barber = getOrThrow(id);

        if (request.specialty() !=null) barber.setSpecialty(request.specialty());
        if (request.availableSchedule() !=null) barber.setAvailableSchedule(request.availableSchedule());
        if (request.active() !=null) barber.setActive(request.active());

        return BarberResponse.from(barberRepository.save(barber));
    }

    // Busca el barbero o responde 404
    private Barber getOrThrow(Integer id) {
        return barberRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Barbero no encontrado con el id " + id));
    }

}
