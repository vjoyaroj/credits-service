package nttdata.bootcamp.credits_service.Exception;

/**
 * Thrown when a customer has overdue debt and must not acquire new credit products.
 */
public class OverdueDebtException extends RuntimeException {
    /**
     * @param message detail for API consumers
     */
    public OverdueDebtException(String message) {
        super(message);
    }
}
