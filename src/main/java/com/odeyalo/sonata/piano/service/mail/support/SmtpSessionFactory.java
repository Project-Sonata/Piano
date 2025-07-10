package com.odeyalo.sonata.piano.service.mail.support;

import jakarta.mail.Session;
import org.jetbrains.annotations.NotNull;
import reactor.core.publisher.Mono;

/**
 * Central interface to create and manage SMTP sessions that are used to send email messages
 */
public interface SmtpSessionFactory {

    @NotNull
    Mono<Session> getSession();

}
