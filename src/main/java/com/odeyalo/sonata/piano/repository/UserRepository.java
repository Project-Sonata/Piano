package com.odeyalo.sonata.piano.repository;

import com.odeyalo.sonata.piano.entity.UserEntity;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Mono;

public interface UserRepository extends R2dbcRepository<UserEntity, Integer> {
    @NotNull
    Mono<UserEntity> findByExternalId(@NotNull String externalId);

    @NotNull
    Mono<UserEntity> findByEmail(@NotNull String email);
}
