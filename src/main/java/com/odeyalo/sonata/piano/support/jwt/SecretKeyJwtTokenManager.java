package com.odeyalo.sonata.piano.support.jwt;

import com.odeyalo.sonata.piano.support.jwt.JwtToken.Lifetime;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.odeyalo.sonata.piano.support.jwt.JwtToken.withTokenValue;
import static com.odeyalo.sonata.piano.support.jwt.JwtTokenGenerator.GenerationOptions.DefaultClaimsOverridePolicy.DO_NOT_OVERRIDE;

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

        final Map<String, Object> claims = normalizeClaims(options);

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

    @NotNull
    private static Map<String, Object> normalizeClaims(@NotNull final GenerationOptions options) {
        if ( options.defaultClaimsOverridePolicy() != DO_NOT_OVERRIDE ) {
            return options.additionalClaims();
        }
        return removeDefaultClaimsFromAdditional(options);
    }

    @Override
    @NotNull
    public Mono<ParsedJwtTokenMetadata> parseToken(@NotNull final String jwtTokenValue) {
        return Mono.fromCallable(() -> {
            JwtParser parser = Jwts.parser().verifyWith(secretKeySupplier.get()).build();
            Claims claims = parser.parseSignedClaims(jwtTokenValue).getPayload();

            Instant remainingLifetime = calculateRemainingLifetime(claims);
            return ParsedJwtTokenMetadata.of(claims, Duration.ofMinutes(remainingLifetime.getEpochSecond()));
        });
    }

    @NotNull
    private static Map<String, Object> removeDefaultClaimsFromAdditional(@NotNull final GenerationOptions options) {
        final HashMap<String, Object> newClaims = new HashMap<>(options.additionalClaims());
        DEFAULT_CLAIMS.forEach(newClaims.keySet()::remove);
        return newClaims;
    }

    private static Instant calculateRemainingLifetime(Claims claims) {
        Date expiration = claims.getExpiration();
        return expiration.toInstant().minusSeconds(LocalDateTime.now().getSecond());
    }
}
