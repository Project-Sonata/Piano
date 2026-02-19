package com.odeyalo.sonata.piano.support;

import com.odeyalo.sonata.common.shared.ErrorDetails;
import org.jetbrains.annotations.NotNull;

public final class ErrorDetailsFactory {

    @NotNull
    public static ErrorDetails invalidCredentials() {
        return ErrorDetails.of(
                "invalid_credentials",
                "User does not exist or password is incorrect",
                "Try another credentials"
        );
    }
}
