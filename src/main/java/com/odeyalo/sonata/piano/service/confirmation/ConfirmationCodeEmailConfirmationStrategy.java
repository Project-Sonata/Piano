package com.odeyalo.sonata.piano.service.confirmation;

import com.odeyalo.sonata.piano.model.Email;
import com.odeyalo.sonata.piano.model.User;
import com.odeyalo.sonata.piano.service.mail.EmailTransport;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public final class ConfirmationCodeEmailConfirmationStrategy implements EmailConfirmationStrategy {
    private final ConfirmationCodeService confirmationCodeService;
    private final EmailConfirmationMessageTemplateFactory confirmationMessageTemplateFactory;
    private final EmailTransport emailTransport;

    public ConfirmationCodeEmailConfirmationStrategy(final ConfirmationCodeService confirmationCodeService,
                                                     final EmailConfirmationMessageTemplateFactory confirmationMessageTemplateFactory,
                                                     final EmailTransport emailTransport) {
        this.confirmationCodeService = confirmationCodeService;
        this.confirmationMessageTemplateFactory = confirmationMessageTemplateFactory;
        this.emailTransport = emailTransport;
    }

    @Override
    @NotNull
    public Mono<Void> sendConfirmationFor(@NotNull final Email emailToConfirm,
                                          @NotNull final User user) {
        return confirmationCodeService.newConfirmationCodeFor(user)
                .map(confirmationCode -> confirmationMessageTemplateFactory.createEmailMessage(emailToConfirm, confirmationCode))
                .flatMap(emailTransport::sendEmail);
    }
}



