package com.odeyalo.sonata.piano.service.login;

import com.odeyalo.sonata.common.authentication.exception.InvalidCredentialsException;
import com.odeyalo.sonata.piano.model.LoginCredentials;
import com.odeyalo.sonata.piano.model.User;
import com.odeyalo.sonata.piano.service.UserService;
import com.odeyalo.sonata.piano.service.support.PasswordEncoder;
import com.odeyalo.sonata.piano.service.token.Tokens;
import com.odeyalo.sonata.piano.service.token.TokensGenerator;
import com.odeyalo.sonata.piano.support.ErrorDetailsFactory;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public final class DefaultLoginManager implements LoginManager {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final TokensGenerator tokensGenerator;

    public DefaultLoginManager(final UserService userService,
                               final PasswordEncoder passwordEncoder,
                               final TokensGenerator tokensGenerator) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.tokensGenerator = tokensGenerator;
    }

    @Override
    @NotNull
    public Mono<Tokens> login(@NotNull final LoginCredentials credentials) {
        return userService.findByEmail(credentials.email())
                .switchIfEmpty(Mono.error(
                        new InvalidCredentialsException(ErrorDetailsFactory.invalidCredentials())
                ))
                .flatMap(user -> validateCredentials(user, credentials))
                .flatMap(tokensGenerator::generateTokensFor);
    }

    @NotNull
    private Mono<User> validateCredentials(@NotNull final User user,
                                           @NotNull final LoginCredentials credentials) {

        if ( passwordEncoder.matches(credentials.password(), user.password()) ) {
            return Mono.just(user);
        }

        return Mono.error(new InvalidCredentialsException(
                ErrorDetailsFactory.invalidCredentials()
        ));
    }
}
