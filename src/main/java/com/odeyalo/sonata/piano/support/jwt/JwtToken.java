package com.odeyalo.sonata.piano.support.jwt;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;

/**
 * Contains jwt token value and basic metadata about it(like lifetime and expires_in)
 */
@Value
@AllArgsConstructor(staticName = "of")
@Builder
public class JwtToken {
    @NotNull
    String tokenValue;
    long expiresIn;
    @NotNull
    Duration lifetime;

    private static JwtToken.JwtTokenBuilder builder() {
        return new JwtTokenBuilder();
    }

    public static JwtToken.JwtTokenBuilder withTokenValue(@NotNull final String value) {
        return builder().tokenValue(value);
    }
}
