package com.odeyalo.sonata.piano.service.confirmation;

import com.odeyalo.sonata.piano.model.User;
import org.jetbrains.annotations.NotNull;

/**
 * Factory for creating {@link ConfirmationCode} instances.
 * <p>
 * This factory is responsible for generating confirmation codes for users,
 * typically used for email confirmation or similar verification processes.
 * </p>
 *
 * <p>
 * Implementations of this factory might include additional logic to ensure the uniqueness,
 * format, or expiration of confirmation codes.
 * </p>
 *
 * <p>
 * Example usage:
 * <pre>
 *     ConfirmationCodeFactory factory = new SimpleConfirmationCodeFactory();
 *     ConfirmationCode code = factory.newConfirmationCodeFor(user);
 * </pre>
 * </p>
 */
public interface ConfirmationCodeFactory {

    /**
     * Generates a new {@link ConfirmationCode} for the specified {@link User}.
     * @param user user to generate confirmation code for
     * @return generated {@link ConfirmationCode}
     */
    @NotNull
    ConfirmationCode newConfirmationCodeFor(@NotNull User user);

}
