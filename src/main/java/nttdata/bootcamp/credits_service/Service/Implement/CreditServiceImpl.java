package nttdata.bootcamp.credits_service.Service.Implement;

import com.bank.credit.model.CreditCreateRequest;
import com.bank.credit.model.CreditResponse;
import com.bank.credit.model.CreditUpdateRequest;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Single;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import nttdata.bootcamp.credits_service.Domain.Customer.CustomerLookup;
import nttdata.bootcamp.credits_service.Domain.Rules.CreditPolicyRegistry;
import nttdata.bootcamp.credits_service.Exception.OverdueDebtException;
import nttdata.bootcamp.credits_service.Mapper.CreditMapper;
import nttdata.bootcamp.credits_service.Repository.CreditRepository;
import nttdata.bootcamp.credits_service.Service.CreditNumberSequence;
import nttdata.bootcamp.credits_service.Service.CreditService;
import nttdata.bootcamp.credits_service.Validation.ValidationSupport;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.stereotype.Service;
import reactor.adapter.rxjava.RxJava3Adapter;

import java.time.Duration;
import java.util.Set;
import java.util.List;
import reactor.core.publisher.Mono;

/**
 * Default implementation of {@link CreditService} with Redis cache, customer lookup and credit policies.
 */
@Service
@RequiredArgsConstructor
public class CreditServiceImpl implements CreditService {

    private static final Set<String> OVERDUE_STATUSES = Set.of("OVERDUE", "VENCIDO");

    private final CreditRepository repository;
    private final CreditMapper mapper;
    private final CustomerLookup customerLookup;
    private final CreditPolicyRegistry policyRegistry;
    private final ValidationSupport validationSupport;
    private final CreditNumberSequence creditNumberSequence;
    private final ReactiveStringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper;

    @Value("${redis.cache.ttl-seconds:900}")
    private long cacheTtlSeconds;

    /**
     * Removes a credit entry from the Redis cache.
     *
     * @param creditId credit identifier
     * @return empty completion when delete finishes
     */
    private Mono<Void> evictCreditCache(String creditId) {
        String key = "credit:" + creditId;
        return redisTemplate.delete(key).then();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Single<List<CreditResponse>> findAll() {
        return RxJava3Adapter.fluxToFlowable(repository.findByStatus("ACTIVE"))
                .map(mapper::toDTO)
                .toList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Single<CreditResponse> createCredit(CreditCreateRequest request) {
        return RxJava3Adapter.monoToSingle(
                Mono.fromCallable(() -> validationSupport.validateOrThrow(request))
                        .then(repository.existsByCustomerIdAndStatusIn(request.getCustomerId(), OVERDUE_STATUSES)
                                .flatMap(hasOverdue -> hasOverdue
                                        ? Mono.error(new OverdueDebtException(
                                        "Cliente posee deuda vencida; no puede adquirir nuevos productos de crédito"))
                                        : Mono.empty()
                                )
                        )
                        .then(customerLookup.getCustomerById(request.getCustomerId()))
                        .switchIfEmpty(Mono.error(new RuntimeException("Customer not found")))
                        .flatMap(customer -> creditNumberSequence.nextCreditNumber()
                                .flatMap(num -> policyRegistry.resolve(request.getType()).apply(request, customer, num)))
                        .flatMap(repository::save)
                        .map(mapper::toDTO)
        ).flatMap(dto -> RxJava3Adapter.monoToSingle(
                evictCreditCache(dto.getId()).thenReturn(dto)
        ));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Maybe<CreditResponse> getCreditById(String id) {
        String cacheKey = "credit:" + id;
        return RxJava3Adapter.monoToMaybe(
                redisTemplate.opsForValue().get(cacheKey)
                        .flatMap(json -> {
                            try {
                                return Mono.just(objectMapper.readValue(json, CreditResponse.class));
                            } catch (JsonProcessingException e) {
                                return Mono.error(new RuntimeException("Failed to deserialize credit from cache", e));
                            }
                        })
                        .switchIfEmpty(repository.findById(id)
                                .map(mapper::toDTO)
                                .flatMap(dto -> {
                                    try {
                                        String json = objectMapper.writeValueAsString(dto);
                                        return redisTemplate.opsForValue()
                                                .set(cacheKey, json, Duration.ofSeconds(cacheTtlSeconds))
                                                .thenReturn(dto);
                                    } catch (JsonProcessingException e) {
                                        return Mono.error(new RuntimeException("Failed to serialize credit for cache", e));
                                    }
                                }))
        );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Single<CreditResponse> updateCredit(String id, CreditUpdateRequest request) {
        return RxJava3Adapter.monoToSingle(
                Mono.fromCallable(() -> validationSupport.validateOrThrow(request))
                        .then(repository.findById(id)
                                .switchIfEmpty(Mono.error(new RuntimeException("Credit not found")))
                                .doOnNext(doc -> mapper.updateDocument(doc, request))
                                .flatMap(repository::save)
                                .map(mapper::toDTO)
                        )
        ).flatMap(dto -> RxJava3Adapter.monoToSingle(
                evictCreditCache(dto.getId()).thenReturn(dto)
        ));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Completable deleteCredit(String id) {
        return RxJava3Adapter.monoToMaybe(repository.findById(id))
                .switchIfEmpty(Single.error(new RuntimeException("Credit not found")))
                .flatMapCompletable(doc -> RxJava3Adapter.monoToCompletable(
                        Mono.fromRunnable(() -> doc.setStatus("INACTIVE"))
                                .then(repository.save(doc))
                                .then(evictCreditCache(id))
                ));
    }
}
