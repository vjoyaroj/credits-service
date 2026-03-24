package nttdata.bootcamp.credits_service.Domain.Rules;

import com.bank.credit.model.CreditCreateRequest;
import lombok.RequiredArgsConstructor;
import nttdata.bootcamp.credits_service.Entity.CreditDocument;
import nttdata.bootcamp.credits_service.Mapper.CreditMapper;
import nttdata.bootcamp.credits_service.Repository.CreditRepository;
import nttdata.bootcamp.credits_service.Dto.CustomerDto;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

/**
 * Rules for PERSONAL credits: one active personal credit per customer and PERSONAL customer type.
 */
@Component
@RequiredArgsConstructor
public class PersonalCreditPolicy implements CreditRulesPolicy {
    private final CreditRepository repository;
    private final CreditMapper mapper;

    /**
     * {@inheritDoc}
     */
    @Override
    public CreditCreateRequest.TypeEnum supportedType() {
        return CreditCreateRequest.TypeEnum.PERSONAL;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Mono<CreditDocument> apply(CreditCreateRequest request, CustomerDto customer, String creditNumber) {
        if (!"PERSONAL".equals(customer.getCustomerType())) {
            return Mono.error(new IllegalArgumentException("A PERSONAL credit requires a PERSONAL customer type"));
        }

        return repository.existsByCustomerIdAndTypeAndStatus(request.getCustomerId(), "PERSONAL", "ACTIVE")
                .flatMap(exists -> {
                    if (exists) {
                        return Mono.error(new IllegalArgumentException("A personal customer can only have one personal credit"));
                    }
                    return Mono.just(mapper.mapToDocument(request, creditNumber));
                });
    }
}
