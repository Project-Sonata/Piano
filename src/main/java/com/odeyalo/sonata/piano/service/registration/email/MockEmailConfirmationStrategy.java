package com.odeyalo.sonata.piano.service.registration.email;

import com.odeyalo.sonata.piano.model.Email;
import com.odeyalo.sonata.piano.model.User;
import com.odeyalo.sonata.piano.service.confirmation.EmailConfirmationStrategy;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.RandomStringUtils;
import org.jetbrains.annotations.NotNull;
import reactor.core.publisher.Mono;

@Log4j2
public final class MockEmailConfirmationStrategy implements EmailConfirmationStrategy {
    private boolean wasSent = false;

    @Override
    public @NotNull Mono<Void> sendConfirmationFor(@NotNull final Email email,
                                                   @NotNull final User user) {
        final String code = RandomStringUtils.randomNumeric(6);
        return Mono.fromRunnable(() -> {
            logger.info("Send confirmation code {} for {} ", code, email);
            wasSent = true;
        });
    }

    public boolean wasSent() {
        return wasSent;
    }
}
