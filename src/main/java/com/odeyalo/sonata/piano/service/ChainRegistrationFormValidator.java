package com.odeyalo.sonata.piano.service;

import com.odeyalo.sonata.piano.service.registration.support.RegistrationFormValidationStep;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public final class ChainRegistrationFormValidator implements RegistrationFormValidator {
    private final List<RegistrationFormValidationStep> validationSteps;

    public ChainRegistrationFormValidator(final List<RegistrationFormValidationStep> validationSteps) {
        Assert.notEmpty(validationSteps, "validationSteps must contain at least one validation!");
        this.validationSteps = validationSteps;
    }

    @Override
    @NotNull
    public Mono<Void> validate(@NotNull final RegistrationForm form) {
        return Flux.fromIterable(validationSteps)
                .flatMap(step -> step.validate(form))
                .then();
    }
}
