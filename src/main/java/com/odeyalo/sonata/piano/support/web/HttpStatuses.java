package com.odeyalo.sonata.piano.support.web;

import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public final class HttpStatuses {

    @NotNull
    public static ResponseEntity<?> ok() {
        return ResponseEntity.ok().build();
    }

    @NotNull
    public static <T> ResponseEntity<T> ok(@NotNull T body) {
        return ResponseEntity.ok(body);
    }

    @NotNull
    public static <T> ResponseEntity<T> badRequest() {
        return ResponseEntity.badRequest().build();
    }

    @NotNull
    public static <T> ResponseEntity<T> unauthorized() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}
