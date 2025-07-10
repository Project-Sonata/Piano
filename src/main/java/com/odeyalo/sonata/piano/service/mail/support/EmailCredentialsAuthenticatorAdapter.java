package com.odeyalo.sonata.piano.service.mail.support;

import jakarta.mail.Authenticator;
import jakarta.mail.PasswordAuthentication;
import org.jetbrains.annotations.NotNull;

/**
 * Adapter to use {@link EmailCredentials} as source of credentials that are used to connect
 * to SMTP server using {@link Authenticator}
 */
public final class EmailCredentialsAuthenticatorAdapter extends Authenticator {
    private final EmailCredentials emailCredentials;

    public EmailCredentialsAuthenticatorAdapter(@NotNull final EmailCredentials emailCredentials) {
        this.emailCredentials = emailCredentials;
    }

    @Override
    @NotNull
    protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(
                emailCredentials.username(),
                emailCredentials.password()
        );
    }
}
