package com.odeyalo.sonata.piano.service.confirmation;

import com.odeyalo.sonata.piano.model.User;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.jetbrains.annotations.NotNull;

import java.security.SecureRandom;
import java.time.Instant;

/**
 * Implementation that just generates a six-digit confirmation code
 */
public final class SimpleConfirmationCodeFactory implements ConfirmationCodeFactory {

    @Override
    @NotNull
    public ConfirmationCode newConfirmationCodeFor(@NotNull final User user) {
        SecureRandom random = new SecureRandom();
        int number = random.nextInt(900000) + 100000;

        return new ConfirmationCode(
                String.valueOf(number),
                Instant.now(),
                Instant.now().plusSeconds(360),
                user
        );
    }
}
