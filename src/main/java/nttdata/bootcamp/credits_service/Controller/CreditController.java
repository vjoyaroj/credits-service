package nttdata.bootcamp.credits_service.Controller;

import com.bank.credit.api.CreditsApi;
import com.bank.credit.model.CreditCreateRequest;
import com.bank.credit.model.CreditResponse;
import com.bank.credit.model.CreditUpdateRequest;
import io.reactivex.rxjava3.core.Single;
import lombok.RequiredArgsConstructor;
import nttdata.bootcamp.credits_service.Service.CreditService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Reactive REST controller implementing the OpenAPI-generated credits API.
 */
@RestController
@RequiredArgsConstructor
public class CreditController implements CreditsApi {

    private final CreditService creditService;

    /**
     * Returns all credits exposed by the service.
     *
     * @return reactive response with HTTP 200 and list body
     */
    @Override
    public Single<ResponseEntity<List<CreditResponse>>> getCredits() {
        return creditService.findAll()
                .map(ResponseEntity::ok);
    }

    /**
     * Creates a new credit from the request payload.
     *
     * @param creditCreateRequest creation payload
     * @return reactive response with HTTP 201 and created credit
     */
    @Override
    public Single<ResponseEntity<CreditResponse>> createCredit(CreditCreateRequest creditCreateRequest) {
        return creditService.createCredit(creditCreateRequest)
                .map(credit -> ResponseEntity.status(HttpStatus.CREATED).body(credit));
    }

    /**
     * Retrieves a credit by identifier.
     *
     * @param id credit id
     * @return reactive response with 200 if found, 404 otherwise
     */
    @Override
    public Single<ResponseEntity<CreditResponse>> getCreditById(String id) {
        return creditService.getCreditById(id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    /**
     * Updates an existing credit.
     *
     * @param id credit id
     * @param creditUpdateRequest update payload
     * @return reactive response with updated credit
     */
    @Override
    public Single<ResponseEntity<CreditResponse>> updateCredit(String id, CreditUpdateRequest creditUpdateRequest) {
        return creditService.updateCredit(id, creditUpdateRequest)
                .map(ResponseEntity::ok);
    }

    /**
     * Logically deletes a credit (typically marks inactive).
     *
     * @param id credit id
     * @return reactive response with HTTP 204 on success
     */
    @Override
    public Single<ResponseEntity<Void>> deleteCredit(String id) {
        return creditService.deleteCredit(id)
                .toSingleDefault(ResponseEntity.noContent().<Void>build());
    }
}
