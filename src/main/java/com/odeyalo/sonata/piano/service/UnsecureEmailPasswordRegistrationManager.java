package com.odeyalo.sonata.piano.service;

import com.odeyalo.sonata.piano.model.User;
import org.jetbrains.annotations.NotNull;
import reactor.core.publisher.Mono;

public final class UnsecureEmailPasswordRegistrationManager implements EmailPasswordRegistrationManager {

    @Override
    @NotNull
    public Mono<RegistrationResult> registerUser(@NotNull final RegistrationForm form) {
        return Mono.just(
                RegistrationResult.completedFor(
                        new User(form.email())
                )
        );
    }
}