package com.odeyalo.sonata.piano.service;

import com.odeyalo.sonata.piano.exception.BirthdatePolicyViolationException;
import com.odeyalo.sonata.piano.model.User;
import com.odeyalo.sonata.piano.model.factory.UserFactory;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public final class UnsecureEmailPasswordRegistrationManager implements EmailPasswordRegistrationManager {
    private final UserFactory userFactory;
    private final UserService userService;
    private final BirthdatePolicy birthdatePolicy;

    public UnsecureEmailPasswordRegistrationManager(final UserFactory userFactory,
                                                    final UserService userService,
                                                    final BirthdatePolicy birthdatePolicy) {
        this.userFactory = userFactory;
        this.userService = userService;
        this.birthdatePolicy = birthdatePolicy;
    }

    @Override
    @NotNull
    public Mono<RegistrationResult> registerUser(@NotNull final RegistrationForm form) {


        return birthdatePolicy.isAllowed(form.birthdate())
                .flatMap(decision -> {
                    if (decision) {
                        return tryRegisterUser(form);
                    }
                    return birthdateViolationError();
                });
    }

    @NotNull
    private static Mono<RegistrationResult> birthdateViolationError() {
        return Mono.defer(() -> Mono.error(
                new BirthdatePolicyViolationException()
        ));
    }

    private @NotNull Mono<RegistrationResult> tryRegisterUser(final @NotNull RegistrationForm form) {
        final User user = userFactory.createUser(form);
        return userService.save(user)
                .map(RegistrationResult::completedFor);
    }
}