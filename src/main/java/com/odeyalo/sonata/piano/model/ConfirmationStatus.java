package com.odeyalo.sonata.piano.model;

import org.jetbrains.annotations.NotNull;

public enum ConfirmationStatus {
    OK,
    DENIED;

    @NotNull
    public static ConfirmationStatus fromBoolean(boolean from) {
        return from ? OK : DENIED;
    }
}
