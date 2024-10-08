package com.odeyalo.sonata.piano.model.factory;

import com.odeyalo.sonata.piano.model.User;
import com.odeyalo.sonata.piano.service.registration.email.RegistrationForm;
import org.jetbrains.annotations.NotNull;

/**
 * A factory interface to create {@link User} instances
 */
public interface UserFactory {
    /**
     * Create user based on the registration form
     * @param form registration form provided by user
     * @return created {@link User}
     */
    @NotNull
    User createUser(@NotNull RegistrationForm form);

    /**
     * Create unactivated user from the given registration form
     * @param form registration form provided by user
     * @return created {@link User}
     */
    @NotNull
    User createUnactivatedUser(@NotNull RegistrationForm form);
}
