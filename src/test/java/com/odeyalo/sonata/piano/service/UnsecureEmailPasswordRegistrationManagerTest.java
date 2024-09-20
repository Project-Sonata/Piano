package com.odeyalo.sonata.piano.service;

import com.odeyalo.sonata.piano.exception.BirthdatePolicyViolationException;
import com.odeyalo.sonata.piano.exception.EmailAddressAlreadyInUseException;
import com.odeyalo.sonata.piano.model.Birthdate;
import com.odeyalo.sonata.piano.model.Gender;
import com.odeyalo.sonata.piano.model.User;
import com.odeyalo.sonata.piano.model.factory.DefaultUserFactory;
import com.odeyalo.sonata.piano.service.support.PasswordEncoder;
import com.odeyalo.sonata.piano.service.support.TestingPasswordEncoder;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;
import reactor.test.StepVerifier;
import testing.RegistrationFormFaker;
import testing.UserFaker;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import static com.odeyalo.sonata.piano.service.RegistrationResult.NextAction.COMPLETED;
import static com.odeyalo.sonata.piano.service.UnsecureEmailPasswordRegistrationManagerTest.BirthdatePolicies.alwaysDeny;
import static com.odeyalo.sonata.piano.service.UnsecureEmailPasswordRegistrationManagerTest.BirthdatePolicies.olderThan;
import static org.assertj.core.api.Assertions.assertThat;

class UnsecureEmailPasswordRegistrationManagerTest {

    @Test
    void shouldReturnCompletedStatusForValidRegistrationForm() {
        UnsecureEmailPasswordRegistrationManager testable = TestableBuilder.builder().build();

        RegistrationForm registrationForm = RegistrationFormFaker.create().get();

        testable.registerUser(registrationForm)
                .as(StepVerifier::create)
                .assertNext(result -> assertThat(result.nextStep()).isEqualTo(COMPLETED))
                .verifyComplete();
    }

    @Test
    void shouldReturnUserWithTheSameEmailAddressAsWasProvidedInRegistrationForm() {
        UnsecureEmailPasswordRegistrationManager testable = TestableBuilder.builder().build();

        RegistrationForm registrationForm = RegistrationFormFaker.create()
                .withEmail("miku.nakano@gmail.com")
                .get();

        testable.registerUser(registrationForm)
                .as(StepVerifier::create)
                .assertNext(result -> assertThat(result.registeredUser().email().asString()).isEqualTo("miku.nakano@gmail.com"))
                .verifyComplete();
    }

    @Test
    void shouldReturnActivatedUserAfterRegistrationComplete() {
        UnsecureEmailPasswordRegistrationManager testable = TestableBuilder.builder().build();

        RegistrationForm registrationForm = RegistrationFormFaker.create().get();

        testable.registerUser(registrationForm)
                .as(StepVerifier::create)
                .assertNext(result -> assertThat(result.registeredUser().isActivated()).isTrue())
                .verifyComplete();
    }

    @Test
    void shouldIndicateThatUserEmailIsNotConfirmedAfterRegistrationComplete() {
        UnsecureEmailPasswordRegistrationManager testable = TestableBuilder.builder().build();

        RegistrationForm registrationForm = RegistrationFormFaker.create().get();

        testable.registerUser(registrationForm)
                .as(StepVerifier::create)
                .assertNext(result -> assertThat(result.registeredUser().isEmailConfirmed()).isFalse())
                .verifyComplete();
    }

    @Test
    void shouldReturnUserWithEncodedPasswordThatMatchesProvidedOne() {
        TestingPasswordEncoder passwordEncoder = new TestingPasswordEncoder();

        UnsecureEmailPasswordRegistrationManager testable = TestableBuilder.builder()
                .withPasswordEncoder(passwordEncoder)
                .build();

        RegistrationForm registrationForm = RegistrationFormFaker.create()
                .withPassword("ilovemikunakan0")
                .get();

        RegistrationResult result = testable.registerUser(registrationForm).block();

        assertThat(result).isNotNull();

        boolean passwordMatches = passwordEncoder.matches("ilovemikunakan0", result.registeredUser().password());

        assertThat(passwordMatches).isTrue();
    }

    @ParameterizedTest
    @EnumSource(Gender.class)
    void shouldReturnUserWithTheSameGenderAsWasProvided(@NotNull final Gender gender) {
        UnsecureEmailPasswordRegistrationManager testable = TestableBuilder.builder().build();

        RegistrationForm registrationForm = RegistrationFormFaker.create()
                .withGender(gender)
                .get();

        testable.registerUser(registrationForm)
                .as(StepVerifier::create)
                .assertNext(result -> assertThat(result.registeredUser().gender()).isEqualTo(gender))
                .verifyComplete();

    }

