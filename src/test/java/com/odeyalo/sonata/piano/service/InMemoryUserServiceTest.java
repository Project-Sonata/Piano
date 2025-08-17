package com.odeyalo.sonata.piano.service;

import com.odeyalo.sonata.piano.model.Email;
import com.odeyalo.sonata.piano.model.User;
import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;
import testing.UserFaker;

import java.util.List;

class InMemoryUserServiceTest {

    @Test
    void shouldReturnEmptyMonoIfUserByEmailDoesNotExist() {
        final InMemoryUserService testable = new InMemoryUserService(List.of(
                UserFaker.create().withEmail("odeyalo@gmail.com").get(),
                UserFaker.create().withEmail("miku@gmail.com").get()
        ));

        testable.findByEmail(Email.valueOf("notexist@gmail.com"))
                .as(StepVerifier::create)
                .verifyComplete();
    }

    @Test
    void shouldReturnUserIfUserByEmailExist() {
        final User user = UserFaker.create().withEmail("odeyalo@gmail.com").get();

        final InMemoryUserService testable = new InMemoryUserService(List.of(
                user,
                UserFaker.create().withEmail("miku@gmail.com").get()
        ));

        testable.findByEmail(Email.valueOf("odeyalo@gmail.com"))
                .as(StepVerifier::create)
                .expectNext(user)
                .verifyComplete();
    }
}