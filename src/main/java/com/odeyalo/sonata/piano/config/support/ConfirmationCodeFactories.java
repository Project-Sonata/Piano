package com.odeyalo.sonata.piano.config.support;

import com.odeyalo.sonata.piano.service.confirmation.ConfirmationCode;
import com.odeyalo.sonata.piano.service.confirmation.ConfirmationCodeFactory;
import org.jetbrains.annotations.NotNull;

public final class ConfirmationCodeFactories {

    @NotNull
    public static ConfirmationCodeFactory staticImpl(@NotNull final String mockCodeValue) {
        return user -> new ConfirmationCode(mockCodeValue, user);
    }
}
