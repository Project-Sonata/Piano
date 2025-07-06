package com.odeyalo.sonata.piano.service.confirmation;

import com.odeyalo.sonata.piano.exception.InvalidConfirmationCodeException;
import org.jetbrains.annotations.NotNull;
import reactor.core.publisher.Mono;

public interface ConfirmationCodeLoader {
    /**
     * Try to load the {@link ConfirmationCode} by its value
     *
     * @param value unique value of the code associated with specific {@link ConfirmationCode}
     * @return a {@link Mono} with {@link ConfirmationCode} if code is found AND valid, otherwise:
     * {@link Mono#empty()} - if code associated with this value DOES NOT exist
     * {@link Mono#error(Throwable)} with {@link InvalidConfirmationCodeException}
     * @throws InvalidConfirmationCodeException if code is invalid(expired, for example)
     */
    @NotNull
    Mono<ConfirmationCode> loadConfirmationCodeByValue(@NotNull String value);
}
