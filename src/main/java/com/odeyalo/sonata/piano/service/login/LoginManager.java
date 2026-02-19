package com.odeyalo.sonata.piano.service.login;

import com.odeyalo.sonata.common.authentication.exception.InvalidCredentialsException;
import com.odeyalo.sonata.piano.api.dto.response.TokensDto;
import com.odeyalo.sonata.piano.model.LoginCredentials;
import org.jetbrains.annotations.NotNull;
import reactor.core.publisher.Mono;

/**
 * Handle user login process
 */
public interface LoginManager {

    /**
     * Login user with credentials
     * @param credentials user credentials
     * @return Mono with tokens if success,
     * or {@link InvalidCredentialsException} error wrapped in {@link Mono} if supplied credentials are not valid
     */
    @NotNull
    Mono<TokensDto> login(@NotNull final LoginCredentials credentials);

}
