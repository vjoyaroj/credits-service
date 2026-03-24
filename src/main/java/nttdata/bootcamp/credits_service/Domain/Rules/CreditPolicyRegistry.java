package nttdata.bootcamp.credits_service.Domain.Rules;

import com.bank.credit.model.CreditCreateRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Resolves the {@link CreditRulesPolicy} implementation for a given credit type.
 */
@Component
public class CreditPolicyRegistry {
    private final Map<CreditCreateRequest.TypeEnum, CreditRulesPolicy> policies;

    /**
     * Builds an immutable map from each policy's {@link CreditRulesPolicy#supportedType()}.
     *
     * @param policies all policy beans
     */
    public CreditPolicyRegistry(List<CreditRulesPolicy> policies) {
        this.policies = policies.stream()
                .collect(Collectors.toUnmodifiableMap(CreditRulesPolicy::supportedType, Function.identity()));
    }

    /**
     * Returns the policy for the given type or throws if unknown.
     *
     * @param type credit type
     * @return matching policy
     */
    public CreditRulesPolicy resolve(CreditCreateRequest.TypeEnum type) {
        CreditRulesPolicy policy = policies.get(type);
        if (policy == null) {
            throw new IllegalArgumentException("Unknown credit type: " + type);
        }
        return policy;
    }
}
