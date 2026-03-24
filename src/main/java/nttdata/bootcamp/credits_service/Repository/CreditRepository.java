package nttdata.bootcamp.credits_service.Repository;

import nttdata.bootcamp.credits_service.Entity.CreditDocument;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collection;

/**
 * Reactive MongoDB repository for {@link CreditDocument}.
 */
public interface CreditRepository extends ReactiveMongoRepository<CreditDocument, String> {
    /**
     * Finds credits by status value.
     *
     * @param status status filter (e.g. ACTIVE)
     * @return matching documents
     */
    Flux<CreditDocument> findByStatus(String status);

    /**
     * Whether a customer already has a credit of the given type and status.
     *
     * @param customerId customer id
     * @param type credit type string
     * @param status status value
     * @return true if such a credit exists
     */
    Mono<Boolean> existsByCustomerIdAndTypeAndStatus(String customerId, String type, String status);

    /**
     * Whether the customer has any credit in one of the given statuses.
     *
     * @param customerId customer id
     * @param statuses collection of status values
     * @return true if a match exists
     */
    Mono<Boolean> existsByCustomerIdAndStatusIn(String customerId, Collection<String> statuses);
}
