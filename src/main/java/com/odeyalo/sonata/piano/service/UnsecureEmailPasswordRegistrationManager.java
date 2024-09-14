package com.odeyalo.sonata.piano.service;

import com.odeyalo.sonata.piano.exception.PasswordRegexException;
import com.odeyalo.sonata.piano.model.User;
import com.odeyalo.sonata.piano.service.support.PasswordEncoder;
import org.jetbrains.annotations.NotNull;
import reactor.core.publisher.Mono;

import java.util.regex.Pattern;

public final class UnsecureEmailPasswordRegistrationManager implements EmailPasswordRegistrationManager {
    private final PasswordEncoder passwordEncoder;

    public UnsecureEmailPasswordRegistrationManager(final PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @NotNull
    public Mono<RegistrationResult> registerUser(@NotNull final RegistrationForm form) {

        String pattern = "^(?=.*[0-9])(?=.*[a-zA-Z]).{8,}$";

        if ( !Pattern.compile(pattern).matcher(form.password()).matches() ) {
            return Mono.error(new PasswordRegexException("Password does not match regex"));
        }

        return Mono.just(
                RegistrationResult.completedFor(
                        new User(
                                form.email(),
                                passwordEncoder.encode(form.password()),
                                true,
                                false
                        )
                )
        );
    }
}