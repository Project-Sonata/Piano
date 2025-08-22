package com.odeyalo.sonata.piano.support.utils;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class TimeUtilTest {

    @ParameterizedTest
    @ValueSource(ints = {1, 9, 30, 366, 700})
    void shouldReturnTrueIfDateIsFuture(final int days) {
        final LocalDate futureDate = LocalDate.now().plusDays(days);

        assertThat(TimeUtil.isFuture(futureDate)).isTrue();
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 9, 30, 366, 700})
    void shouldReturnFalseIfDateIsInPast(final int days) {
        final LocalDate pastDate = LocalDate.now().minusDays(days);

        assertThat(TimeUtil.isFuture(pastDate)).isFalse();
    }

    @Test
    void shouldReturnFalseIfDateIsToday() {
        final LocalDate today = LocalDate.now();

        assertThat(TimeUtil.isFuture(today)).isFalse();
    }
}