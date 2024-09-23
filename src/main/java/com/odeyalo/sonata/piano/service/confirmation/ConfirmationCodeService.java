package com.odeyalo.sonata.piano.service.confirmation;

import com.odeyalo.sonata.piano.exception.InvalidConfirmationCodeException;
import com.odeyalo.sonata.piano.model.User;
import org.jetbrains.annotations.NotNull;
import reactor.core.publisher.Mono;

public interface ConfirmationCodeService {
    /**
     * Create a new {@link ConfirmationCode} for the {@link User}
     * @param user a user to generate confirmation code for
     * @return {@link Mono} with generated {@link ConfirmationCode}
     */
    @NotNull
    Mono<ConfirmationCode> newConfirmationCodeFor(@NotNull final User user);

    /**
     * Try to load the {@link ConfirmationCode} by its value
     * @param value unique value of the code associated with specific {@link ConfirmationCode}
     * @return a {@link Mono} with {@link ConfirmationCode} if code is found AND valid, otherwise:
     * {@link Mono#empty()} - if code associated with this value DOES NOT exist
     * {@link Mono#error(Throwable)} with {@link InvalidConfirmationCodeException}
     *
     * @throws InvalidConfirmationCodeException if code is invalid(expired, for example)
     */
    @NotNull
    Mono<ConfirmationCode> loadConfirmationCodeByValue(@NotNull final String value);

}
