package nttdata.bootcamp.credits_service.Domain.Rules;

import com.bank.credit.model.CreditCreateRequest;
import nttdata.bootcamp.credits_service.Dto.CustomerDto;
import nttdata.bootcamp.credits_service.Entity.CreditDocument;
import reactor.core.publisher.Mono;

/**
 * Credit-type specific validation and mapping to a {@link CreditDocument}.
 */
public interface CreditRulesPolicy {
    /**
     * Credit type handled by this policy.
     *
     * @return supported enum value
     */
    CreditCreateRequest.TypeEnum supportedType();

    /**
     * Validates business rules and produces a document to persist.
     *
     * @param request create request
     * @param customer customer data from customer-service
     * @param creditNumber generated credit number
     * @return document to save
     */
    Mono<CreditDocument> apply(CreditCreateRequest request, CustomerDto customer, String creditNumber);
}
