package com.odeyalo.sonata.piano.service.mail;

import com.odeyalo.sonata.piano.exception.MailTransportException;
import org.jetbrains.annotations.NotNull;
import reactor.core.publisher.Mono;

/**
 * Transport to send the email messages in reactive way
 */
public interface EmailTransport {
    /**
     * Send the supplied message to {@link EmailMessage#to()}
     * @param message message payload with all necessary info
     * @return a {@link Mono} with {@link Void} on success,
     * {@link Mono#error(Throwable)} with {@link MailTransportException} on failure
     *
     * @throws MailTransportException if message was failed to be sent
     */
    @NotNull
    Mono<Void> sendEmail(@NotNull final EmailMessage message);

}
