package com.odeyalo.sonata.piano.exception;

import com.odeyalo.sonata.piano.api.dto.ExceptionMessage;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public final class GlobalExceptionHandlerController {

    @ExceptionHandler(EmailRegexException.class)
    public ResponseEntity<ExceptionMessage> emailRegexException(@NotNull final EmailRegexException ex) {
        final ExceptionMessage exceptionMessage = ExceptionMessage.of(
                "Email has invalid format"
        );

        return ResponseEntity
                .badRequest()
                .body(exceptionMessage);
    }

    @ExceptionHandler(PasswordRegexException.class)
    public ResponseEntity<ExceptionMessage> handlePasswordRegexException(@NotNull final PasswordRegexException ex) {
        final ExceptionMessage exceptionMessage = ExceptionMessage.of(
                "Password must contain at least 8 characters and at least 1 number"
        );

        return ResponseEntity
                .badRequest()
                .body(exceptionMessage);
    }

    @ExceptionHandler(BirthdatePolicyViolationException.class)
    public ResponseEntity<ExceptionMessage> handleBirthdatePolicyViolationException(@NotNull final BirthdatePolicyViolationException ex) {
        final ExceptionMessage exceptionMessage = ExceptionMessage.of("User must be older than 13 years");

        return ResponseEntity
                .badRequest()
                .body(exceptionMessage);
    }
}
