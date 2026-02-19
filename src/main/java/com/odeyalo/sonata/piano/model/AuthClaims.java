package com.odeyalo.sonata.piano.model;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public final class AuthClaims implements Claims {
    private final Map<String, Object> claims;

    private AuthClaims(final Map<String, Object> claims) {
        this.claims = claims;
    }

    @NotNull
    public static AuthClaims createFor(@NotNull final User user) {
        final Map<String, Object> claims = Map.of(
                "user_id", user.id().value()
        );

        return new AuthClaims(claims);
    }

    @Override
    @Nullable
    public Object get(@NotNull final String claim) {
        return claims.get(claim);
    }

    @Override
    @Nullable
    public <T> T get(@NotNull final String claim,
                     @NotNull final Class<T> requiredType) {
        final Object val = claims.get(claim);

        if ( val == null ) {
            return null;
        }

        return requiredType.cast(val);
    }

    @Override
    @NotNull
    public Map<String, Object> asMap() {
        return claims;
    }
}
