package co.edu.sena.lionwarrior.dto;


import co.edu.sena.lionwarrior.model.User;
import co.edu.sena.lionwarrior.model.UserRole;

/** Usuario tal como lo ve el cliente. No incluye la contraseña ni su hash. */
public record UserResponse(
        Integer idUser,
        String userName,
        String lastName,
        String email,
        String phoneNumber,
        UserRole userRole
) {
    //Convierte la entidad en una respuesta pública
    public static UserResponse from(User user) {
        return new UserResponse(user.getIdUser(), user.getUserName(), user.getLastName(),
                user.getEmail(), user.getPhoneNumber(), user.getUserRole());
    }
}
