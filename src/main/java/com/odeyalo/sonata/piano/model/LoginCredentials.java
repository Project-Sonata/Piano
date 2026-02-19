package com.odeyalo.sonata.piano.model;

import org.jetbrains.annotations.NotNull;

/**
 * Value object representing credentials used for login
 */
public record LoginCredentials(@NotNull Email email, @NotNull String password) {

    @NotNull
    public static LoginCredentials of(@NotNull final Email email, @NotNull final String password) {
        return new LoginCredentials(email, password);
    }
}
