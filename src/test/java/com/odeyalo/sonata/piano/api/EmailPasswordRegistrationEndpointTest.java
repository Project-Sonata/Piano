package com.odeyalo.sonata.piano.api;

import com.odeyalo.sonata.piano.api.dto.RegistrationFormDto;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.time.LocalDate;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@SpringBootTest
@AutoConfigureWebTestClient
@ActiveProfiles("test")
class EmailPasswordRegistrationEndpointTest {

    @Autowired
    WebTestClient webTestClient;

    @Test
    void shouldReturn200OkStatusIfCredentialsAreValid() {

        final RegistrationFormDto registrationForm = RegistrationFormDto.randomForm();

        final WebTestClient.ResponseSpec responseSpec = registrationRequest(registrationForm);

        responseSpec.expectStatus().isOk();
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
