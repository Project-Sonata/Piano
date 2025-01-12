package com.odeyalo.sonata.piano.service.confirmation;

import org.jetbrains.annotations.NotNull;
import reactor.core.publisher.Mono;

public interface EmailConfirmationCodeChecker {

    @NotNull
    Mono<Boolean> confirmCode(@NotNull String code);
}
