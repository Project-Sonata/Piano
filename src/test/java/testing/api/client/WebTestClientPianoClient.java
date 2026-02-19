package testing.api.client;

import com.odeyalo.sonata.piano.api.dto.EmailPasswordLoginRequestDto;
import com.odeyalo.sonata.piano.api.exchange.dto.RegistrationFormDto;
import com.odeyalo.sonata.piano.api.exchange.dto.RegistrationResponseDto;
import org.jetbrains.annotations.NotNull;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.Objects;

import static org.springframework.http.MediaType.APPLICATION_JSON;

public final class WebTestClientPianoClient implements PianoClient {
    private final WebTestClient webTestClient;

    public WebTestClientPianoClient(final WebTestClient webTestClient) {
        this.webTestClient = webTestClient;
    }

    @Override
    @NotNull
    public RegistrationResponseDto sendRegistrationForm(@NotNull final RegistrationFormDto form) {
        final RegistrationResponseDto responseBody = webTestClient
                .post()
                .uri("/v1/signup/email")
                .contentType(APPLICATION_JSON)
                .bodyValue(form)
                .exchange()
                .expectBody(RegistrationResponseDto.class)
                .returnResult().getResponseBody();

        return Objects.requireNonNull(
                responseBody,
                "Missing response body for Email registration API, as it is required by SPEC, test is failing! " +
                        "Consider check the '/v1/signup/email' endpoint that it returns body correctly"
        );
    }

    @Override
    @NotNull
    public WebTestClient.ResponseSpec login(@NotNull final EmailPasswordLoginRequestDto loginRequest) {
        return webTestClient.post()
                .uri("/v1/login/email")
                .contentType(APPLICATION_JSON)
                .bodyValue(loginRequest)
                .exchange();
    }
}
