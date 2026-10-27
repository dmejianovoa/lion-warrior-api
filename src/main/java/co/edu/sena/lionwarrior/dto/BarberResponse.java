package co.edu.sena.lionwarrior.dto;

import co.edu.sena.lionwarrior.model.Barber;
import co.edu.sena.lionwarrior.model.User;


/** Barbero tal como lo ve el cliente. No incluye email ni teléfono. */
public record BarberResponse(
        Integer idBarber,
        Integer idUser,
        String userName,
        String lastName,
        boolean active,
        String specialty,
        String availableSchedule
) {
    public static BarberResponse from(Barber barber) {
        User user = barber.getUser();
        return new BarberResponse(barber.getIdBarber(), user.getIdUser(),
                user.getUserName(), user.getLastName(), barber.getActive(), barber.getSpecialty(),
                barber.getAvailableSchedule());
    }
}
