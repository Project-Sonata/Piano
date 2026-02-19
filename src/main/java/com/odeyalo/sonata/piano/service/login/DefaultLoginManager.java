package com.odeyalo.sonata.piano.service.login;

import com.odeyalo.sonata.piano.api.dto.response.TokensDto;
import com.odeyalo.sonata.piano.model.Email;
import com.odeyalo.sonata.piano.model.LoginCredentials;
import com.odeyalo.sonata.piano.service.UserService;
import com.odeyalo.sonata.piano.service.support.PasswordEncoder;
import com.odeyalo.sonata.piano.support.jwt.JwtTokenGenerator;
import com.odeyalo.sonata.piano.support.jwt.JwtTokenManager;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class DefaultLoginManager implements LoginManager {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenManager jwtTokenManager;

    public DefaultLoginManager(final UserService userService,
                               final PasswordEncoder passwordEncoder,
                               final JwtTokenManager jwtTokenManager) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenManager = jwtTokenManager;
    }

    @Override
    @NotNull
    public Mono<TokensDto> login(@NotNull final LoginCredentials credentials) {
        return userService.findByEmail(credentials.email())
                .filter(user -> passwordEncoder.matches(credentials.password(), user.password()))
                .flatMap(user -> {
                    final JwtTokenGenerator.GenerationOptions options = JwtTokenGenerator.GenerationOptions.builder()
                            .additionalClaim("user_id", user.id().value())
                            .build();

                    return jwtTokenManager.generateJwt(options)
                            .map(jwt -> new TokensDto(jwt.tokenValue()));
                });
    }
}
