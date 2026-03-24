package nttdata.bootcamp.credits_service.Dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Customer projection deserialized from customer-service (profile/type and status).
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDto {
    private String id;
    /**
     * Matches customer-service JSON: primary property {@code customerProfile}, alias {@code customerType}.
     * Typical values: PERSONAL, ENTERPRISE, VIP, PYME.
     */
    @JsonProperty("customerProfile")
    @JsonAlias("customerType")
    private String customerType;
    private String status;
}
