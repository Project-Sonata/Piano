package com.odeyalo.sonata.piano.repository;

import com.odeyalo.sonata.piano.entity.UserEntity;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

public interface UserRepository extends R2dbcRepository<UserEntity, Integer> {
}
