package co.edu.sena.lionwarrior.service;

import co.edu.sena.lionwarrior.dto.*;
import co.edu.sena.lionwarrior.model.User;
import co.edu.sena.lionwarrior.model.UserRole;
import co.edu.sena.lionwarrior.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

/** Lógica de negocio de usuarios: registro, login y CRUD. */
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // Spring entrega el repositorio y el encriptador (inyección de dependencias)
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /** Registra un usuario nuevo. Siempre queda con rol client y la contraseña encriptada. */
    public UserResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Ya existe un usuario con ese email");
        }

        User user = new User();
        user.setUserName(request.userName());
        user.setLastName(request.lastName());
        user.setEmail(request.email());
        user.setPhoneNumber(request.phoneNumber());
        user.setPasswordHash(passwordEncoder.encode(request.password()));
        // El rol nunca lo decide el cliente: evita que alguien se registre como admin
        user.setUserRole(UserRole.client);

        return UserResponse.from(userRepository.save(user));
    }

    /** Valída email y contraseña. Si algo falla, el mismo 401 (no revela si el email existe). */
    public UserResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.email())
                // Compara la contraseña enviada contra el hash guardado
                .filter(found -> passwordEncoder.matches(request.password(), found.getPasswordHash()))
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED, "Error en la autenticación"
                ));
        return UserResponse.from(user);
    }

    /** Lista todos los usuarios (sin contraseña). */
    public List<UserResponse> findAll() {
        return userRepository.findAll().stream().map(UserResponse::from).toList();
    }

    /** Busca un usuario por id. */
    public UserResponse findById(Integer id) {
        return UserResponse.from(getUserOrThrow(id));
    }

    /** Actualiza solo los campos que lleguen con valor. */
    public UserResponse update(Integer id, UpdateUserRequest request) {
        User user = getUserOrThrow(id);

        // Si cambia el email, que no lo tenga otro usuario
        if (request.email() != null && !request.email().equalsIgnoreCase(user.getEmail())
            && userRepository.existsByEmail(request.email())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Ya existe un usuario con ese email");
        }

        if (request.userName() != null) user.setUserName(request.userName());
        if (request.lastName() != null) user.setLastName(request.lastName());
        if (request.email() != null) user.setEmail(request.email());
        if (request.phoneNumber() != null) user.setPhoneNumber(request.phoneNumber());

        return UserResponse.from(userRepository.save(user));
    }

    /** Eliminar un usuario. Si tiene citas o ventas, la base de datos lo impide (RESTRICT) y responde 409.*/
    public  void delete(Integer id) {
        userRepository.delete(getUserOrThrow(id));
    }

    // Busca al usuario o responde un 404: Not Found
    private User getUserOrThrow(Integer id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Usuario no encontrado con el id" + id
                ));
    }
}
