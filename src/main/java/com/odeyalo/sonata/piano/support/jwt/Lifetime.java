package com.odeyalo.sonata.piano.support.jwt;

import io.jsonwebtoken.lang.Assert;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;

public record Lifetime(@NotNull Instant issuedAt,
                       @NotNull Instant expiresAt) {

    public Lifetime {
        Assert.notNull(issuedAt, "issuedAt cannot be null");
        Assert.notNull(expiresAt, "expiresAt cannot be null");
        Assert.state(issuedAt.isBefore(expiresAt), "expiresAt must be after issuedAt");
    }

    public Lifetime(@NotNull final Date issuedAt,
                    @NotNull final Date expiresAt) {
        this(issuedAt.toInstant(), expiresAt.toInstant());
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
