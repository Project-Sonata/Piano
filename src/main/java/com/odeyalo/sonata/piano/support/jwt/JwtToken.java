package com.odeyalo.sonata.piano.support.jwt;

import io.jsonwebtoken.lang.Assert;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;

/**
 * Contains jwt token value and basic metadata about it(like lifetime and expires_in)
 */
@Value
@AllArgsConstructor(staticName = "of")
@Builder
public class JwtToken {
    @NotNull
    String tokenValue;
    @NotNull
    Lifetime lifetime;
    @NotNull
    Map<String, Object> claims;

    @NotNull
    public static JwtToken.JwtTokenBuilder withTokenValue(@NotNull final String value) {
        return builder().tokenValue(value);
    }

    @NotNull
    private static JwtToken.JwtTokenBuilder builder() {
        return new JwtTokenBuilder();
    }


    public record Lifetime(@NotNull Instant issuedAt,
                           @NotNull Instant expiresAt) {
        public Lifetime {
            Assert.notNull(issuedAt, "issuedAt cannot be null");
            Assert.notNull(expiresAt, "expiresAt cannot be null");
            Assert.state(issuedAt.isBefore(expiresAt), "expiresAt must be after issuedAt");
        }

        @NotNull
        public static Lifetime lasting(@NotNull final Duration duration) {
            final Instant now = Instant.now();
            return new Lifetime(
                    now,
                    now.plus(duration)
            );
        }

        @NotNull
        public Duration remaining() {
            return Duration.between(Instant.now(), expiresAt);
        }

        @NotNull
        public Duration duration() {
            return Duration.between(issuedAt, expiresAt);
        }
    }
}
