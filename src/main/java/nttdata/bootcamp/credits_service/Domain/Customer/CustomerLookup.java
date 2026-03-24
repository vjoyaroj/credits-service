package nttdata.bootcamp.credits_service.Domain.Customer;

import nttdata.bootcamp.credits_service.Dto.CustomerDto;
import reactor.core.publisher.Mono;

/**
 * Abstraction for loading {@link CustomerDto} from customer-service.
 */
public interface CustomerLookup {
    /**
     * @param id customer identifier
     * @return customer data when found
     */
    Mono<CustomerDto> getCustomerById(String id);
}

