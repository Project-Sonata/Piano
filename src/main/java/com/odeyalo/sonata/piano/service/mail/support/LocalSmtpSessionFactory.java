package com.odeyalo.sonata.piano.service.mail.support;

import jakarta.mail.Session;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

import java.util.Properties;

/**
 * An implementation that used locally, primary for tests
 */
public final class LocalSmtpSessionFactory implements SmtpSessionFactory {

    private final Properties smtpProperties;
    private final Logger logger = LoggerFactory.getLogger(LocalSmtpSessionFactory.class);

    public LocalSmtpSessionFactory() {
        final Properties defaultSmtpProperties = new Properties();
        defaultSmtpProperties.put("mail.smtp.host", "localhost");
        defaultSmtpProperties.put("mail.smtp.port", "25");

        this.smtpProperties = defaultSmtpProperties;
        logger.info("Initialized LocalSmtpSessionFactory with default values {}", smtpProperties);
    }

    public LocalSmtpSessionFactory(@NotNull final Properties smtpProperties) {
        this.smtpProperties = smtpProperties;
        logger.info("Initialized LocalSmtpSessionFactory with custom values {}", smtpProperties);
    }

    @Override
    @NotNull
    public Mono<Session> getSession() {
        return Mono.fromCallable(() -> Session.getInstance(smtpProperties));
    }
}
