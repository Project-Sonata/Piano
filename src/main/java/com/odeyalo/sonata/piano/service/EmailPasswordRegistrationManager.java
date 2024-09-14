package com.odeyalo.sonata.piano.service;

import org.jetbrains.annotations.NotNull;
import reactor.core.publisher.Mono;

/**
 * Contract to register user using email/password schema,
 *
 * @see UnsecureEmailPasswordRegistrationManager
 */
public interface EmailPasswordRegistrationManager {

    @NotNull
    Mono<RegistrationResult> registerUser(@NotNull RegistrationForm form);

}

