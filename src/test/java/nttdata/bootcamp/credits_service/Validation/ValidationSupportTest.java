package nttdata.bootcamp.credits_service.Validation;

import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Tests for {@link ValidationSupport}: valid objects pass; invalid objects throw {@link ConstraintViolationException}.
 */
class ValidationSupportTest {

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
    private final ValidationSupport support = new ValidationSupport(validator);

    @Data
    private static class Sample {
        @NotBlank
        private String name;
    }

    /**
     * Violations should produce {@link ConstraintViolationException}.
     */
    @Test
    void validateOrThrow_whenInvalid_throws() {
        assertThrows(ConstraintViolationException.class, () -> support.validateOrThrow(new Sample()));
    }

    /**
     * Valid samples should be returned unchanged.
     */
    @Test
    void validateOrThrow_whenValid_returnsSame() {
        Sample s = new Sample();
        s.setName("ok");
        assertEquals(s, support.validateOrThrow(s));
    }
}
