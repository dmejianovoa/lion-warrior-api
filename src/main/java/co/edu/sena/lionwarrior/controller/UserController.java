package co.edu.sena.lionwarrior.controller;

import co.edu.sena.lionwarrior.dto.UpdateUserRequest;
import co.edu.sena.lionwarrior.dto.UserResponse;
import co.edu.sena.lionwarrior.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/** Endpoints de consulta, actualización y eliminación de usuarios. */
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // GET /api/users -> 200 con la lista
    @GetMapping
    public List<UserResponse> getAll() {
        return userService.findAll();
    }

    // GET /api/users/{id} -> 200: ok o 404: not found
    @GetMapping("/{id}")
    public UserResponse getById(@PathVariable Integer id) {
        return userService.findById(id);
    }

    // PUT /api/users/{id} -> 200: ok, 404: not found o 409: conflict
    @PutMapping("/{id}")
    public UserResponse update(@PathVariable Integer id, @Valid @RequestBody UpdateUserRequest request) {
        return userService.update(id, request);
    }

    // DELETE /api/users/{id} -> 204, 404: not found, 409 si tiene registros relacionados
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
