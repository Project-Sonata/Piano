package com.odeyalo.sonata.piano.service;

import com.odeyalo.sonata.piano.model.Gender;
import com.odeyalo.sonata.piano.service.support.PasswordEncoder;
import com.odeyalo.sonata.piano.service.support.TestingPasswordEncoder;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;
import reactor.test.StepVerifier;
import testing.RegistrationFormFaker;

import java.time.LocalDate;
import java.time.Month;

import static com.odeyalo.sonata.piano.service.RegistrationResult.NextAction.COMPLETED;
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

    private static class TestableBuilder {
        private PasswordEncoder passwordEncoder = new TestingPasswordEncoder();

        public static TestableBuilder builder() {
            return new TestableBuilder();
        }

        public TestableBuilder withPasswordEncoder(final PasswordEncoder passwordEncoder) {
            this.passwordEncoder = passwordEncoder;
            return this;
        }

        public UnsecureEmailPasswordRegistrationManager build() {
            return new UnsecureEmailPasswordRegistrationManager(passwordEncoder);
        }
    }
}