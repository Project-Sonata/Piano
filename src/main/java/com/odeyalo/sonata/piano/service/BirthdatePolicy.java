package com.odeyalo.sonata.piano.service;

import com.odeyalo.sonata.piano.model.Birthdate;
import org.jetbrains.annotations.NotNull;
import reactor.core.publisher.Mono;

public interface BirthdatePolicy {

    @NotNull
    Mono<Boolean> isAllowed(@NotNull Birthdate birthdate);

}
