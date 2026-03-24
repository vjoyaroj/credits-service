package nttdata.bootcamp.credits_service.Config;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Smoke test that {@link WebClientConfig} exposes a non-null {@link org.springframework.web.reactive.function.client.WebClient.Builder}.
 */
class WebClientConfigTest {

    /**
     * Bean method should return a builder instance.
     */
    @Test
    void webClientBuilder_isExposed() {
        assertNotNull(new WebClientConfig().webClientBuilder());
    }
}
