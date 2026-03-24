package nttdata.bootcamp.credits_service.Entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Stores monotonic sequence counters for generated identifiers (e.g. credit numbers).
 */
@Data
@Document(collection = "sequences")
public class SequenceDocument {

    @Id
    private String id;

    private long seq;
}
