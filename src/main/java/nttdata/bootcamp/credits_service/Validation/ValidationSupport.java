package nttdata.bootcamp.credits_service.Validation;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * Validates objects using Jakarta Bean Validation and aggregates violation messages.
 */
@Component
@RequiredArgsConstructor
public class ValidationSupport {
    private final Validator validator;

    /**
     * Runs validation; throws {@link ConstraintViolationException} when invalid.
     *
     * @param value object to validate
     * @param <T> value type
     * @return the same instance when valid
     */
    public <T> T validateOrThrow(T value) {
        Set<ConstraintViolation<T>> violations = validator.validate(value);
        if (!violations.isEmpty()) {
            String message = violations.stream()
                    .map(v -> v.getPropertyPath() + " " + v.getMessage())
                    .collect(Collectors.joining(", "));
            throw new ConstraintViolationException(message, (Set) violations);
        }
        return value;
    }
}

