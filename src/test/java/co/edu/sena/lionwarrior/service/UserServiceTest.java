package co.edu.sena.lionwarrior.service;

import co.edu.sena.lionwarrior.dto.LoginRequest;
import co.edu.sena.lionwarrior.dto.RegisterRequest;
import co.edu.sena.lionwarrior.dto.UserResponse;
import co.edu.sena.lionwarrior.model.User;
import co.edu.sena.lionwarrior.model.UserRole;
import co.edu.sena.lionwarrior.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    // Encriptador real: así se comprueba que el hash de verdad coincide
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    private UserService userService;

    @BeforeEach
    void setUp() {
        userService = new UserService(userRepository, passwordEncoder);
    }

    @Test
    void register_conDatosValidos_guardaClienteConPasswordEncriptada() {
        // Arrange: preparar los datos y el comportamiento del mock
        RegisterRequest request = new RegisterRequest(
                "Juan", "Perez", "juan@mail.com", "300 000 0000", "Clave1234");
        when(userRepository.existsByEmail("juan@mail.com")).thenReturn(false);
        when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

        // Act: ejecutar el método que se prueba
        UserResponse response = userService.register(request);

        // Assert: verificar el resultado
        assertEquals(UserRole.client, response.userRole());
        assertEquals("juan@mail.com", response.email());

        // Verifica qué se guardó de verdad: la contraseña NO debe ir en texto plano
        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(captor.capture());
        assertNotEquals("Clave1234", captor.getValue().getPasswordHash());
        assertTrue(passwordEncoder.matches("Clave1234", captor.getValue().getPasswordHash()));
    }

    @Test
    void register_conEmailRepetido_lanzaConflictoYNoGuarda() {
        // Arrange: preparar los datos y el comportamiento del mock (email ya existe)
        RegisterRequest request = new RegisterRequest(
                "Juan", "Perez", "juan@mail.com", "300 000 0000", "Clave1234");
        when(userRepository.existsByEmail("juan@mail.com"))
                .thenReturn(true);

        // Act: ejecutar y capturar la excepción que debe lanzar
        ResponseStatusException ex = assertThrows
                (ResponseStatusException.class,
                        () -> userService.register(request));

        // Assert: el código debe ser 409 y no se debió guardar nada
        assertEquals(HttpStatus.CONFLICT, ex.getStatusCode());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void login_InicioDeSesion_conDatosValidos() {
        // Crea usuario con email y password encriptada
        User user = new User();
        user.setEmail("juan@mail.com");
        user.setPasswordHash(passwordEncoder.encode("Clave1234"));

        when(userRepository.findByEmail("juan@mail.com")).thenReturn(Optional.of(user));
        // Arrange: preparar los datos y el comportamiento del mock (iniciar sesion correctamente)
        LoginRequest request = new LoginRequest(
                "juan@mail.com", "Clave1234");
        UserResponse userResponse = userService.login(request);

        // Assert: el login devuelve al usuario correcto
        assertEquals("juan@mail.com", userResponse.email());
    }

    @Test
    void login_conPasswordIncorrecta_lanzaNoAutorizado() {
        // Arrange: Crea usuario con su contraseña correcta encriptada
        User user = new User();
        user.setEmail("juan@mail.com");
        user.setPasswordHash(passwordEncoder.encode("Clave1234"));

        when(userRepository.findByEmail("juan@mail.com")).thenReturn(Optional.of(user));

        LoginRequest request = new LoginRequest(
                "juan@mail.com", "Clave2026");
        // Act: intenta iniciar sesión con una contraseña incorrecta y captura la excepción
        ResponseStatusException ex = assertThrows(
                ResponseStatusException.class,
                () -> userService.login(request));

        // Assert: el código debe ser 401 y no inicia sesion
        assertEquals(HttpStatus.UNAUTHORIZED, ex.getStatusCode());
    }
}