package co.edu.sena.lionwarrior.controller;

import co.edu.sena.lionwarrior.dto.AuthResponse;
import co.edu.sena.lionwarrior.dto.LoginRequest;
import co.edu.sena.lionwarrior.dto.RegisterRequest;
import co.edu.sena.lionwarrior.dto.UserResponse;
import co.edu.sena.lionwarrior.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/** Endpoints de autenticación: registro e inicio de sesión. */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    // POST /api/auth/register -> 201 creación exitosa / 409 Usuario ya existente / 400 Dato faltante
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        UserResponse user = userService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new AuthResponse("Usuario creado correctamente", user));
    }

    // POST /api/auth/login -> 200 correcto, 401 Login fallido, 400 Dato faltante
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> logIn(@Valid @RequestBody LoginRequest request) {
        UserResponse user = userService.login(request);
        return ResponseEntity.ok(new AuthResponse("Autenticación exitosa.", user));
    }
}
