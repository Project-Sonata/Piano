package com.odeyalo.sonata.piano.service.registration.policy;

import com.odeyalo.sonata.piano.model.Birthdate;
import org.jetbrains.annotations.NotNull;
import reactor.core.publisher.Mono;

/**
 * Represent a birthdate policy for Sonata
 */
public interface BirthdatePolicy {
    /**
     * Check if the given birthdate is allowed to be registered
     * @param birthdate birthdate of the user
     * @return a {@link Mono} with {@link Boolean#TRUE} if birthdate is allowed,
     * or {@link }
     */
    @NotNull
    Mono<Boolean> isAllowed(@NotNull Birthdate birthdate);

}
