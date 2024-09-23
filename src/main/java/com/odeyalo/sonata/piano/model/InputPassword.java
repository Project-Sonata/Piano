package com.odeyalo.sonata.piano.model;

import com.odeyalo.sonata.piano.exception.PasswordRegexException;
import org.jetbrains.annotations.NotNull;

import java.util.regex.Pattern;

public record InputPassword(@NotNull CharSequence value) implements CharSequence {

    private static final String PASSWORD_REGEX = "^(?=.*[0-9])(?=.*[a-zA-Z]).{8,}$";

    public InputPassword {

        if ( !Pattern.compile(PASSWORD_REGEX).matcher(value).matches() ) {
            throw new PasswordRegexException("Password does not match regex, password must have 8 characters with at least one number");
        }
    }

    @NotNull
    public static InputPassword valueOf(@NotNull final CharSequence rawPassword) {
        return new InputPassword(rawPassword.toString());
    }

    @Override
    public int length() {
        return value.length();
    }

    @Override
    public char charAt(final int index) {
        return value.charAt(index);
    }

    @Override
    @NotNull
    public CharSequence subSequence(final int start, final int end) {
        return value.subSequence(start, end);
    }

    @Override
    @NotNull
    public String toString() {
        return value.toString();
    }
}
