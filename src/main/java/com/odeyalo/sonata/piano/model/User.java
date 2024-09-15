package com.odeyalo.sonata.piano.model;

import lombok.Value;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;

@Value
public class User {
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
