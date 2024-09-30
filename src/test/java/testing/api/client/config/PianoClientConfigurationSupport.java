package testing.api.client.config;

import org.springframework.context.annotation.Bean;
import org.springframework.test.web.reactive.server.WebTestClient;
import testing.api.client.PianoClient;
import testing.api.client.WebTestClientPianoClient;

public class PianoClientConfigurationSupport {

    @Bean
    public PianoClient pianoClient(final WebTestClient client) {
        return new WebTestClientPianoClient(client);
    }
}
