package nttdata.bootcamp.credits_service.Domain.Rules;

import com.bank.credit.model.CreditCreateRequest;
import lombok.RequiredArgsConstructor;
import nttdata.bootcamp.credits_service.Dto.CustomerDto;
import nttdata.bootcamp.credits_service.Entity.CreditDocument;
import nttdata.bootcamp.credits_service.Mapper.CreditMapper;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

/**
 * Rules for CREDIT_CARD credits: maps request to document without extra uniqueness checks.
 */
@Component
@RequiredArgsConstructor
public class CreditCardPolicy implements CreditRulesPolicy {
    private final CreditMapper mapper;

    @Override
    public CreditCreateRequest.TypeEnum supportedType() {
        return CreditCreateRequest.TypeEnum.CREDIT_CARD;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Mono<CreditDocument> apply(CreditCreateRequest request, CustomerDto customer, String creditNumber) {
        return Mono.just(mapper.mapToDocument(request, creditNumber));
    }
}
