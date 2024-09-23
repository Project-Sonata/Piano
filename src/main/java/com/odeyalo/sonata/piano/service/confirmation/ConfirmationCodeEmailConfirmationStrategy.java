package com.odeyalo.sonata.piano.service.confirmation;

import com.odeyalo.sonata.piano.model.Email;
import com.odeyalo.sonata.piano.model.User;
import com.odeyalo.sonata.piano.service.mail.EmailMessage;
import com.odeyalo.sonata.piano.service.mail.EmailTransport;
import org.jetbrains.annotations.NotNull;
import reactor.core.publisher.Mono;

public final class ConfirmationCodeEmailConfirmationStrategy implements EmailConfirmationStrategy {
    private final EmailTransport emailTransport;

    public ConfirmationCodeEmailConfirmationStrategy(final EmailTransport emailTransport) {
        this.emailTransport = emailTransport;
    }

    @Override
    @NotNull
    public Mono<Void> sendConfirmationFor(@NotNull final Email emailToConfirm,
                                          @NotNull final User user) {

        final EmailMessage message = EmailMessage.builder()
                .to(emailToConfirm)
                .body("Your confirmation code: 123x456")
                .subject("Your confirmation code for Sonata")
                .html(false)
                .build();

        return emailTransport.sendEmail(message);
    }
}



