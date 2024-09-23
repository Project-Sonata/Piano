package com.odeyalo.sonata.piano.support.web;

import com.odeyalo.sonata.piano.api.dto.EmailConfirmationRequiredResponseDto;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.ResponseEntity;

public final class HttpStatuses {

    @NotNull
    public static <T> ResponseEntity<T> ok(@NotNull T body) {
        return ResponseEntity.ok(body);
    }
}
