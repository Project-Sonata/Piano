package com.odeyalo.sonata.piano.service;

import com.odeyalo.sonata.piano.exception.EmailAddressAlreadyInUseException;
import com.odeyalo.sonata.piano.model.User;
import com.odeyalo.sonata.piano.model.UserId;
import com.odeyalo.sonata.piano.service.support.PasswordEncoder;
import org.jetbrains.annotations.NotNull;
import reactor.core.publisher.Mono;

public final class UnsecureEmailPasswordRegistrationManager implements EmailPasswordRegistrationManager {
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;

    public UnsecureEmailPasswordRegistrationManager(final PasswordEncoder passwordEncoder,
                                                    final UserService userService) {
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
    }

    @Override
    @NotNull
    public Mono<RegistrationResult> registerUser(@NotNull final RegistrationForm form) {

        Mono<RegistrationResult> processRegistration = Mono.just(
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

        return userService.findByEmail(form.email())
                .flatMap(u -> Mono.error(new EmailAddressAlreadyInUseException()))
                .switchIfEmpty(processRegistration)
                .cast(RegistrationResult.class);
    }
}