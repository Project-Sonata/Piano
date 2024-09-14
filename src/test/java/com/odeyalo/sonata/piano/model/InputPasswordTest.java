package com.odeyalo.sonata.piano.model;

import com.odeyalo.sonata.piano.exception.PasswordRegexException;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


class InputPasswordTest {

    @ParameterizedTest
    @ValueSource(strings = {
            "L@9mXp5Yv!",
            "hello123",
            "Z6%jTk1Pw8As"
    })
    void shouldNotReturnAnyErrorIfPasswordIsValid(@NotNull final String input) {
        assertThatCode(() -> InputPassword.valueOf(input))
                .doesNotThrowAnyException();
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "invalid", // no digits
            "12345689012", // no character
            "more", // less than 8 symbols
    })
    void shouldReturnErrorIfPasswordIsInvalid(@NotNull final String input) {

        assertThatThrownBy(() -> InputPassword.valueOf(input))
                .isInstanceOf(PasswordRegexException.class)
                .hasMessage("Password does not match regex, password must have 8 characters with at least one number");
    }
}