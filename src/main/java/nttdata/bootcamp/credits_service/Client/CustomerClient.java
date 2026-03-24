package nttdata.bootcamp.credits_service.Client;

import nttdata.bootcamp.credits_service.Dto.CustomerDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.cloud.client.circuitbreaker.ReactiveCircuitBreakerFactory;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import org.springframework.cloud.client.circuitbreaker.ReactiveCircuitBreaker;

/**
 * HTTP client for customer-service with reactive circuit breaker.
 */
@Component
public class CustomerClient {

    private final WebClient webClient;
    private final ReactiveCircuitBreaker circuitBreaker;

    /**
     * @param builder shared WebClient builder
     * @param customerServiceUrl base URL for customer API
     * @param circuitBreakerFactory factory to create the customer-service breaker
     */
    public CustomerClient(WebClient.Builder builder,
                          @Value("${customer.service.url:http://localhost:8081/api/v1}") String customerServiceUrl,
                          ReactiveCircuitBreakerFactory<?, ?> circuitBreakerFactory) {
        this.webClient = builder.baseUrl(customerServiceUrl).build();
        this.circuitBreaker = circuitBreakerFactory.create("customerServiceCb");
    }

    /**
     * Fetches a customer by id.
     *
     * @param id customer identifier
     * @return customer DTO or error on fallback
     */
    public Mono<CustomerDto> getCustomerById(String id) {
        return circuitBreaker.run(
            webClient.get()
                .uri("/customers/{id}", id)
                .retrieve()
                .bodyToMono(CustomerDto.class),
            throwable -> Mono.error(new RuntimeException("Fallback: Customer Service is currently unavailable or taking too long. Details: " + throwable.getMessage()))
        );
    }
}
