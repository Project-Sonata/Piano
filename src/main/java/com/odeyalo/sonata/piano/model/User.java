package com.odeyalo.sonata.piano.model;

import com.odeyalo.sonata.common.context.ContextUri;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import org.jetbrains.annotations.NotNull;

@Value
@Builder
@AllArgsConstructor
public class User {
    @NotNull
    UserId id;
    @NotNull
    Email email;
    // maybe i should move it to HashedPassword VO?
    // not sure about it, how validation of hashed password should be performed?
    // leave it like this for now, for simplicity
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

    @NotNull
    public ContextUri contextUri() {
        return id.toContextUri();
    }
}
