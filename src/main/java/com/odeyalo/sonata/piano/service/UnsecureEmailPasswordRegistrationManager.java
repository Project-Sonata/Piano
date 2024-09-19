package com.odeyalo.sonata.piano.service;

import com.odeyalo.sonata.piano.model.User;
import com.odeyalo.sonata.piano.model.factory.UserFactory;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public final class UnsecureEmailPasswordRegistrationManager implements EmailPasswordRegistrationManager {
    private final UserFactory userFactory;
    private final UserService userService;

    public UnsecureEmailPasswordRegistrationManager(final UserFactory userFactory,
                                                    final UserService userService) {
        this.userFactory = userFactory;
        this.userService = userService;
    }

    @Override
    @NotNull
    public Mono<RegistrationResult> registerUser(@NotNull final RegistrationForm form) {

        final User user = userFactory.createUser(form);

        return userService.save(user)
                .map(RegistrationResult::completedFor);
    }
}