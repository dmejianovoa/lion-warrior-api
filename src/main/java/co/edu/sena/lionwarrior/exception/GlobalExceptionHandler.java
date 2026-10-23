package co.edu.sena.lionwarrior.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

/** Convierte las excepciones en respuestas JSON {"mensaje": "..."} con el código HTTP correcto. */
@RestControllerAdvice
public class GlobalExceptionHandler {

    // Errores lanzados a propósito desde los services (401, 404, 409...)
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Map<String, String>> handleStatus(ResponseStatusException ex) {
        String reason = ex.getReason() != null ? ex.getReason() : "Error en la ejecución";
        return ResponseEntity.status(ex.getStatusCode()).body(Map.of("mensaje", reason));
    }

    // Falla una validación del body (@Valid): 400 con el mensaje del primer campo inválido
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidation(MethodArgumentNotValidException ex) {
        String mensaje = ex.getBindingResult().getFieldErrors().stream()
                .findFirst()
                .map(FieldError::getDefaultMessage)
                .orElse("Datos inválidos");
        return ResponseEntity.badRequest().body(Map.of("mensaje", "Bad Request: " + mensaje));
    }

    // Borrar o guardar algo que choca con una llave foránea o un valor único de la BD
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, String>> handleIntegrity(DataIntegrityViolationException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(Map.of("mensaje", "La operación no se puede realizar: hay registros relacionados o duplicados"));
    }
}
