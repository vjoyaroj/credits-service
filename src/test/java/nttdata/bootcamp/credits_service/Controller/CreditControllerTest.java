package nttdata.bootcamp.credits_service.Controller;

import com.bank.credit.model.CreditCreateRequest;
import com.bank.credit.model.CreditResponse;
import com.bank.credit.model.CreditUpdateRequest;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Single;
import nttdata.bootcamp.credits_service.Service.CreditService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

/**
 * Unit tests for {@link CreditController}: verifies HTTP status mapping when delegating to {@link CreditService}.
 */
@ExtendWith(MockitoExtension.class)
class CreditControllerTest {
    @Mock
    private CreditService creditService;
    @InjectMocks
    private CreditController controller;

    /**
     * {@code GET} credits should return 200 OK with body from the service.
     */
    @Test
    void getCredits_shouldReturnOk() {
        when(creditService.findAll()).thenReturn(Single.just(List.of(new CreditResponse())));
        ResponseEntity<List<CreditResponse>> result = controller.getCredits().blockingGet();
        assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    /**
     * {@code POST} credit should return 201 Created and echo the created resource id.
     */
    @Test
    void createCredit_shouldReturnCreated() {
        CreditCreateRequest req = new CreditCreateRequest();
        CreditResponse resp = new CreditResponse();
        resp.setId("cr-1");
        when(creditService.createCredit(req)).thenReturn(Single.just(resp));
        ResponseEntity<CreditResponse> result = controller.createCredit(req).blockingGet();
        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertEquals("cr-1", result.getBody().getId());
    }

    /**
     * {@code GET} by id returns 200 when present and 404 when the service returns empty.
     */
    @Test
    void getCreditById_shouldHandleFoundAndNotFound() {
        CreditResponse resp = new CreditResponse();
        when(creditService.getCreditById("1")).thenReturn(Maybe.just(resp));
        assertEquals(HttpStatus.OK, controller.getCreditById("1").blockingGet().getStatusCode());

        when(creditService.getCreditById("2")).thenReturn(Maybe.empty());
        assertEquals(HttpStatus.NOT_FOUND, controller.getCreditById("2").blockingGet().getStatusCode());
    }

    /**
     * {@code PUT} returns 200; {@code DELETE} returns 204 No Content.
     */
    @Test
    void updateAndDelete_shouldReturnExpectedStatus() {
        CreditUpdateRequest req = new CreditUpdateRequest();
        when(creditService.updateCredit("1", req)).thenReturn(Single.just(new CreditResponse()));
        assertEquals(HttpStatus.OK, controller.updateCredit("1", req).blockingGet().getStatusCode());

        when(creditService.deleteCredit("1")).thenReturn(Completable.complete());
        assertEquals(HttpStatus.NO_CONTENT, controller.deleteCredit("1").blockingGet().getStatusCode());
    }
}
