package com.odeyalo.sonata.piano.model;

import com.odeyalo.sonata.piano.exception.EmailRegexException;
import org.apache.commons.validator.routines.EmailValidator;
import org.jetbrains.annotations.NotNull;

/**
 * Represent the valid email address
 *
 * @param value - valid email address
 */
public record Email(@NotNull String value) {

    /**
     * @param value - valid email address
     * @throws IllegalArgumentException if provided email address has invalid format
     */
    public Email {
        if ( !EmailValidator.getInstance().isValid(value) ) {
            throw new EmailRegexException("Invalid email: Email address does not match the email address pattern");
        }
    }

    public static Email valueOf(@NotNull final String value) {
        return new Email(value);
    }

    @NotNull
    public String asString() {
        return value;
    }
}
