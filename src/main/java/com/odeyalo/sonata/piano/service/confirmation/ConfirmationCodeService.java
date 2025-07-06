package com.odeyalo.sonata.piano.service.confirmation;

import com.odeyalo.sonata.piano.model.User;
import org.jetbrains.annotations.NotNull;
import reactor.core.publisher.Mono;

public interface ConfirmationCodeService extends ConfirmationCodeLoader {
    /**
     * Create a new {@link ConfirmationCode} for the {@link User}
     * @param user a user to generate confirmation code for
     * @return {@link Mono} with generated {@link ConfirmationCode}
     */
    @NotNull
    Mono<ConfirmationCode> newConfirmationCodeFor(@NotNull final User user);

}
