package com.odeyalo.sonata.piano.service;

import com.odeyalo.sonata.piano.exception.EmailAddressAlreadyInUseException;
import com.odeyalo.sonata.piano.model.Email;
import com.odeyalo.sonata.piano.model.User;
import com.odeyalo.sonata.piano.model.UserId;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

public final class InMemoryUserService implements UserService {
    private final Map<UserId, User> users;
    private final Logger logger = LoggerFactory.getLogger(InMemoryUserService.class);

    public InMemoryUserService(final Map<UserId, User> users) {
        this.users = users;
    }

    public InMemoryUserService(final List<User> users) {
        this.users = users.stream()
                .collect(Collectors.toMap(User::id, Function.identity()));

        logger.info("Initialized the InMemoryUserService with {} users", users.size());
    }

    @Override
    @NotNull
    public Mono<User> save(@NotNull final User user) {

        final Mono<User> saveUser = Mono.fromCallable(() -> {
            users.put(user.id(), user);
            logger.info("Saved/updated user {}", user);
            return user;
        });

        return findById(user.id())
                .flatMap(u -> saveUser)
                .doOnNext(updatedUser -> logger.info("User {} has been updated", user.id()))
                .switchIfEmpty(findByEmail(user.email())
                        .flatMap(u -> Mono.error(new EmailAddressAlreadyInUseException()))
                        .switchIfEmpty(Mono.defer(() -> saveUser))
                        .cast(User.class));
    }

    @Override
    @NotNull
    public Mono<User> findById(@NotNull final UserId id) {
        return Mono.justOrEmpty(
                users.get(id)
        ).doOnNext(user -> logger.info("User with id {} was retrieved by its id", user.id()));
    }

    @Override
    @NotNull
    public Mono<User> findByEmail(@NotNull final Email email) {
        return Mono.justOrEmpty(
                users.values().stream()
                        .filter(u -> Objects.equals(u.email(), email))
                        .findFirst()
        ).doOnNext(user -> logger.info("User with id {} was retrieved by its email", user.id()));
    }
}
