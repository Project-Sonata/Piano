package com.odeyalo.sonata.piano.repository;


import com.odeyalo.sonata.piano.entity.UserEntity;
import com.odeyalo.sonata.piano.model.User;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import reactor.test.StepVerifier;
import testing.base.AbstractIntegrationTest;
import testing.faker.UserEntityFaker;

import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

@DataR2dbcTest
class UserRepositoryTest extends AbstractIntegrationTest {

    @Autowired
    UserRepository testable;

    @AfterEach
    void tearDown() {
        testable.deleteAll().block();
    }

    @Test
    void shouldFindAllSavedUsers() {
        // given
        final UserEntity user1 = UserEntityFaker.newUser().get();
        final UserEntity user2 = UserEntityFaker.newUser().get();

        final List<UserEntity> savedUsers = insertUsers(user1, user2);

        // when
        final List<UserEntity> foundUsers = testable.findAll().collectList().block();

        // then
        assertThat(foundUsers).containsAll(savedUsers);
    }

    @Test
    void shouldFindByExternalId() {
        final UserEntity user1 = UserEntityFaker.newUser()
                .withExternalId("helloworld").get();
        final UserEntity user2 = UserEntityFaker.newUser().get();

        insertUsers(user1, user2);

        final UserEntity foundUser = testable.findByExternalId("helloworld").block();

        assertThat(foundUser)
                .usingRecursiveComparison().ignoringFields("id")
                .isEqualTo(user1);
    }

    @NotNull
    private List<UserEntity> insertUsers(@NotNull final UserEntity... users) {
        return Objects.requireNonNull(
                testable.saveAll(List.of(users)).collectList().block(),
                () -> "Error occurred during saving to database"
        );
    }
}