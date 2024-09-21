package com.odeyalo.sonata.piano.service.registration.email;

import com.odeyalo.sonata.piano.model.User;
import com.odeyalo.sonata.piano.model.factory.UserFactory;
import com.odeyalo.sonata.piano.service.UserService;
import com.odeyalo.sonata.piano.service.registration.support.RegistrationFormValidator;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public final class UnsecureEmailPasswordRegistrationManager implements EmailPasswordRegistrationManager {
    private final UserFactory userFactory;
    private final UserService userService;
    private final RegistrationFormValidator registrationFormValidator;

    public UnsecureEmailPasswordRegistrationManager(final UserFactory userFactory,
                                                    final UserService userService,
                                                    final RegistrationFormValidator registrationFormValidator) {
        this.userFactory = userFactory;
        this.userService = userService;
        this.registrationFormValidator = registrationFormValidator;
    }

    @Override
    @NotNull
    public Mono<RegistrationResult> registerUser(@NotNull final RegistrationForm form) {

        return registrationFormValidator.validate(form)
                .then(Mono.defer(() -> tryRegisterUser(form)));
    }

    private @NotNull Mono<RegistrationResult> tryRegisterUser(final @NotNull RegistrationForm form) {
        final User user = userFactory.createUser(form);
        return userService.save(user)
                .map(RegistrationResult::completedFor);
    }
}