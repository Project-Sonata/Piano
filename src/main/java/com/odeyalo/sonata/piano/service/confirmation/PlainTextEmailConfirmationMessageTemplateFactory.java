package com.odeyalo.sonata.piano.service.confirmation;

import com.odeyalo.sonata.piano.model.Email;
import com.odeyalo.sonata.piano.service.mail.EmailMessage;
import org.jetbrains.annotations.NotNull;

public final class PlainTextEmailConfirmationMessageTemplateFactory implements EmailConfirmationMessageTemplateFactory {

    @Override
    @NotNull
    public EmailMessage createEmailMessage(@NotNull final Email to,
                                           @NotNull final ConfirmationCode code) {

        return EmailMessage.builder()
                .to(to)
                .body("Your confirmation code: " + code.value())
                .subject("Your confirmation code for Sonata")
                .html(false)
                .build();
    }
}
