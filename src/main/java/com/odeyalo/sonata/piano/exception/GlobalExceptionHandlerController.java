package com.odeyalo.sonata.piano.exception;

import com.odeyalo.sonata.piano.api.dto.ExceptionMessage;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public final class GlobalExceptionHandlerController {

    @ExceptionHandler(PasswordRegexException.class)
    public ResponseEntity<ExceptionMessage> handlePasswordRegexException(@NotNull final PasswordRegexException ex) {
        final ExceptionMessage exceptionMessage = ExceptionMessage.of(
                "Password must contain at least 8 characters and at least 1 number"
        );

        return ResponseEntity
                .badRequest()
                .body(exceptionMessage);
    }
}
