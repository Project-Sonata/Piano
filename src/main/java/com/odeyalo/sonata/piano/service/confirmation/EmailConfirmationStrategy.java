package com.odeyalo.sonata.piano.service.confirmation;

import com.odeyalo.sonata.piano.model.Email;
import com.odeyalo.sonata.piano.model.User;
import org.jetbrains.annotations.NotNull;
import reactor.core.publisher.Mono;

/**
 * A strategy to confirm email provided by user
 */
public interface EmailConfirmationStrategy {
    /**
     * Send the confirmation to provided email
     * @param emailToConfirm email that needs to be confirmed
     * @return a {@link Mono} with {@link Void} on success,
     * {@link Mono#error(Throwable)} on any exception
     */
    @NotNull
    Mono<Void> sendConfirmationFor(@NotNull Email emailToConfirm,
                                   @NotNull User confirmationFor);

}
