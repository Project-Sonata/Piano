package com.odeyalo.sonata.piano.support.utils;

import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.time.chrono.ChronoLocalDate;

@UtilityClass
public final class TimeUtil {
    /**
     * Indicates if the {@link LocalDate} is in the future
     * @param localDate - date to check
     * @return - true if {@link LocalDate} is in the future, false otherwise
     */
    public static boolean isFuture(@NotNull final ChronoLocalDate localDate) {
        return LocalDate.now().isBefore(localDate);
    }
}
