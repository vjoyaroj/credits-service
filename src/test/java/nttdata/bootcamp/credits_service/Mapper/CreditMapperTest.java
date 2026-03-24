package nttdata.bootcamp.credits_service.Mapper;

import com.bank.credit.model.CreditCreateRequest;
import com.bank.credit.model.CreditResponse;
import com.bank.credit.model.CreditUpdateRequest;
import nttdata.bootcamp.credits_service.Entity.CreditDocument;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Tests for {@link CreditMapper}: DTO mapping, document creation defaults, and updates.
 */
class CreditMapperTest {

    private final CreditMapper mapper = new CreditMapper();

    /**
     * {@link CreditMapper#toDTO} should copy fields and convert timestamps to UTC offset.
     */
    @Test
    void toDTO_mapsFields() {
        Instant created = Instant.parse("2024-06-01T12:00:00Z");
        CreditDocument doc = CreditDocument.builder()
                .id("c1")
                .customerId("cust")
                .creditNumber("CR-1")
                .type("PERSONAL")
                .creditLimit(100d)
                .availableBalance(50d)
                .amountPaid(10d)
                .status("ACTIVE")
                .interestRate(1.5)
                .createdAt(created)
                .build();

        CreditResponse r = mapper.toDTO(doc);
        assertEquals("c1", r.getId());
        assertEquals(CreditResponse.TypeEnum.PERSONAL, r.getType());
        assertEquals(OffsetDateTime.ofInstant(created, ZoneOffset.UTC), r.getCreatedAt());
    }

    /**
     * {@link CreditMapper#mapToDocument} should assign id, status ACTIVE, and zero defaults for null numerics.
     */
    @Test
    void mapToDocument_setsDefaults() {
        CreditCreateRequest req = new CreditCreateRequest("cust", CreditCreateRequest.TypeEnum.BUSINESS, 200d);
        req.setAvailableBalance(null);
        req.setAmountPaid(null);
        req.setInterestRate(null);

        CreditDocument doc = mapper.mapToDocument(req, "CN-9");
        assertNotNull(doc.getId());
        assertEquals("CN-9", doc.getCreditNumber());
        assertEquals("ACTIVE", doc.getStatus());
        assertEquals(0.0, doc.getAvailableBalance());
    }

    /**
     * {@link CreditMapper#updateDocument} should merge update request into an existing document.
     */
    @Test
    void updateDocument_appliesRequest() {
        CreditDocument doc = new CreditDocument();
        doc.setAvailableBalance(1d);
        doc.setAmountPaid(2d);
        doc.setInterestRate(3d);

        CreditUpdateRequest u = new CreditUpdateRequest("x", CreditUpdateRequest.TypeEnum.CREDIT_CARD, 99d);
        u.setStatus("BLOCKED");

        mapper.updateDocument(doc, u);
        assertEquals("BLOCKED", doc.getStatus());
        assertEquals(CreditUpdateRequest.TypeEnum.CREDIT_CARD.getValue(), doc.getType());
    }
}
