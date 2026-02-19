package com.odeyalo.sonata.piano.service.login;

import com.odeyalo.sonata.piano.api.dto.response.TokensDto;
import com.odeyalo.sonata.piano.repository.UserRepository;
import com.odeyalo.sonata.piano.service.support.PasswordEncoder;
import com.odeyalo.sonata.piano.support.jwt.JwtTokenGenerator;
import com.odeyalo.sonata.piano.support.jwt.JwtTokenManager;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class DefaultLoginManager implements LoginManager {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenManager jwtTokenManager;

    public DefaultLoginManager(UserRepository userRepository,
                               PasswordEncoder passwordEncoder,
                               JwtTokenManager jwtTokenManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenManager = jwtTokenManager;
    }

    @Override
    @NotNull
    public Mono<TokensDto> login(@NotNull String email, @NotNull String password) {
        return userRepository.findByEmail(email)
                .filter(user -> passwordEncoder.matches(password, user.password()))
                .flatMap(user -> {
                    JwtTokenGenerator.GenerationOptions options = JwtTokenGenerator.GenerationOptions.builder()
                            .additionalClaim("user_id", user.externalId())
                            .build();

                    return jwtTokenManager.generateJwt(options)
                            .map(jwt -> new TokensDto(jwt.tokenValue()));
                });
    }
}
