package com.odeyalo.sonata.piano.api;

import com.odeyalo.sonata.piano.api.exchange.dto.ExceptionMessageDto;
import com.odeyalo.sonata.piano.api.exchange.dto.RegistrationResponseDto;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import com.odeyalo.sonata.piano.api.exchange.dto.RegistrationFormDto;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON;


@SpringBootTest
@AutoConfigureWebTestClient
@ActiveProfiles("test")
class EmailPasswordRegistrationEndpointTest {

    @Autowired
    WebTestClient webTestClient;

    @Test
    void shouldReturn200OkStatusIfRegistrationFormIsValid() {

        final RegistrationFormDto registrationForm = RegistrationFormDto.randomForm();

        final WebTestClient.ResponseSpec responseSpec = registrationRequest(registrationForm);

        responseSpec.expectStatus().isOk();
    }

    @Test
    void shouldReturnResponseIndicatingEmailMustBeConfirmed() {

        final RegistrationFormDto registrationForm = RegistrationFormDto.randomForm();

        final WebTestClient.ResponseSpec responseSpec = registrationRequest(registrationForm);

        responseSpec.expectBody(RegistrationResponseDto.class)
                .value(response -> assertThat(response.getAction()).isEqualTo("email_confirmation_required"));
    }

    @Test
    void shouldReturnErrorIfUserIsYoungerThan13YearsOld() {
        final RegistrationFormDto registrationForm = RegistrationFormDto.randomForm()
                .birthdate(LocalDate.now().minusYears(10));

        final WebTestClient.ResponseSpec responseSpec = registrationRequest(registrationForm);

        responseSpec.expectStatus().isBadRequest();

        responseSpec.expectBody(ExceptionMessageDto.class)
                .value(message -> assertThat(message.description()).isEqualTo("User must be older than 13 years"));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "invalid.com",
            "user@invalid_domain",
            "user@@example.com"
    })
    void shouldReturnErrorIfEmailAddressHasInvalidFormat(@NotNull final String email) {
        final RegistrationFormDto registrationForm = RegistrationFormDto.randomForm()
                .withEmail(email);

        final WebTestClient.ResponseSpec responseSpec = registrationRequest(registrationForm);

        responseSpec.expectStatus().isBadRequest();

        responseSpec
                .expectBody(ExceptionMessageDto.class)
                .value(message -> assertThat(message.description()).isEqualTo("Email has invalid format"));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "aboba", // must be more than 8 characters
            "mustContainAtLeastOneNumber",
            "1234567890" // must contain at least one character
    })
    void shouldReturnErrorIfPasswordIsInvalid(@NotNull final String password) {
        final RegistrationFormDto registrationForm = RegistrationFormDto.randomForm()
                .withPassword(password);

        final WebTestClient.ResponseSpec responseSpec = registrationRequest(registrationForm);

        responseSpec.expectStatus().isBadRequest();

        responseSpec
                .expectBody(ExceptionMessageDto.class)
                .value(message -> assertThat(message.description()).isEqualTo("Password must contain at least 8 characters and at least 1 number"));
    }

    @NotNull
    private WebTestClient.ResponseSpec registrationRequest(final RegistrationFormDto registrationForm) {
        return webTestClient
                .post()
                .uri("/v1/signup/email")
                .contentType(APPLICATION_JSON)
                .bodyValue(registrationForm)
                .exchange();
    }
}
