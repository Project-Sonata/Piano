package com.odeyalo.sonata.piano.service.mail.support;

import jakarta.mail.Authenticator;
import jakarta.mail.Session;
import org.jetbrains.annotations.NotNull;
import reactor.core.publisher.Mono;

import java.util.Properties;

public final class DefaultSmtpSessionFactory implements SmtpSessionFactory {
    private final Properties props;
    private final Authenticator authenticator;

    private static final Properties GMAIL_PROPERTIES = new Properties();

    static {
        GMAIL_PROPERTIES.put("mail.smtp.host", "smtp.gmail.com");
        GMAIL_PROPERTIES.put("mail.smtp.port", 587);
        GMAIL_PROPERTIES.put("mail.transport.protocol", "smtp");
        GMAIL_PROPERTIES.put("mail.smtp.auth", "true");
        GMAIL_PROPERTIES.put("mail.smtp.starttls.enable", "true");
        GMAIL_PROPERTIES.put("mail.debug", "true");
    }

    private DefaultSmtpSessionFactory(@NotNull final Properties props,
                                      @NotNull final EmailCredentials emailCredentials) {
        this.props = props;
        this.authenticator = new EmailCredentialsAuthenticatorAdapter(emailCredentials);
    }

    private DefaultSmtpSessionFactory(@NotNull final Properties props,
                                      @NotNull final Authenticator authenticator) {
        this.props = props;
        this.authenticator = authenticator;
    }

    /**
     * Factory method to create a configurable factory with custom properties and authenticator
     * @param props - properties for {@link Session}
     * @param authenticator - authenticator to use to connect to SMTP server
     * @return - {@link SmtpSessionFactory} with custom properties and authenticator
     */
    @NotNull
    public static DefaultSmtpSessionFactory of(@NotNull final Properties props,
                                               @NotNull final Authenticator authenticator) {
        return new DefaultSmtpSessionFactory(props, authenticator);
    }

    /**
     * A static factory method to create Gmail specific session factory
     *
     * @param emailCredentials - credentials that will be used to connect to GMail SMTP server
     * @return - A factory that returns Gmail sessions
     */
    @NotNull
    public static DefaultSmtpSessionFactory gmail(@NotNull final EmailCredentials emailCredentials) {
        return new DefaultSmtpSessionFactory(GMAIL_PROPERTIES, emailCredentials);
    }

    @Override
    @NotNull
    public Mono<Session> getSession() {
        return Mono.fromCallable(() -> Session.getDefaultInstance(props, authenticator));
    }
}
