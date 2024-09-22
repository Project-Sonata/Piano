package com.odeyalo.sonata.piano.service.registration.email;

import com.odeyalo.sonata.piano.model.User;
import com.odeyalo.sonata.piano.model.factory.UserFactory;
import com.odeyalo.sonata.piano.service.UserService;
import com.odeyalo.sonata.piano.service.registration.support.RegistrationFormValidator;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public final class SecureEmailPasswordRegistrationManager implements EmailPasswordRegistrationManager {
    private final UserFactory userFactory;
    private final UserService userService;
    private final RegistrationFormValidator registrationFormValidator;

    @Override
    @NotNull
    public Mono<RegistrationResult> registerUser(@NotNull final RegistrationForm form) {

        return registrationFormValidator.validate(form)
                .then(Mono.defer(() -> tryRegisterUser(form)));
    }

    @NotNull
    private Mono<RegistrationResult> tryRegisterUser(@NotNull final RegistrationForm form) {
        final User user = userFactory.createUser(form);
        return userService.save(user)
                .map(RegistrationResult::completedFor);
    }
}