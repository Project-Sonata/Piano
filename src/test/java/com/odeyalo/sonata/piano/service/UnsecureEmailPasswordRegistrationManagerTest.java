package com.odeyalo.sonata.piano.service;

import com.odeyalo.sonata.piano.model.Email;
import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;
import testing.RegistrationFormFaker;

import static com.odeyalo.sonata.piano.service.RegistrationResult.NextAction.COMPLETED;
import static org.assertj.core.api.Assertions.assertThat;

class UnsecureEmailPasswordRegistrationManagerTest {

    @Test
    void shouldReturnCompletedStatusForValidRegistrationForm() {
        UnsecureEmailPasswordRegistrationManager testable = new UnsecureEmailPasswordRegistrationManager();

        RegistrationForm registrationForm = RegistrationFormFaker.create().get();

        testable.registerUser(registrationForm)
                .as(StepVerifier::create)
                .assertNext(result -> assertThat(result.nextStep()).isEqualTo(COMPLETED))
                .verifyComplete();
    }

    @Test
    void shouldReturnUserWithTheSameEmailAddressAsWasProvidedInRegistrationForm() {
        UnsecureEmailPasswordRegistrationManager testable = new UnsecureEmailPasswordRegistrationManager();

        RegistrationForm registrationForm = RegistrationFormFaker.create()
                .withEmail("miku.nakano@gmail.com")
                .get();

        testable.registerUser(registrationForm)
                .as(StepVerifier::create)
                .assertNext(result -> assertThat(result.registerdUser().email().asString()).isEqualTo("miku.nakano@gmail.com"))
                .verifyComplete();
    }

    @Test
    void shouldReturnActivatedUserAfterRegistrationComplete() {
        UnsecureEmailPasswordRegistrationManager testable = new UnsecureEmailPasswordRegistrationManager();

        RegistrationForm registrationForm = RegistrationFormFaker.create().get();

        testable.registerUser(registrationForm)
                .as(StepVerifier::create)
                .assertNext(result -> assertThat(result.registerdUser().isActivated()).isTrue())
                .verifyComplete();
    }
}