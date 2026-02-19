package com.odeyalo.sonata.piano.service.token;

import com.odeyalo.sonata.piano.model.AuthClaims;
import com.odeyalo.sonata.piano.model.User;
import com.odeyalo.sonata.piano.support.jwt.JwtTokenGenerator;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public final class JwtTokensGenerator implements TokensGenerator {
    private final JwtTokenGenerator jwtTokenGenerator;

    public JwtTokensGenerator(final JwtTokenGenerator jwtTokenGenerator) {
        this.jwtTokenGenerator = jwtTokenGenerator;
    }

    @Override
    @NotNull
    public Mono<Tokens> generateTokensFor(@NotNull final User user) {
        final AuthClaims claims = AuthClaims.createFor(user);

        final JwtTokenGenerator.GenerationOptions options = JwtTokenGenerator.GenerationOptions.builder()
                .additionalClaims(claims.asMap())
                .build();

        return jwtTokenGenerator.generateJwt(options)
                .map(accessToken -> new Tokens(accessToken.tokenValue()));
    }
}
