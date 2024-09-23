package com.odeyalo.sonata.piano.model.factory;

import com.odeyalo.sonata.piano.model.User;
import com.odeyalo.sonata.piano.model.UserId;
import com.odeyalo.sonata.piano.service.registration.email.RegistrationForm;
import com.odeyalo.sonata.piano.service.support.PasswordEncoder;
import org.jetbrains.annotations.NotNull;

public final class DefaultUserFactory implements UserFactory {
    private final PasswordEncoder passwordEncoder;

    public DefaultUserFactory(final PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @NotNull
    public User createUser(@NotNull final RegistrationForm form) {

        return new User(
                UserId.random(),
                form.email(),
                passwordEncoder.encode(form.password()),
                form.gender(),
                true,
                false,
                form.birthdate()
        );
    }

    @Override
    @NotNull
    public User createUnactivatedUser(@NotNull final RegistrationForm form) {

        return new User(
                UserId.random(),
                form.email(),
                passwordEncoder.encode(form.password()),
                form.gender(),
                false,
                false,
                form.birthdate()
        );
    }
}
