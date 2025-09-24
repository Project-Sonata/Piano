package com.odeyalo.sonata.piano.api;

import com.odeyalo.sonata.piano.api.dto.TokensDto;
import com.odeyalo.sonata.piano.api.exchange.dto.AccessTokenValidationResponseDto;
import com.odeyalo.sonata.piano.api.exchange.dto.EmailConfirmationCodeDto;
import com.odeyalo.sonata.piano.api.exchange.dto.RegistrationFormDto;
import com.odeyalo.sonata.piano.api.exchange.dto.ValidateAccessTokenDto;
import com.odeyalo.sonata.piano.exception.InvalidConfirmationCodeException;
import com.odeyalo.sonata.piano.model.User;
import com.odeyalo.sonata.piano.repository.UserRepository;
import com.odeyalo.sonata.piano.service.confirmation.ConfirmationCode;
import com.odeyalo.sonata.piano.service.confirmation.ConfirmationCodeService;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;
import testing.api.client.PianoClient;
import testing.api.client.config.AutoConfigurePianoClient;
import testing.base.AbstractIntegrationTest;

import java.io.IOException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@SpringBootTest
@AutoConfigureWebTestClient
@AutoConfigurePianoClient
@ActiveProfiles("test")
class EmailConfirmationEndpointTest extends AbstractIntegrationTest {

    @Autowired
    WebTestClient webTestClient;

    @Autowired
    PianoClient pianoClient;

    @Autowired
    UserRepository userRepository;

    static final String VALID_CONFIRMATION_CODE = "123456";
    static final String INVALID_CONFIRMATION_CODE = "666666";
    static final String EXPIRED_CONFIRMATION_CODE = "111111";

    static final MockWebServer PROFILES_SERVICE = new MockWebServer();

    @BeforeAll
    static void prepare() throws Exception {
        PROFILES_SERVICE.start(0);
    }

    @DynamicPropertySource
    static void registerDynamicProperties(@NotNull final DynamicPropertyRegistry registry) {
        registry.add("sonata.profiles.url", () -> "http://localhost:" + PROFILES_SERVICE.getPort());
    }

    @BeforeEach
    void setUp() {
        userRepository.deleteAll().block();
    }

    @TestConfiguration
    static class ConfirmationCodeConfiguration {

        // ALERT!!!!
        // there is a bunch of code that just mocks some dependencies, LOL.
        // I just not sure about how to replace tests with MOCKS without mocks
        // it's just like I can make simple SMTP mock and parse messages, not sure about it!!!

        @Bean
        @Primary
        public ConfirmationCodeService confirmationCodeService() {

            Map<String, ConfirmationCode> cache = new ConcurrentHashMap<>();

            return new ConfirmationCodeService() {
                @Override
                public @NotNull Mono<ConfirmationCode> newConfirmationCodeFor(final @NotNull User user) {
                    ConfirmationCode code = ConfirmationCode.builder()
                            .value(VALID_CONFIRMATION_CODE)
                            .expiresIn(Instant.now().plus(10, ChronoUnit.HOURS))
                            .issuedAt(Instant.now())
                            .generatedFor(user)
                            .build();

                    return Mono.fromCallable(() -> {
                        cache.put(VALID_CONFIRMATION_CODE, code);
                        return code;
                    });
                }

                @Override
                public @NotNull Mono<ConfirmationCode> loadConfirmationCodeByValue(final @NotNull String value) {
                    return Mono.fromCallable(() -> {
                        switch (value) {
                            case VALID_CONFIRMATION_CODE -> {
                                return cache.get(VALID_CONFIRMATION_CODE);
                            }

                            case EXPIRED_CONFIRMATION_CODE ->
                                    throw new InvalidConfirmationCodeException("Code is expired");
                            default -> {
                                return null;
                            }
                        }
                    });
                }
            };
        }
    }


    @Test
    void shouldReturnOkIfConfirmationCodeIsValid() {
        PROFILES_SERVICE
                .enqueue(new MockResponse()
                        .setResponseCode(200)
                );

        final RegistrationFormDto form = RegistrationFormDto.randomForm()
                .withEmail("odeyalo@gmail.com");

        pianoClient.sendRegistrationForm(form);

        final WebTestClient.ResponseSpec answer = sendEmailConfirmationWithCode(VALID_CONFIRMATION_CODE);

        answer.expectStatus().isOk();
        answer.expectBody(TokensDto.class).value(tokens -> {
            assertThat(tokens).isNotNull();
            assertThat(tokens.accessToken()).isNotNull();
        });
    }

    @Test
    void shouldReturnValidAccessToken() {
        PROFILES_SERVICE
                .enqueue(new MockResponse()
                        .setResponseCode(200)
                );

        final RegistrationFormDto form = RegistrationFormDto.randomForm()
                .withEmail("odeyalo@gmail.com");

        pianoClient.sendRegistrationForm(form);

        final WebTestClient.ResponseSpec answer = sendEmailConfirmationWithCode(VALID_CONFIRMATION_CODE);

        answer.expectStatus().isOk();

        final TokensDto tokens = Objects.requireNonNull(answer.expectBody(TokensDto.class)
                .returnResult().getResponseBody());

        final String accessToken = tokens.accessToken();

        final var body = ValidateAccessTokenDto.builder()
                .accessToken(accessToken)
                .build();

        final WebTestClient.ResponseSpec responseSpec = webTestClient.post().uri("/v1/tokens/access")
                .bodyValue(body)
                .exchange();

        responseSpec.expectStatus().isOk();
        responseSpec.expectHeader().contentType(APPLICATION_JSON);
        responseSpec.expectBody(AccessTokenValidationResponseDto.class).value(response -> {
            assertThat(response.userId()).isNotNull();
            assertThat(response.expiresAt()).isNotNull();
        });
    }

    @Test
    void shouldReturnBadRequestIfConfirmationCodeIsInvalid() {
        RegistrationFormDto form = RegistrationFormDto.randomForm()
                .withEmail("odeyalo@gmail.com");

        pianoClient.sendRegistrationForm(form);

        WebTestClient.ResponseSpec answer = sendEmailConfirmationWithCode(INVALID_CONFIRMATION_CODE);

        answer.expectStatus().isBadRequest();
    }

    @NotNull
    private WebTestClient.ResponseSpec sendEmailConfirmationWithCode(@NotNull final String confirmationCode) {
        return webTestClient
                .post()
                .uri("/v1/signup/email/confirm")
                .contentType(APPLICATION_JSON)
                .bodyValue(new EmailConfirmationCodeDto(confirmationCode))
                .exchange();
    }
}
