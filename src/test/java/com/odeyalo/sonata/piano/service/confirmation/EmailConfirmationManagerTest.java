package com.odeyalo.sonata.piano.service.confirmation;

import com.odeyalo.sonata.piano.exception.InvalidConfirmationCodeException;
import com.odeyalo.sonata.piano.model.User;
import com.odeyalo.sonata.piano.service.InMemoryUserService;
import com.odeyalo.sonata.piano.service.UserService;
import com.odeyalo.sonata.piano.service.confirmation.callback.UserEmailConfirmationCallback;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import testing.UserFaker;

import java.util.Collections;
import java.util.List;

import static com.odeyalo.sonata.piano.model.ConfirmationStatus.DENIED;
import static com.odeyalo.sonata.piano.model.ConfirmationStatus.OK;
import static org.assertj.core.api.Assertions.assertThat;

class EmailConfirmationManagerTest {
    static final User USER = UserFaker.create()
            .withActivated(false)
            .get();

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class ValidCode {

        static final String VALID_CODE_VALUE = "111111";
        static final ConfirmationCodeLoader STATIC_LOADER = code -> Mono.just(new ConfirmationCode(VALID_CODE_VALUE, USER));


        @Test
        void shouldReturnOkStatus() {
            final UserService userService = new InMemoryUserService(List.of(USER));
            final EmailConfirmationManager testable = new EmailConfirmationManager(
                    STATIC_LOADER,
                    userService
            );

            testable.confirmEmail(VALID_CODE_VALUE)
                    .as(StepVerifier::create)
                    .assertNext(status -> assertThat(status).isEqualTo(OK))
                    .verifyComplete();
        }

        @Test
        void shouldActiveUserAssociatedWithConfirmationCode() {
            final UserService userService = new InMemoryUserService(List.of(USER));
            final EmailConfirmationManager testable = new EmailConfirmationManager(
                    STATIC_LOADER,
                    userService
            );

            testable.confirmEmail(VALID_CODE_VALUE).block();

            userService.findById(USER.id())
                    .as(StepVerifier::create)
                    .assertNext(user -> assertThat(user.activated()).isTrue())
                    .verifyComplete();
        }

        @Test
        void shouldTriggerSuccessCallback() {
            final UserService userService = new InMemoryUserService(List.of(USER));
            var callback = new SpyCallback();

            final EmailConfirmationManager testable = new EmailConfirmationManager(
                    STATIC_LOADER,
                    userService,
                    List.of(callback)
            );

            testable.confirmEmail(VALID_CODE_VALUE).block();

            userService.findById(USER.id())
                    .as(StepVerifier::create)
                    .expectNextCount(1)
                    .verifyComplete();

            assertThat(callback.isSuccessTriggered()).isTrue();

        }
    }

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class InvalidCode {


        @Test
        void shouldNotConfirmEmailWhenIncorrectConfirmationCodeIsUsed() {
            final EmailConfirmationManager testable = new EmailConfirmationManager(
                    code -> Mono.empty(),
                    new InMemoryUserService(Collections.emptyList())
            );

            testable.confirmEmail("000000")
                    .as(StepVerifier::create)
                    .assertNext(status -> assertThat(status).isEqualTo(DENIED))
                    .verifyComplete();
        }

        @Test
        void shouldNotConfirmEmailWhenExpiredConfirmationCodeIsUsed() {
            final EmailConfirmationManager testable = new EmailConfirmationManager(
                    code -> Mono.error(new InvalidConfirmationCodeException("Invalid confirmation code")),
                    new InMemoryUserService(Collections.emptyList())

            );

            testable.confirmEmail("000000")
                    .as(StepVerifier::create)
                    .assertNext(status -> assertThat(status).isEqualTo(DENIED))
                    .verifyComplete();
        }
    }

    private static class SpyCallback implements UserEmailConfirmationCallback {
        private boolean successTriggered = false;

        @Override
        @NotNull
        public Mono<Void> onSuccess(@NotNull final User user,
                                             @NotNull final ConfirmationCode code) {
            successTriggered = true;
            return UserEmailConfirmationCallback.super.onSuccess(user, code);
        }

        public boolean isSuccessTriggered() {
            return successTriggered;
        }
    }
}