    @Test
    void shouldReturnUserWithTheSameBirthdateAsWasProvided() {
        UnsecureEmailPasswordRegistrationManager testable = TestableBuilder.builder().build();

        LocalDate birthdate = LocalDate.of(2000, Month.MAY, 5);

        RegistrationForm registrationForm = RegistrationFormFaker.create()
                .withBirthdate(birthdate)
                .get();

        RegistrationResult result = testable.registerUser(registrationForm).block();

        assertThat(result).isNotNull();

        assertThat(result.registeredUser().birthdate()).isEqualTo(birthdate);
    }

    @Test
    void shouldGenerateUserWithGeneratedId() {
        UnsecureEmailPasswordRegistrationManager testable = TestableBuilder.builder().build();

        RegistrationForm registrationForm = RegistrationFormFaker.create().get();

        testable.registerUser(registrationForm)
                .as(StepVerifier::create)
                .assertNext(result -> assertThat(result.registeredUser().id()).isNotNull())
                .verifyComplete();
    }

    @Test
    void shouldGenerateUserWithContextUri() {
        UnsecureEmailPasswordRegistrationManager testable = TestableBuilder.builder().build();

        RegistrationForm registrationForm = RegistrationFormFaker.create().get();

        testable.registerUser(registrationForm)
                .as(StepVerifier::create)
                .assertNext(result -> assertThat(result.registeredUser().contextUri().asString()).isEqualTo("sonata:user:" + result.registeredUser().id().value()))
                .verifyComplete();
    }

    @Test
    void shouldReturnErrorIfUserWithTheSameEmailAlreadyExist() {
        UnsecureEmailPasswordRegistrationManager testable = TestableBuilder.builder()
                .withUsers("miku.nakano@gmail.com")
                .build();

        RegistrationForm registrationForm = RegistrationFormFaker.create()
                .withEmail("miku.nakano@gmail.com")
                .get();

        testable.registerUser(registrationForm)
                .as(StepVerifier::create)
                .expectError(EmailAddressAlreadyInUseException.class)
                .verify();
    }

    @Test
    void shouldReturnErrorIfBirthdatePolicyIsViolated() {
        UnsecureEmailPasswordRegistrationManager testable = TestableBuilder.builder()
                .withBirthdatePolicy(alwaysDeny())
                .build();

        RegistrationForm registrationForm = RegistrationFormFaker.create()
                .withBirthdate("2024-04-24")
                .get();

        testable.registerUser(registrationForm)
                .as(StepVerifier::create)
                .expectError(BirthdatePolicyViolationException.class)
                .verify();
    }

    @Test
    void shouldSaveRegisteredUser() {
        UnsecureEmailPasswordRegistrationManager testable = TestableBuilder.builder()
                .build();

        RegistrationForm registrationForm = RegistrationFormFaker.create()
                .withEmail("miku.nakano@gmail.com")
                .get();

        testable.registerUser(registrationForm)
                .as(StepVerifier::create)
                .expectNextCount(1)
                .verifyComplete();

        // verify that user is saved and we can't register with the same values(email, for example)
        testable.registerUser(registrationForm)
                .as(StepVerifier::create)
                .expectError(EmailAddressAlreadyInUseException.class)
                .verify();
    }

    private static class TestableBuilder {
        private PasswordEncoder passwordEncoder = new TestingPasswordEncoder();
        private final List<User> registeredUsers = new ArrayList<>();
        private BirthdatePolicy birthdatePolicy = olderThan(13);

        public static TestableBuilder builder() {
            return new TestableBuilder();
        }

        public TestableBuilder withPasswordEncoder(final PasswordEncoder passwordEncoder) {
            this.passwordEncoder = passwordEncoder;
            return this;
        }

        public TestableBuilder withUsers(@NotNull final String... emails) {

            for (final String email : emails) {
                User user = UserFaker.create().withEmail(email).get();

                registeredUsers.add(user);
            }

            return this;
        }

        public TestableBuilder withBirthdatePolicy(@NotNull final BirthdatePolicy birthdatePolicy) {
            this.birthdatePolicy = birthdatePolicy;
            return this;
        }

        public UnsecureEmailPasswordRegistrationManager build() {

            return new UnsecureEmailPasswordRegistrationManager(
                    new DefaultUserFactory(passwordEncoder),
                    new InMemoryUserService(registeredUsers),
                    birthdatePolicy
            );
        }
    }

    static class BirthdatePolicies {

        static BirthdatePolicy olderThan(final int minimalAllowedAge) {
            return new SimplePridicateBirthdatePolicy(
                    birthdate -> birthdate.isOlderThan(minimalAllowedAge)
            );
        }

        static BirthdatePolicy alwaysDeny() {
            return new SimplePridicateBirthdatePolicy(
                    birthdate -> false
            );
        }
    }
}