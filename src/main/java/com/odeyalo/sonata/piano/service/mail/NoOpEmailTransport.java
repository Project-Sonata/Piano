package com.odeyalo.sonata.piano.service.mail;

import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@Profile({"dev", "local", "test"})
@Log4j2
public final class NoOpEmailTransport implements EmailTransport {

    public NoOpEmailTransport() {
        logger.warn("NoOpEmailTransport is used for [DEV] and [LOCAL] mode. All EmailMessage(s) will be printed in log stream");
    }

    @Override
    @NotNull
    public Mono<Void> sendEmail(@NotNull final EmailMessage message) {
        return Mono.fromRunnable(() -> logger.info("Sending the email message: {}", message));
    }
}
