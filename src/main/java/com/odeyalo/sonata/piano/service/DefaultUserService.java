package com.odeyalo.sonata.piano.service;

import com.odeyalo.sonata.piano.entity.UserEntity;
import com.odeyalo.sonata.piano.model.Birthdate;
import com.odeyalo.sonata.piano.model.Email;
import com.odeyalo.sonata.piano.model.User;
import com.odeyalo.sonata.piano.model.UserId;
import com.odeyalo.sonata.piano.repository.UserRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public final class DefaultUserService implements UserService {
    private final UserRepository userRepository;

    public DefaultUserService(final UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @NotNull
    public Mono<User> save(final @NotNull User user) {
        final UserEntity userEntity = UserEntity.builder()
                .id(user.internalId())
                .externalId(user.id().value())
                .email(user.email().asString())
                .password(user.password())
                .activated(user.isActivated())
                .emailConfirmed(user.isEmailConfirmed())
                .birthdate(user.birthdate().toLocalDate())
                .gender(user.gender())
                .build();
        return userRepository.save(userEntity)
                .map(this::toUser);
    }

    @Override
    @NotNull
    public Mono<User> findById(@NotNull final UserId id) {
        return userRepository.findByExternalId(id.value())
                .map(this::toUser);
    }

    @Override
    @NotNull
    public Mono<User> findByEmail(@NotNull final Email email) {
        return userRepository.findByEmail(email.value())
                .map(this::toUser);
    }

    @NotNull
    private User toUser(@NotNull final UserEntity entity) {
        return User.builder()
                .internalId(entity.id())
                .id(UserId.fromString(entity.externalId()))
                .email(Email.valueOf(entity.email()))
                .password(entity.password())
                .activated(entity.activated())
                .emailConfirmed(entity.emailConfirmed())
                .birthdate(Birthdate.of(entity.birthdate()))
                .gender(entity.gender())
                .build();
    }
}
