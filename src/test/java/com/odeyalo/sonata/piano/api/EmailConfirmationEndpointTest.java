package com.odeyalo.sonata.piano.api;

import com.odeyalo.sonata.piano.api.exchange.dto.EmailConfirmationCodeDto;
import com.odeyalo.sonata.piano.api.exchange.dto.RegistrationFormDto;
import com.odeyalo.sonata.piano.exception.InvalidConfirmationCodeException;
import com.odeyalo.sonata.piano.model.User;
import com.odeyalo.sonata.piano.service.confirmation.ConfirmationCode;
import com.odeyalo.sonata.piano.service.confirmation.ConfirmationCodeService;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;
import testing.api.client.PianoClient;
import testing.api.client.config.AutoConfigurePianoClient;
import testing.base.AbstractIntegrationTest;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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

    public static final String VALID_CONFIRMATION_CODE = "123456";
    public static final String INVALID_CONFIRMATION_CODE = "666666";
    public static final String EXPIRED_CONFIRMATION_CODE = "111111";

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
        RegistrationFormDto form = RegistrationFormDto.randomForm()
                .withEmail("odeyalo@gmail.com");

        pianoClient.sendRegistrationForm(form);

        WebTestClient.ResponseSpec answer = sendEmailConfirmationWithCode(VALID_CONFIRMATION_CODE);

        answer.expectStatus().isOk();
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
