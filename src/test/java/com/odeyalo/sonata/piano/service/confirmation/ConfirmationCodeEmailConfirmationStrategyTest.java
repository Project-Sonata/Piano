package com.odeyalo.sonata.piano.service.confirmation;

import com.odeyalo.sonata.piano.model.Email;
import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;
import testing.UserFaker;
import testing.mock.MockEmailTransport;

import static org.assertj.core.api.Assertions.assertThat;

class ConfirmationCodeEmailConfirmationStrategyTest {

    @Test
    void shouldSendConfirmationCodeToProvidedEmail() {
        MockEmailTransport emailTransport = new MockEmailTransport();

        var testable = new ConfirmationCodeEmailConfirmationStrategy(
                emailTransport
        );

        testable.sendConfirmationFor(Email.valueOf("odeyalo@gmail.com"), UserFaker.create().get())
                .as(StepVerifier::create)
                .verifyComplete();

        assertThat(emailTransport.getSentMessages()).hasSize(1);
        assertThat(emailTransport.getFirstMessage().to()).isEqualTo(Email.valueOf("odeyalo@gmail.com"));
    }

}
