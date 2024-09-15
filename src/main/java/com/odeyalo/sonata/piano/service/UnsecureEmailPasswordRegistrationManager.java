package com.odeyalo.sonata.piano.service;

import com.odeyalo.sonata.piano.model.User;
import com.odeyalo.sonata.piano.model.UserId;
import com.odeyalo.sonata.piano.service.support.PasswordEncoder;
import org.jetbrains.annotations.NotNull;
import reactor.core.publisher.Mono;

import static com.odeyalo.sonata.piano.model.Gender.MALE;

public final class UnsecureEmailPasswordRegistrationManager implements EmailPasswordRegistrationManager {
    private final PasswordEncoder passwordEncoder;

    public UnsecureEmailPasswordRegistrationManager(final PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @NotNull
    public Mono<RegistrationResult> registerUser(@NotNull final RegistrationForm form) {

        return Mono.just(
                RegistrationResult.completedFor(
                        new User(
                                UserId.random(),
                                form.email(),
                                passwordEncoder.encode(form.password()),
                                form.gender(),
                                true,
                                false,
                                form.birthdate()
                                )
                        )
        );
    }
}