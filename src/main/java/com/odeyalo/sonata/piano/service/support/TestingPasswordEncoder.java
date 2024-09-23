package com.odeyalo.sonata.piano.service.support;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public final class TestingPasswordEncoder implements PasswordEncoder {

    @Override
    @NotNull
    public String encode(@NotNull final CharSequence password) {
        return password + "123";
    }

    @Override
    public boolean matches(@NotNull final CharSequence password, @NotNull final String encodedPassword) {
        return Objects.equals(
                encode(password),
                encodedPassword
        );
    }
}
