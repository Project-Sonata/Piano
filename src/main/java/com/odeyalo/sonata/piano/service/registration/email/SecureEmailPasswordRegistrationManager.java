package com.odeyalo.sonata.piano.service.registration.email;

import com.odeyalo.sonata.piano.model.User;
import com.odeyalo.sonata.piano.model.factory.UserFactory;
import com.odeyalo.sonata.piano.service.UserService;
import com.odeyalo.sonata.piano.service.confirmation.EmailConfirmationStrategy;
import com.odeyalo.sonata.piano.service.registration.support.RegistrationFormValidator;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

/**
 * An implementation of {@link EmailPasswordRegistrationManager} that requires email confirmation to activate the user
 */
@Component
@RequiredArgsConstructor
public final class SecureEmailPasswordRegistrationManager implements EmailPasswordRegistrationManager {
    private final UserFactory userFactory;
    private final UserService userService;
    private final RegistrationFormValidator registrationFormValidator;
    private final EmailConfirmationStrategy emailConfirmationStrategy;

    @Override
    @NotNull
    public Mono<RegistrationResult> registerUser(@NotNull final RegistrationForm form) {

        return registrationFormValidator.validate(form)
                .then(Mono.defer(() -> tryRegisterUser(form)));
    }

    @NotNull
    private Mono<RegistrationResult> tryRegisterUser(@NotNull final RegistrationForm form) {
        final User user = userFactory.createUnactivatedUser(form);

        return userService.save(user)
                .flatMap(u -> emailConfirmationStrategy.sendConfirmationFor(u.email(), u).thenReturn(u))
                .map(RegistrationResult::confirmEmailFor);
    }
}