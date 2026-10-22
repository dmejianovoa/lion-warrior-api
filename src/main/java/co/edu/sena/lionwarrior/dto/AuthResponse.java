package co.edu.sena.lionwarrior.dto;

/** Respuesta del registro y del login: un mensaje y el usuario. */
public record AuthResponse(String mensaje, UserResponse user) {
}
