package com.odeyalo.sonata.piano.service.confirmation.callback;

import com.odeyalo.sonata.piano.model.User;
import com.odeyalo.sonata.piano.service.confirmation.ConfirmationCode;
import org.jetbrains.annotations.NotNull;
import reactor.core.publisher.Mono;

/**
 * A callback interface used to handle the result of a user email confirmation attempt.
 * <p>
 * Implementations can override the default methods to provide custom logic
 * for both successful and failed confirmation flows. By default, both methods
 * return {@link Mono#empty()} and perform no action.
 */
public interface UserEmailConfirmationCallback {

    /**
     * Invoked when a user’s email has been successfully confirmed.
     *
     * @param user the user whose email was confirmed
     * @param code the confirmation code that was used
     * @return a {@link Mono} signaling completion of the success handling
     */
    @NotNull
    default Mono<Void> onSuccess(@NotNull final User user,
                                 @NotNull final ConfirmationCode code) {
        return Mono.empty();
    }

    /**
     * Invoked when the email confirmation process fails.
     *
     * @param code the confirmation code that could not be verified
     * @return a {@link Mono} signaling completion of the failure handling
     */
    @NotNull
    default Mono<Void> onFailure(@NotNull final ConfirmationCode code) {
        return Mono.empty();
    }
}
