package com.odeyalo.sonata.piano.support.web;

import org.jetbrains.annotations.NotNull;
import org.springframework.http.ResponseEntity;

public final class HttpStatuses {

    @NotNull
    public static <T> ResponseEntity<T> ok(@NotNull T body) {
        return ResponseEntity.ok(body);
    }

    @NotNull
    public static <T> ResponseEntity<T> badRequest() {
        return ResponseEntity.badRequest().build();
    }
}
