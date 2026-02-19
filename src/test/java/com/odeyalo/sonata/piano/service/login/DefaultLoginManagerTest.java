package com.odeyalo.sonata.piano.service.login;

import com.odeyalo.sonata.piano.model.Email;
import com.odeyalo.sonata.piano.model.LoginCredentials;
import com.odeyalo.sonata.piano.model.User;
import com.odeyalo.sonata.piano.service.InMemoryUserService;
import com.odeyalo.sonata.piano.service.support.PasswordEncoder;
import com.odeyalo.sonata.piano.service.support.TestingPasswordEncoder;
import com.odeyalo.sonata.piano.support.jwt.SecretKeyJwtTokenManager;
import com.odeyalo.sonata.piano.support.jwt.StaticJwtTokenSecretKeySupplier;
import io.jsonwebtoken.Jwts;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;
import testing.UserFaker;

import javax.crypto.SecretKey;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class DefaultLoginManagerTest {

    @Test
    void shouldReturnTokensIfCredentialsAreValid() {
        final PasswordEncoder passwordEncoder = new TestingPasswordEncoder();
        final String encodedPassword = passwordEncoder.encode("password123");

        final DefaultLoginManager testable = TestableBuilder.builder()
                .withPasswordEncoder(passwordEncoder)
                .withUser(UserFaker.create()
                        .withEmail("test@example.com")
                        .withPassword(encodedPassword)
                        .get())
                .build();

        final LoginCredentials credentials = LoginCredentials.of(Email.valueOf("test@example.com"), "password123");

        testable.login(credentials)
                .as(StepVerifier::create)
                .assertNext(tokens -> assertThat(tokens.accessToken()).isNotEmpty())
                .verifyComplete();
    }

    @Test
    void shouldReturnEmptyIfPasswordIsIncorrect() {
        final PasswordEncoder passwordEncoder = new TestingPasswordEncoder();

        final DefaultLoginManager testable = TestableBuilder.builder()
                .withPasswordEncoder(passwordEncoder)
                .withUser(UserFaker.create()
                        .withEmail("test@example.com")
                        .withPassword(passwordEncoder.encode("correct_password"))
                        .get())
                .build();

        final LoginCredentials credentials = LoginCredentials.of(Email.valueOf("test@example.com"), "wrong_password");

        testable.login(credentials)
                .as(StepVerifier::create)
                .verifyComplete();
    }

    @Test
    void shouldReturnEmptyIfUserDoesNotExist() {
        final DefaultLoginManager testable = TestableBuilder.builder().build();

        final LoginCredentials credentials = LoginCredentials.of(Email.valueOf("nonexistent@example.com"), "any_password");

        testable.login(credentials)
                .as(StepVerifier::create)
                .verifyComplete();
    }

    private static class TestableBuilder {
        private PasswordEncoder passwordEncoder = new TestingPasswordEncoder();
        private final List<User> users = new ArrayList<>();
        private static final SecretKey SECRET_KEY = Jwts.SIG.HS256.key().build();

        public static TestableBuilder builder() {
            return new TestableBuilder();
        }

        public TestableBuilder withPasswordEncoder(final PasswordEncoder passwordEncoder) {
            this.passwordEncoder = passwordEncoder;
            return this;
        }

        public TestableBuilder withUser(final User user) {
            this.users.add(user);
            return this;
        }

        public DefaultLoginManager build() {
            return new DefaultLoginManager(
                    new InMemoryUserService(users),
                    passwordEncoder,
                    new SecretKeyJwtTokenManager(new StaticJwtTokenSecretKeySupplier(SECRET_KEY))
            );
        }
    }
}
