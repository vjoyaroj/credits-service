package nttdata.bootcamp.credits_service.Exception;

import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Unit tests for {@link GlobalExceptionHandler} status codes and error body shape.
 */
class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    /**
     * Overdue debt should map to HTTP 422 with message key.
     */
    @Test
    void handleOverdue_returns422() {
        ResponseEntity<Map<String, String>> res =
                handler.handleOverdueDebtException(new OverdueDebtException("due"));
        assertEquals(422, res.getStatusCode().value());
        assertEquals("due", res.getBody().get("message"));
    }

    /**
     * {@link IllegalArgumentException} should map to 400 Bad Request.
     */
    @Test
    void handleIllegalArgument_returnsBadRequest() {
        ResponseEntity<Map<String, String>> res =
                handler.handleIllegalArgumentException(new IllegalArgumentException("bad"));
        assertEquals(HttpStatus.BAD_REQUEST, res.getStatusCode());
    }

    /**
     * Bean validation failures should map to 400 Bad Request.
     */
    @Test
    void handleConstraintViolation_returnsBadRequest() {
        ResponseEntity<Map<String, String>> res =
                handler.handleConstraintViolationException(
                        new ConstraintViolationException("v", null));
        assertEquals(HttpStatus.BAD_REQUEST, res.getStatusCode());
    }

    /**
     * Generic runtime errors should map to 404 in this handler.
     */
    @Test
    void handleRuntime_returnsNotFound() {
        ResponseEntity<Map<String, String>> res =
                handler.handleRuntimeException(new RuntimeException("x"));
        assertEquals(HttpStatus.NOT_FOUND, res.getStatusCode());
    }
}
