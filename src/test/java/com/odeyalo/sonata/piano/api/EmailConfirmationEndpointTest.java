package com.odeyalo.sonata.piano.api;

import com.odeyalo.sonata.piano.api.exchange.dto.RegistrationFormDto;
import com.odeyalo.sonata.piano.service.registration.email.RegistrationForm;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import testing.api.client.PianoClient;
import testing.api.client.config.AutoConfigurePianoClient;

@SpringBootTest
@AutoConfigureWebTestClient
@AutoConfigurePianoClient
@ActiveProfiles("test")
class EmailConfirmationEndpointTest {

    @Autowired
    WebTestClient webTestClient;

    @Autowired
    PianoClient pianoClient;

    @Test
    void shouldReturnOkIfConfirmationCodeIsValid() {

        RegistrationFormDto form = RegistrationFormDto.randomForm()
                .withEmail("odeyalo@gmail.com");

        pianoClient.sendRegistrationForm(form);

        var validCode = "123456";

//        WebTestClient.ResponseSpec answer = sendEmailConfirmationWithCode(validCode);
//
//        answer.expectStatus().isOk();
    }
}
