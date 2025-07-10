package com.odeyalo.sonata.piano.service.confirmation;

import com.odeyalo.sonata.piano.model.Email;
import com.odeyalo.sonata.piano.service.mail.EmailMessage;
import org.jetbrains.annotations.NotNull;
import reactor.core.publisher.Mono;

public interface EmailConfirmationMessageTemplateFactory {


    @NotNull
    @Deprecated
    default EmailMessage createEmailMessage(@NotNull final Email to,
                                            @NotNull final ConfirmationCode code) {
        throw new UnsupportedOperationException("createEmailMessageAsync should be used!");
    }

    @NotNull
    default Mono<EmailMessage> createEmailMessageAsync(@NotNull final Email to,
                                                       @NotNull final ConfirmationCode code) {
        return Mono.fromCallable(() -> createEmailMessage(to, code));
    }

}
