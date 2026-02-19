package com.odeyalo.sonata.piano.model;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

/**
 * A key-value read-only structure that used to store token's claims
 */
public interface Claims {

    @Nullable
    Object get(@NotNull String claim);

    @Nullable
    <T> T get(@NotNull String claim, @NotNull Class<T> requiredType);

    @NotNull
    Map<String, Object> asMap();
}
