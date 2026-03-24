package nttdata.bootcamp.credits_service.Domain.Customer;

import lombok.RequiredArgsConstructor;
import nttdata.bootcamp.credits_service.Client.CustomerClient;
import nttdata.bootcamp.credits_service.Dto.CustomerDto;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

/**
 * Delegates customer resolution to {@link CustomerClient}.
 */
@Component
@RequiredArgsConstructor
public class CustomerLookupImpl implements CustomerLookup {
    private final CustomerClient customerClient;

    /**
     * {@inheritDoc}
     */
    @Override
    public Mono<CustomerDto> getCustomerById(String id) {
        return customerClient.getCustomerById(id);
    }
}

