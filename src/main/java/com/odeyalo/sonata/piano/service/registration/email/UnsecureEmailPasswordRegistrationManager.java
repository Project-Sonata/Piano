package com.odeyalo.sonata.piano.service.registration.email;

import com.odeyalo.sonata.piano.model.User;
import com.odeyalo.sonata.piano.model.factory.UserFactory;
import com.odeyalo.sonata.piano.service.UserService;
import com.odeyalo.sonata.piano.service.registration.support.RegistrationFormValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import lombok.extern.log4j.Log4j;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public final class UnsecureEmailPasswordRegistrationManager implements EmailPasswordRegistrationManager {
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