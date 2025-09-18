package com.odeyalo.sonata.piano.service.token;

import com.odeyalo.sonata.piano.model.User;
import org.jetbrains.annotations.NotNull;
import reactor.core.publisher.Mono;

public interface TokensGenerator {

    @NotNull
    Mono<Tokens> generateTokensFor(@NotNull User user);

}
