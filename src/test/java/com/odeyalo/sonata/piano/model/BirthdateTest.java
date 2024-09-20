package com.odeyalo.sonata.piano.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.Month;

import static org.assertj.core.api.Assertions.assertThat;

class BirthdateTest {

    @Test
    void shouldReturnTrueIfBirthdateIsOlderThan() {
        Birthdate birthdate = Birthdate.of(LocalDate.of(2000, Month.MAY, 20));

        assertThat(birthdate.isOlderThan(13)).isTrue();
    }

    @Test
    void shouldReturnFalseIfBirthdateIsNotOlderThan() {
        Birthdate birthdate = Birthdate.of(LocalDate.now());

        assertThat(birthdate.isOlderThan(13)).isFalse();
    }
}