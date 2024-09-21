package com.odeyalo.sonata.piano.service.registration.support;

import com.odeyalo.sonata.piano.exception.RegistrationFormPolicyViolationException;
import com.odeyalo.sonata.piano.service.registration.email.RegistrationForm;
import org.jetbrains.annotations.NotNull;
import reactor.core.publisher.Mono;

/**
 * A single step to validate the given {@link RegistrationForm},
 * used by {@link ChainRegistrationFormValidator}
 */
public interface RegistrationFormValidationStep {
    /**
     * A success completion of form validation by implementation
     */
    Mono<Void> SUCCESS = Mono.empty();

    /**
     * Validate the given form against registration rules and policy,
     * determine if user is allowed to be registered with provided data.
     * Example of policy can be that no user younger than 13 y.o. can be registered
     * @param form a registration form provided by user to be registered
     * @return {{@link #SUCCESS}} if validation completes successfully or
     * {@link Mono#error(Throwable)} with {@link RegistrationFormPolicyViolationException} if registration form violates policy of Sonata
     */
    @NotNull
    Mono<Void> validate(@NotNull RegistrationForm form);

}
