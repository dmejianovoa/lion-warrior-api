package co.edu.sena.lionwarrior.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

/** Datos para crear o actualizar un producto. */
public record ProductRequest(

        @NotBlank(message = "El nombre del producto es obligatorio")
        @Size(max = 100, message = "El nombre no puede superar los 100 caracteres")
        String productName,

        // La columna de la BD admite máximo 100 caracteres
        @Size(max = 100, message = "La descripción no puede superar los 100 caracteres")
        String productDescription,

        @NotNull(message = "El precio al detal es obligatorio")
        @DecimalMin(value = "0.0", inclusive = false, message = "El precio a detal debe ser mayor a 0")
        @Digits(integer = 8, fraction = 2, message = "El precio a detal admite 8 enteros y 2 decimales")
        BigDecimal priceRetail,

        @NotNull(message = "El precio mayorista es obligatorio")
        @DecimalMin(value = "0.0", inclusive = false, message = "El precio a detal debe ser mayor a 0")
        @Digits(integer = 8, fraction = 2, message = "El precio mayorista admite 8 enteros y 2 decimales")
        BigDecimal priceWholesale,

        @NotNull(message = "El stock actual es obligatorio")
        @PositiveOrZero(message = "El stock actual no puede ser negativo")
        Integer currentStock,

        @NotNull(message = "El stock minimo es obligatorio")
        @PositiveOrZero(message = "El stock minimo no puede ser negativo")
        Integer minimumStock
) {
}
