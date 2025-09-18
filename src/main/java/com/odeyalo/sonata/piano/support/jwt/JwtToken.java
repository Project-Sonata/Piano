package com.odeyalo.sonata.piano.support.jwt;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import org.jetbrains.annotations.NotNull;

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
}
