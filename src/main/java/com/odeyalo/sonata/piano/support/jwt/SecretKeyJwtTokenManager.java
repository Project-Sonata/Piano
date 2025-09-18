package com.odeyalo.sonata.piano.support.jwt;

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
        final Instant generationTime = Instant.now();

        final Date issuedAt = Date.from(generationTime);
        final Date expiresIn = Date.from(generationTime.plusSeconds(options.lifetime().toSeconds()));
        final Map<String, Object> additionalClaims = options.additionalClaims();

        final JwtBuilder jwtBuilder = Jwts.builder()
                .id(UUID.randomUUID().toString())
                .issuedAt(issuedAt)
                .expiration(expiresIn)
                .signWith(secretKeySupplier.get())
                .claims(additionalClaims);

        if ( options.defaultClaimsOverridePolicy() == DO_NOT_OVERRIDE ) {
            final Map<String, Object> claims = removeDefaultClaimsFromAdditional(options);

            jwtBuilder.claims(claims);
        }

        final JwtToken jwtToken = convertToJwtToken(options, expiresIn, jwtBuilder);

        return Mono.just(jwtToken);
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

    private static JwtToken convertToJwtToken(@NotNull GenerationOptions options, Date expiresIn, JwtBuilder jwtBuilder) {
        return withTokenValue(jwtBuilder.compact())
                .lifetime(options.lifetime())
                .expiresIn(expiresIn.toInstant().getEpochSecond())
                .build();
    }

    private static Map<String, Object> removeDefaultClaimsFromAdditional(@NotNull GenerationOptions options) {
        HashMap<String, Object> newClaims = new HashMap<>(options.additionalClaims());
        DEFAULT_CLAIMS.forEach(newClaims.keySet()::remove);
        return newClaims;
    }

    private static Instant calculateRemainingLifetime(Claims claims) {
        Date expiration = claims.getExpiration();
        return expiration.toInstant().minusSeconds(LocalDateTime.now().getSecond());
    }
}
