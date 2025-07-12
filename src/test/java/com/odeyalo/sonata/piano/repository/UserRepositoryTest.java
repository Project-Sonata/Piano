package com.odeyalo.sonata.piano.repository;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import reactor.test.StepVerifier;
import testing.base.AbstractIntegrationTest;

@DataR2dbcTest
class UserRepositoryTest extends AbstractIntegrationTest {

    @Autowired
    UserRepository testable;

    @Test
    void shouldNotFail() {
        testable.findAll()
                .as(StepVerifier::create)
                .expectNextCount(0)
                .verifyComplete();
    }
}