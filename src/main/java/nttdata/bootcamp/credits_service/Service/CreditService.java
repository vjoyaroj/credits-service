package nttdata.bootcamp.credits_service.Service;

import com.bank.credit.model.CreditCreateRequest;
import com.bank.credit.model.CreditResponse;
import com.bank.credit.model.CreditUpdateRequest;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Single;

import java.util.List;

/**
 * Reactive contract for credit lifecycle operations.
 */
public interface CreditService {

    /**
     * Returns all active credits (or as defined by persistence query).
     *
     * @return list of credit responses
     */
    Single<List<CreditResponse>> findAll();

    /**
     * Creates a credit after validation and business rules.
     *
     * @param request creation payload
     * @return created credit
     */
    Single<CreditResponse> createCredit(CreditCreateRequest request);

    /**
     * Loads a credit by id, optionally using cache.
     *
     * @param id credit identifier
     * @return credit if present
     */
    Maybe<CreditResponse> getCreditById(String id);

    /**
     * Updates an existing credit.
     *
     * @param id credit identifier
     * @param request update payload
     * @return updated credit
     */
    Single<CreditResponse> updateCredit(String id, CreditUpdateRequest request);

    /**
     * Logically deletes a credit.
     *
     * @param id credit identifier
     * @return completion signal
     */
    Completable deleteCredit(String id);

}
