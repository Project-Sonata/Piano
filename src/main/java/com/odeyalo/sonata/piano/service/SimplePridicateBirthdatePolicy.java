package com.odeyalo.sonata.piano.service;

import com.odeyalo.sonata.piano.model.Birthdate;
import org.jetbrains.annotations.NotNull;
import reactor.core.publisher.Mono;

import java.util.function.Predicate;

/**
 * Primary used for tests
 */
public final class SimplePridicateBirthdatePolicy implements BirthdatePolicy {
    private final Predicate<Birthdate> delegate;

    public SimplePridicateBirthdatePolicy(final Predicate<Birthdate> predicate) {
        this.delegate = predicate;
    }

    @Override
    @NotNull
    public Mono<Boolean> isAllowed(@NotNull final Birthdate birthdate) {
        return Mono.fromCallable(() -> delegate.test(birthdate));
    }
}
