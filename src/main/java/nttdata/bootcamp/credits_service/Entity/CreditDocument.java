package nttdata.bootcamp.credits_service.Entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

/**
 * MongoDB document representing a credit product for a customer.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "credits")
public class CreditDocument {

    @Id
    private String id;

    private String customerId;

    private String creditNumber;

    private String type;

    private Double creditLimit;

    private Double availableBalance;

    private Double amountPaid;

    private String status;

    private Double interestRate;

    private Instant createdAt;

}
