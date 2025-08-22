package com.odeyalo.sonata.piano.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ConfirmationStatusTest {

    @Test
    void shouldReturnOkStatusWhenTrueIsUsed() {
        final ConfirmationStatus status = ConfirmationStatus.fromBoolean(true);

        assertThat(status).isEqualTo(ConfirmationStatus.OK);
    }

    @Test
    void shouldReturnDeniedStatusWhenFalseIsUsed() {
        final ConfirmationStatus status = ConfirmationStatus.fromBoolean(false);

        assertThat(status).isEqualTo(ConfirmationStatus.DENIED);
    }
}