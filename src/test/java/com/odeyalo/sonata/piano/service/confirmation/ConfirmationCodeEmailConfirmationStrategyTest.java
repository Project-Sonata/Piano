package com.odeyalo.sonata.piano.service.confirmation;

import com.odeyalo.sonata.piano.model.Email;
import com.odeyalo.sonata.piano.service.mail.EmailTransport;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;
import testing.UserFaker;
import testing.mock.MockEmailTransport;

import static org.assertj.core.api.Assertions.assertThat;

class ConfirmationCodeEmailConfirmationStrategyTest {

    @Test
    void shouldSendConfirmationCodeToProvidedEmail() {
        MockEmailTransport emailTransport = new MockEmailTransport();

        var testable = TestableBuilder.builder()
                .sendWith(emailTransport)
                .build();

        testable.sendConfirmationFor(Email.valueOf("odeyalo@gmail.com"), UserFaker.create().get())
                .as(StepVerifier::create)
                .verifyComplete();

        assertThat(emailTransport.getSentMessages()).hasSize(1);
        assertThat(emailTransport.getFirstMessage().to()).isEqualTo(Email.valueOf("odeyalo@gmail.com"));
    }


    static class TestableBuilder {
        private EmailTransport emailTransport = new MockEmailTransport();

        public static TestableBuilder builder() {
            return new TestableBuilder();
        }

        public TestableBuilder sendWith(@NotNull final EmailTransport transport) {
            this.emailTransport = transport;
            return this;
        }

        public ConfirmationCodeEmailConfirmationStrategy build() {
            return new ConfirmationCodeEmailConfirmationStrategy(
                    emailTransport
            );
        }
    }
}
