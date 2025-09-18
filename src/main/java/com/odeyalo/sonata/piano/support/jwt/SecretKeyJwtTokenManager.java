package com.odeyalo.sonata.piano.support.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Date;
import java.util.Map;
import java.util.UUID;

/**
 * Generate JWT token and sign it with SecretKey
 */
@Component
public class SecretKeyJwtTokenManager implements JwtTokenManager {
    private final JwtTokenSecretKeySupplier secretKeySupplier;

    public SecretKeyJwtTokenManager(JwtTokenSecretKeySupplier secretKeySupplier) {
        this.secretKeySupplier = secretKeySupplier;
    }

    @Override
    @NotNull
    public Mono<JwtToken> generateJwt(@NotNull final GenerationOptions options) {
        final Lifetime lifetime = Lifetime.lasting(options.lifetime());

        final Map<String, Object> claims = options.getNormalizedClaims();

        final JwtBuilder jwtBuilder = Jwts.builder()
                .id(UUID.randomUUID().toString())
                .issuedAt(Date.from(lifetime.issuedAt()))
                .expiration(Date.from(lifetime.expiresAt()))
                .signWith(secretKeySupplier.get())
                .claims(claims);

        final JwtToken jwtToken = JwtToken.withTokenValue(jwtBuilder.compact())
                .lifetime(lifetime)
                .claims(claims)
                .build();

        return Mono.just(jwtToken);
    }

    @Override
    @NotNull
    public Mono<ParsedJwtTokenMetadata> parseToken(@NotNull final String jwtTokenValue) {
        return Mono.fromCallable(() -> {
            final JwtParser parser = Jwts.parser()
                    .verifyWith(secretKeySupplier.get())
                    .build();
            final Claims claims = parser.parseSignedClaims(jwtTokenValue).getPayload();

            final Lifetime remainingLifetime = calculateRemainingLifetime(claims);
            return ParsedJwtTokenMetadata.of(claims, remainingLifetime);
        });
    }


    @NotNull
    private static Lifetime calculateRemainingLifetime(@NotNull final Claims claims) {
        return new Lifetime(
                claims.getIssuedAt(),
                claims.getExpiration()
        );
    }
}
