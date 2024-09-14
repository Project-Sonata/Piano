package com.odeyalo.sonata.piano.model;

import lombok.Value;
import org.jetbrains.annotations.NotNull;

@Value
public class User {
    @NotNull
    Email email;
    @NotNull
    String password;
    boolean activated;
    boolean emailConfirmed;

    public boolean isActivated() {
        return activated;
    }

    public boolean isEmailConfirmed() {
        return emailConfirmed;
    }
}
