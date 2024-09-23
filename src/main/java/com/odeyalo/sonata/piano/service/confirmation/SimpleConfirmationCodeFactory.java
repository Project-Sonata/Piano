package com.odeyalo.sonata.piano.service.confirmation;

import com.odeyalo.sonata.piano.model.User;
import org.apache.commons.lang3.RandomStringUtils;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;

/**
 * Implementation that just generates a six-digit confirmation code
 */
public final class SimpleConfirmationCodeFactory implements ConfirmationCodeFactory {

    @Override
    @NotNull
    public ConfirmationCode newConfirmationCodeFor(@NotNull final User user) {
        String value = RandomStringUtils.randomNumeric(6);
        return new ConfirmationCode(value,
                Instant.now(),
                Instant.now().plusSeconds(360),
                user
        );
    }
}
