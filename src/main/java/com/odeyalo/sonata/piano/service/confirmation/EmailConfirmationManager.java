package com.odeyalo.sonata.piano.service.confirmation;

import com.odeyalo.sonata.piano.model.ConfirmationStatus;
import org.jetbrains.annotations.NotNull;
import reactor.core.publisher.Mono;

public final class EmailConfirmationManager {
    private final EmailConfirmationCodeChecker emailConfirmationCodeChecker;

    public EmailConfirmationManager(final EmailConfirmationCodeChecker emailConfirmationCodeChecker) {
        this.emailConfirmationCodeChecker = emailConfirmationCodeChecker;
    }

    @NotNull
    public Mono<ConfirmationStatus> confirmEmail(@NotNull final String code) {
        return emailConfirmationCodeChecker.checkConfirmationCode(code)
                .map(ConfirmationStatus::fromBoolean);
    }
}
