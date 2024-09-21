package com.odeyalo.sonata.piano.service.registration.support;

import com.odeyalo.sonata.piano.exception.RegistrationFormPolicyViolationException;
import com.odeyalo.sonata.piano.service.registration.email.RegistrationForm;
import org.jetbrains.annotations.NotNull;
import reactor.core.publisher.Mono;

/**
 * Strategy to validate the {@link RegistrationForm} provided by user
 */
public interface RegistrationFormValidator {

    /**
     * Validate the given {@link RegistrationForm}
     * @param form registration form provided by user
     * @return a {@link Mono} with {@link Void} on validation success,
     * {@link Mono#error(Throwable)} with {@link RegistrationFormPolicyViolationException} if registration form violates policy
     */
    @NotNull
    Mono<Void> validate(@NotNull RegistrationForm form);

}
