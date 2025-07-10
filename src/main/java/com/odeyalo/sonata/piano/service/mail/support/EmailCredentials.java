package com.odeyalo.sonata.piano.service.mail.support;

import org.jetbrains.annotations.NotNull;

/**
 * Credentials that are used during SMTP connection to remote SMTP server
 */
public record EmailCredentials(@NotNull String username,
                               @NotNull String password) {
}
