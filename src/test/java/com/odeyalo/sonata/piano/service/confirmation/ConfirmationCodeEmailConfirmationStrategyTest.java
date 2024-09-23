package com.odeyalo.sonata.piano.service.confirmation;

import com.odeyalo.sonata.piano.model.Email;
import com.odeyalo.sonata.piano.service.mail.EmailMessage;
import com.odeyalo.sonata.piano.service.mail.EmailTransport;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;
import testing.UserFaker;
import testing.mock.MockEmailTransport;
import testing.mock.PrefixBodyEmailConfirmationMessageTemplateFactory;
import testing.mock.StaticEmailConfirmationMessageTemplateFactory;

import java.time.Instant;

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

    @Test
    void shouldSendConfirmationCodeMessageTemplateThatWasGenerated() {

        var message = EmailMessage.builder()
                .to(Email.valueOf("odeyalo@gmail.com"))
                .subject("Hello from Sonata!")
                .body("<p> Hello </p>")
                .html(true)
                .build();

        var emailTransport = new MockEmailTransport();

        var templateFactory = new StaticEmailConfirmationMessageTemplateFactory(
                message
        );

        var testable = TestableBuilder.builder()
                .templateMessageFactory(templateFactory)
                .sendWith(emailTransport)
                .build();

        testable.sendConfirmationFor(Email.valueOf("odeyalo@gmail.com"), UserFaker.create().get())
                .as(StepVerifier::create)
                .verifyComplete();

        assertThat(emailTransport.getSentMessages()).hasSize(1);
        assertThat(emailTransport.getFirstMessage()).isEqualTo(message);
    }

    @Test
    void shouldSendConfirmationCodeThatWasGenerated() {
        var emailTransport = new MockEmailTransport();

        ConfirmationCodeFactory codeFactory = user -> new ConfirmationCode(
                "123",
                Instant.now(),
                Instant.now().plusSeconds(360),
                user
        );

        var templateFactory = new PrefixBodyEmailConfirmationMessageTemplateFactory(
                "Your confirmation code: "
        );

        var testable = TestableBuilder.builder()
                .confirmationCodeFactory(codeFactory)
                .templateMessageFactory(templateFactory)
                .sendWith(emailTransport)
                .build();

        testable.sendConfirmationFor(Email.valueOf("odeyalo@gmail.com"), UserFaker.create().get())
                .as(StepVerifier::create)
                .verifyComplete();

        assertThat(emailTransport.getSentMessages()).hasSize(1);
        assertThat(emailTransport.getFirstMessage().body()).isEqualTo("Your confirmation code: 123");
    }


    static class TestableBuilder {
        private EmailTransport emailTransport = new MockEmailTransport();
        private EmailConfirmationMessageTemplateFactory templateMessageFactory = new PlainTextEmailConfirmationMessageTemplateFactory();
        private ConfirmationCodeFactory confirmationCodeFactory = new SimpleConfirmationCodeFactory();

        public static TestableBuilder builder() {
            return new TestableBuilder();
        }

        public TestableBuilder sendWith(@NotNull final EmailTransport transport) {
            this.emailTransport = transport;
            return this;
        }

        public TestableBuilder templateMessageFactory(@NotNull final EmailConfirmationMessageTemplateFactory templateFactory) {
            this.templateMessageFactory = templateFactory;
            return this;
        }

        public TestableBuilder confirmationCodeFactory(ConfirmationCodeFactory confirmationCodeFactory) {
            this.confirmationCodeFactory = confirmationCodeFactory;
            return this;
        }

        public ConfirmationCodeEmailConfirmationStrategy build() {
            return new ConfirmationCodeEmailConfirmationStrategy(
                    new InMemoryConfirmationCodeService(confirmationCodeFactory),
                    templateMessageFactory,
                    emailTransport
            );
        }
    }
}
