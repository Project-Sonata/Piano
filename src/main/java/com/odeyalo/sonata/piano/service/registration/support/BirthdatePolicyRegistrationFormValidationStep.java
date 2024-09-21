package com.odeyalo.sonata.piano.service.registration.support;

import com.odeyalo.sonata.piano.exception.BirthdatePolicyViolationException;
import com.odeyalo.sonata.piano.service.BirthdatePolicy;
import com.odeyalo.sonata.piano.service.RegistrationForm;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public final class BirthdatePolicyRegistrationFormValidationStep implements RegistrationFormValidationStep {
    private final BirthdatePolicy birthdatePolicy;

    public BirthdatePolicyRegistrationFormValidationStep(final BirthdatePolicy birthdatePolicy) {
        this.birthdatePolicy = birthdatePolicy;
    }

    @Override
    public @NotNull Mono<Void> validate(@NotNull final RegistrationForm form) {
        return birthdatePolicy.isAllowed(form.birthdate())
                .flatMap(BirthdatePolicyRegistrationFormValidationStep::successOrError);
    }

    @NotNull
    private static Mono<Void> successOrError(final boolean isAllowed) {
        if ( isAllowed ) {
            return SUCCESS;
        }

        return birthdateViolationError();
    }

    @NotNull
    private static <T> Mono<T> birthdateViolationError() {
        return Mono.defer(() -> Mono.error(
                new BirthdatePolicyViolationException()
        ));
    }
}
