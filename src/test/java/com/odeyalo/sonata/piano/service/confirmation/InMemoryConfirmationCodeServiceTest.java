package com.odeyalo.sonata.piano.service.confirmation;

import com.odeyalo.sonata.piano.exception.InvalidConfirmationCodeException;
import com.odeyalo.sonata.piano.model.User;
import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;
import testing.UserFaker;

import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class InMemoryConfirmationCodeServiceTest {


    @Test
    void shouldReturnGeneratedConfirmationCode() {
        final User user = UserFaker.create().get();

        InMemoryConfirmationCodeService testable = new InMemoryConfirmationCodeService(new SimpleConfirmationCodeFactory());

        testable.newConfirmationCodeFor(user)
                .as(StepVerifier::create)
                .assertNext(res -> {
                    assertThat(res.generatedFor()).isEqualTo(user);
                    assertThat(res.value()).isNotBlank();
                })
                .verifyComplete();
    }

    @Test
    void shouldFindGeneratedConfirmationCodeThatWasGenerated() {
        final User user = UserFaker.create().get();

        InMemoryConfirmationCodeService testable = new InMemoryConfirmationCodeService(new SimpleConfirmationCodeFactory());

        ConfirmationCode code = testable.newConfirmationCodeFor(user).block();

        assertThat(code).isNotNull();

        testable.loadConfirmationCodeByValue(code.value())
                .as(StepVerifier::create)
                .assertNext(found -> assertThat(found).isEqualTo(code))
                .verifyComplete();
    }

    @Test
    void shouldReturnNothingIfCodeWithValueDoesNotExist() {
        InMemoryConfirmationCodeService testable = new InMemoryConfirmationCodeService(new SimpleConfirmationCodeFactory());

        testable.loadConfirmationCodeByValue("not_exist")
                .as(StepVerifier::create)
                .verifyComplete();
    }

    @Test
    void shouldReturnErrorIfConfirmationCodeIsExpired() {
        final User user = UserFaker.create().get();

        ConfirmationCode code = ConfirmationCode.builder()
                .value("123456")
                .issuedAt(Instant.now().minusSeconds(200))
                .expiresIn(Instant.now().minusSeconds(100))
                .generatedFor(user)
                .build();

        InMemoryConfirmationCodeService testable = new InMemoryConfirmationCodeService(
                List.of(code),
                new SimpleConfirmationCodeFactory()
        );

        testable.loadConfirmationCodeByValue("123456")
                .as(StepVerifier::create)
                .expectError(InvalidConfirmationCodeException.class)
                .verify();
    }
}