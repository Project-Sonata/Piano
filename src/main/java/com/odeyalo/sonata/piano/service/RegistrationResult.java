package com.odeyalo.sonata.piano.service;

import com.odeyalo.sonata.piano.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import org.jetbrains.annotations.NotNull;

/**
 * A result of user registration using email/password schema.
 */
@Value
@Builder
@AllArgsConstructor(staticName = "of")
public class RegistrationResult {
    @NotNull
    NextAction nextStep;
    @NotNull
    User registeredUser;

    @NotNull
    public static RegistrationResult confirmEmailFor(@NotNull final User user) {
        return of(NextAction.CONFIRM_EMAIL, user);
    }

    @NotNull
    public static RegistrationResult completedFor(@NotNull final User user) {
        return of(NextAction.COMPLETED, user);
    }

    public enum NextAction {
        CONFIRM_EMAIL,
        COMPLETED
    }
}
