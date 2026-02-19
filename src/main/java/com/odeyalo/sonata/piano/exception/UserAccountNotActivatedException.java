package com.odeyalo.sonata.piano.exception;

import com.odeyalo.sonata.common.shared.ErrorDetails;
import lombok.EqualsAndHashCode;
import lombok.Value;
import org.jetbrains.annotations.NotNull;

/**
 * Exception thrown when user tries to login but account is not activated/email is not confirmed
 */
@EqualsAndHashCode(callSuper = true)
@Value
public final class UserAccountNotActivatedException extends RuntimeException {
    @NotNull
    ErrorDetails details;

    public UserAccountNotActivatedException(@NotNull final ErrorDetails details) {
        this.details = details;
    }
}
