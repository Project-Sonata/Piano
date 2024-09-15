package com.odeyalo.sonata.piano.model;


import com.odeyalo.sonata.piano.support.utils.TimeUtil;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

/**
 * Wrapper around the birthdate.
 * <p>
 * Rules applied for birthdate:
 * <ul>
 *     <li>Cannot be in the future</li>
 * </ul>
 *
 * @param value local date that represent the date of birth for the user
 */
public record Birthdate(@NotNull LocalDate value) {

    public Birthdate {
        if ( TimeUtil.isFuture(value) ) {
            throw new IllegalArgumentException("Invalid birthdate: Birthdate cannot be in the future.");
        }
    }

    @NotNull
    public static Birthdate of(@NotNull final LocalDate value) {
        return new Birthdate(value);
    }

    @NotNull
    public static Birthdate of(@NotNull final Date value) {

        final LocalDate localDate = value.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();

        return of(localDate);
    }

    @NotNull
    public LocalDate toLocalDate() {
        return value;
    }

    @Override
    public boolean equals(final Object obj) {
        if ( obj == this ) return true;

        if ( obj instanceof final Birthdate other ) {
            return value.equals(other.value);
        }

        if ( obj instanceof final LocalDate other ) {
            return value.equals(other);
        }

        return false;
    }
}
