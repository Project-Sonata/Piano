package com.odeyalo.sonata.piano.service.confirmation;

import com.odeyalo.sonata.piano.config.support.ConfirmationCodeFactories;
import com.odeyalo.sonata.piano.exception.InvalidConfirmationCodeException;
import com.odeyalo.sonata.piano.model.User;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import testing.UserFaker;

import static com.odeyalo.sonata.piano.model.ConfirmationStatus.DENIED;
import static com.odeyalo.sonata.piano.model.ConfirmationStatus.OK;
import static org.assertj.core.api.Assertions.assertThat;

class EmailConfirmationManagerTest {
    static final User USER = UserFaker.create().get();

    @Test
    void shouldConfirmEmailWhenCorrectConfirmationCodeIsUsed() {

        final EmailConfirmationManager testable = new EmailConfirmationManager(
                code -> Mono.just(new ConfirmationCode("111111", USER))
        );

        testable.confirmEmail("111111")
                .as(StepVerifier::create)
                .assertNext(status -> assertThat(status).isEqualTo(OK))
                .verifyComplete();
    }

    @Test
    void shouldNotConfirmEmailWhenIncorrectConfirmationCodeIsUsed() {
        final EmailConfirmationManager testable = new EmailConfirmationManager(
                code -> Mono.empty()
        );

        testable.confirmEmail("000000")
                .as(StepVerifier::create)
                .assertNext(status -> assertThat(status).isEqualTo(DENIED))
                .verifyComplete();
    }

    @Test
    void shouldNotConfirmEmailWhenExpiredConfirmationCodeIsUsed() {
        final EmailConfirmationManager testable = new EmailConfirmationManager(
                code -> Mono.error(new InvalidConfirmationCodeException("Invalid confirmation code"))
        );

        testable.confirmEmail("000000")
                .as(StepVerifier::create)
                .assertNext(status -> assertThat(status).isEqualTo(DENIED))
                .verifyComplete();
    }
}