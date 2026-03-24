package nttdata.bootcamp.credits_service.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Registers a shared {@link WebClient.Builder} bean for outbound HTTP calls.
 */
@Configuration
public class WebClientConfig {

    /**
     * @return shared {@link WebClient.Builder} for outbound reactive HTTP
     */
    @Bean
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder();
    }
}
