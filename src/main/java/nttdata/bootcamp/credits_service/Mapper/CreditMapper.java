package nttdata.bootcamp.credits_service.Mapper;

import com.bank.credit.model.CreditCreateRequest;
import com.bank.credit.model.CreditResponse;
import com.bank.credit.model.CreditUpdateRequest;
import nttdata.bootcamp.credits_service.Entity.CreditDocument;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

/**
 * Maps between API models and {@link CreditDocument} persistence entities.
 */
@Component
public class CreditMapper {

    /**
     * Converts a stored document to an API response.
     *
     * @param doc persisted credit
     * @return API response model
     */
    public CreditResponse toDTO(CreditDocument doc) {

        CreditResponse response = new CreditResponse();

        response.setId(doc.getId());
        response.setCustomerId(doc.getCustomerId());
        response.setCreditNumber(doc.getCreditNumber());
        response.setType(CreditResponse.TypeEnum.valueOf(doc.getType()));
        response.setCreditLimit(doc.getCreditLimit());
        response.setAvailableBalance(doc.getAvailableBalance());
        response.setAmountPaid(doc.getAmountPaid());
        response.setStatus(doc.getStatus());
        response.setInterestRate(doc.getInterestRate());

        if (doc.getCreatedAt() != null) {
            response.setCreatedAt(OffsetDateTime.ofInstant(doc.getCreatedAt(), ZoneOffset.UTC));
        }

        return response;
    }

    /**
     * Builds a new document for creation with generated id and defaults.
     *
     * @param request create request
     * @param creditNumber assigned credit number
     * @return document ready to save
     */
    public CreditDocument mapToDocument(CreditCreateRequest request, String creditNumber) {

        CreditDocument doc = new CreditDocument();

        doc.setId(UUID.randomUUID().toString());
        doc.setCustomerId(request.getCustomerId());
        doc.setCreditNumber(creditNumber);
        doc.setType(request.getType().getValue());
        doc.setCreditLimit(request.getCreditLimit());
        doc.setAvailableBalance(request.getAvailableBalance() != null ? request.getAvailableBalance() : 0.0);
        doc.setAmountPaid(request.getAmountPaid() != null ? request.getAmountPaid() : 0.0);
        doc.setStatus("ACTIVE");
        doc.setInterestRate(request.getInterestRate() != null ? request.getInterestRate() : 0.0);
        doc.setCreatedAt(Instant.now());

        return doc;
    }

    /**
     * Applies update request fields onto an existing document.
     *
     * @param doc document to mutate
     * @param request partial update payload
     */
    public void updateDocument(CreditDocument doc, CreditUpdateRequest request) {

        doc.setCustomerId(request.getCustomerId());
        doc.setType(request.getType().getValue());
        doc.setCreditLimit(request.getCreditLimit());
        doc.setAvailableBalance(request.getAvailableBalance() != null ? request.getAvailableBalance() : doc.getAvailableBalance());
        doc.setAmountPaid(request.getAmountPaid() != null ? request.getAmountPaid() : doc.getAmountPaid());
        doc.setInterestRate(request.getInterestRate() != null ? request.getInterestRate() : doc.getInterestRate());
        if (request.getStatus() != null && !request.getStatus().isBlank()) {
            doc.setStatus(request.getStatus());
        }
    }
}
