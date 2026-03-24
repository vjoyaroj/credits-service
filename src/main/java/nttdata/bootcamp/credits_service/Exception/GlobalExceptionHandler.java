package nttdata.bootcamp.credits_service.Exception;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

/**
 * Maps domain and validation exceptions to JSON error bodies and HTTP status codes.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    /**
     * Overdue debt blocks new credit products (HTTP 422).
     *
     * @param ex business rule exception
     * @return error payload
     */
    @ExceptionHandler(OverdueDebtException.class)
    public ResponseEntity<Map<String, String>> handleOverdueDebtException(OverdueDebtException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("message", ex.getMessage());

        return ResponseEntity
                .status(HttpStatusCode.valueOf(422))
                .body(error);
    }

    /**
     * Invalid arguments and business validation errors (HTTP 400).
     *
     * @param ex illegal argument
     * @return error payload
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleIllegalArgumentException(IllegalArgumentException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("message", ex.getMessage());

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(error);
    }

    /**
     * Bean Validation constraint violations (HTTP 400).
     *
     * @param ex validation exception
     * @return error payload
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Map<String, String>> handleConstraintViolationException(ConstraintViolationException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("message", ex.getMessage());

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(error);
    }

    /**
     * Generic runtime errors (e.g. not found); mapped to HTTP 404 in this handler.
     *
     * @param ex runtime exception
     * @return error payload
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, String>> handleRuntimeException(RuntimeException ex) {

        Map<String, String> error = new HashMap<>();
        error.put("message", ex.getMessage());

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(error);
    }
}
