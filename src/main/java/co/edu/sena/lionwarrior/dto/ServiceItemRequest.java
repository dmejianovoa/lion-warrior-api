package co.edu.sena.lionwarrior.dto;


import jakarta.validation.constraints.*;
import java.math.BigDecimal;

/** Datos para crear o actualizar un servicio. */
public record ServiceItemRequest(

        @NotBlank(message = "El nombre del servicio es obligatorio.")
        @Size(max = 80, message = "Maximo 80 caracteres.")
        String serviceName,

        @Size(max = 255, message = "Descripción maxima de 255 caracteres")
        String serviceDescription,

        @NotNull(message = "El precio es obligatorio")
        @DecimalMin(value = "0.0", inclusive = false, message = "El precio debe ser mayor a 0")
        @Digits(integer = 8, fraction = 2, message = "El precio admite hasta 8 entereos y 2 decimales")
        BigDecimal servicePrice,

        @Positive(message = "La duración debe ser mayor a 0")
        Integer durationMin
) {
}
