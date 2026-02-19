package com.odeyalo.sonata.piano.service.login;

import com.odeyalo.sonata.piano.api.dto.response.TokensDto;
import org.jetbrains.annotations.NotNull;
import reactor.core.publisher.Mono;

/**
 * Handle user login process
 */
public interface LoginManager {

    /**
     * Login user with email and password
     * @param email user email
     * @param password user raw password
     * @return Mono with tokens if success, or empty if failed
     */
    @NotNull
    Mono<TokensDto> login(@NotNull String email, @NotNull String password);

}
