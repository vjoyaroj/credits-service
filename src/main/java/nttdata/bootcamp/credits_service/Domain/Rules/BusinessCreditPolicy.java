package nttdata.bootcamp.credits_service.Domain.Rules;

import com.bank.credit.model.CreditCreateRequest;
import lombok.RequiredArgsConstructor;
import nttdata.bootcamp.credits_service.Dto.CustomerDto;
import nttdata.bootcamp.credits_service.Entity.CreditDocument;
import nttdata.bootcamp.credits_service.Mapper.CreditMapper;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

/**
 * Rules for BUSINESS credits: requires an ENTERPRISE customer.
 */
@Component
@RequiredArgsConstructor
public class BusinessCreditPolicy implements CreditRulesPolicy {
    private final CreditMapper mapper;

    /**
     * {@inheritDoc}
     */
    @Override
    public CreditCreateRequest.TypeEnum supportedType() {
        return CreditCreateRequest.TypeEnum.BUSINESS;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Mono<CreditDocument> apply(CreditCreateRequest request, CustomerDto customer, String creditNumber) {
        if (!"ENTERPRISE".equals(customer.getCustomerType())) {
            return Mono.error(new IllegalArgumentException("A BUSINESS credit requires an ENTERPRISE customer type"));
        }
        return Mono.just(mapper.mapToDocument(request, creditNumber));
    }
}
