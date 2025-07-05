package com.odeyalo.sonata.piano.service.confirmation;

import com.odeyalo.sonata.piano.exception.InvalidConfirmationCodeException;
import com.odeyalo.sonata.piano.model.Email;
import com.odeyalo.sonata.piano.model.User;
import com.odeyalo.sonata.piano.service.mail.EmailTransport;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public final class ConfirmationCodeEmailConfirmationStrategy implements EmailConfirmationStrategy, EmailConfirmationCodeChecker {
    private final ConfirmationCodeService confirmationCodeService;
    private final EmailConfirmationMessageTemplateFactory confirmationMessageTemplateFactory;
    private final EmailTransport emailTransport;
    private final Logger logger = LoggerFactory.getLogger(ConfirmationCodeEmailConfirmationStrategy.class);

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
        logger.info("Starting the email confirmation for {}", emailToConfirm.masked());
        return confirmationCodeService.newConfirmationCodeFor(user)
                .map(confirmationCode -> confirmationMessageTemplateFactory.createEmailMessage(emailToConfirm, confirmationCode))
                .flatMap(emailTransport::sendEmail)
                .doOnSuccess(unused -> logger.info("A confirmation code has been successfully sent to {}", emailToConfirm.masked()));
    }

    @Override
    @NotNull
    public Mono<Boolean> checkConfirmationCode(@NotNull final String code) {
        return confirmationCodeService.loadConfirmationCodeByValue(code)
                .map(confirmationCode -> true)
                .defaultIfEmpty(false)
                .onErrorReturn(InvalidConfirmationCodeException.class, false);
    }
}
