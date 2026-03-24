package nttdata.bootcamp.credits_service.Service;

import nttdata.bootcamp.credits_service.Entity.SequenceDocument;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * Generates unique sequential credit numbers using the {@code sequences} collection in MongoDB.
 */
@Service
public class CreditNumberSequence {

    private static final String SEQUENCE_KEY = "creditNumber";

    private final ReactiveMongoTemplate mongoTemplate;

    @Value("${credits.number.prefix:CR-}")
    private String prefix;

    @Value("${credits.number.width:8}")
    private int width;

    /**
     * @param mongoTemplate reactive Mongo template for find-and-modify
     */
    public CreditNumberSequence(ReactiveMongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    /**
     * Returns the next formatted credit number (prefix + zero-padded sequence).
     *
     * @return new credit number string
     */
    public Mono<String> nextCreditNumber() {
        Query query = Query.query(Criteria.where("_id").is(SEQUENCE_KEY));
        Update update = new Update().inc("seq", 1);
        FindAndModifyOptions options = new FindAndModifyOptions().returnNew(true).upsert(true);
        return mongoTemplate.findAndModify(query, update, options, SequenceDocument.class)
                .map(doc -> prefix + String.format("%0" + width + "d", doc.getSeq()));
    }
}
