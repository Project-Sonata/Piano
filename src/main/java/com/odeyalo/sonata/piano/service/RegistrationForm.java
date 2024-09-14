package com.odeyalo.sonata.piano.service;

import com.odeyalo.sonata.piano.model.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import org.jetbrains.annotations.NotNull;

/**
 * A registration form to register user using email/password schema
 */
@Value
@Builder
@AllArgsConstructor(staticName = "of")
public class RegistrationForm {
    @NotNull
    Email email;
    @NotNull
    String password;
}
