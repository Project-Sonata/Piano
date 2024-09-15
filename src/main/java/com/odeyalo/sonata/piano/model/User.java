package com.odeyalo.sonata.piano.model;

import lombok.Value;
import org.jetbrains.annotations.NotNull;

@Value
public class User {
    @NotNull
    UserId id;
    @NotNull
    Email email;
    @NotNull
    String password;
    @NotNull
    Gender gender;
    boolean activated;
    boolean emailConfirmed;
    @NotNull
    Birthdate birthdate;

    public boolean isActivated() {
        return activated;
    }

    public boolean isEmailConfirmed() {
        return emailConfirmed;
    }
}
