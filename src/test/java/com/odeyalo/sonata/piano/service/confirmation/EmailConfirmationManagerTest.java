package com.odeyalo.sonata.piano.service.confirmation;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static com.odeyalo.sonata.piano.model.ConfirmationStatus.OK;
import static org.assertj.core.api.Assertions.assertThat;

class EmailConfirmationManagerTest {


    @Test
    void shouldConfirmEmailWhenCorrectConfirmationCodeIsUsed() {

        final EmailConfirmationManager testable = new EmailConfirmationManager(
                code -> Mono.just(Boolean.TRUE)
        );

        testable.confirmEmail("111111")
                .as(StepVerifier::create)
                .assertNext(status -> assertThat(status).isEqualTo(OK))
                .verifyComplete();
    }
}