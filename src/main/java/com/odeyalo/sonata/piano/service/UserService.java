package com.odeyalo.sonata.piano.service;

import com.odeyalo.sonata.piano.model.Email;
import com.odeyalo.sonata.piano.model.User;
import com.odeyalo.sonata.piano.model.UserId;
import org.jetbrains.annotations.NotNull;
import reactor.core.publisher.Mono;

public interface UserService {

    @NotNull
    Mono<User> findById(@NotNull UserId id);


    @NotNull
    Mono<User> findByEmail(@NotNull Email email);



}
