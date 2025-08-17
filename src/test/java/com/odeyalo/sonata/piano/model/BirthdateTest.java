package com.odeyalo.sonata.piano.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.Month;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class BirthdateTest {

    @Test
    void shouldReturnTrueIfBirthdateIsOlderThan() {
        final Birthdate birthdate = Birthdate.of(LocalDate.of(2000, Month.MAY, 20));

        assertThat(birthdate.isOlderThan(13)).isTrue();
    }

    @Test
    void shouldReturnFalseIfBirthdateIsNotOlderThan() {
        final Birthdate birthdate = Birthdate.of(LocalDate.now());

        assertThat(birthdate.isOlderThan(13)).isFalse();
    }

    @Test
    void shouldThrowErrorIfBirthdateIsInTheFuture() {
        assertThatThrownBy(() -> Birthdate.of(LocalDate.now().plusDays(5)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid birthdate: Birthdate cannot be in the future.");
    }
}