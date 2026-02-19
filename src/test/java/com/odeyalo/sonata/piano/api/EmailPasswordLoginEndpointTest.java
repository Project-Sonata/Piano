package com.odeyalo.sonata.piano.api;

import com.odeyalo.sonata.piano.api.dto.EmailPasswordLoginRequestDto;
import com.odeyalo.sonata.piano.api.dto.response.TokensDto;
import com.odeyalo.sonata.piano.api.exchange.dto.ExceptionMessageDto;
import com.odeyalo.sonata.piano.entity.UserEntity;
import com.odeyalo.sonata.piano.repository.UserRepository;
import com.odeyalo.sonata.piano.service.support.PasswordEncoder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import testing.api.client.PianoClient;
import testing.api.client.config.AutoConfigurePianoClient;
import testing.base.AbstractIntegrationTest;
import testing.faker.UserEntityFaker;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureWebTestClient
@AutoConfigurePianoClient
@ActiveProfiles("test")
class EmailPasswordLoginEndpointTest extends AbstractIntegrationTest {

    @Autowired
    PianoClient pianoClient;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll().block();
    }

    @Test
    void shouldReturn200OkIfCredentialsAreValid() {
        final String password = "password123";
        final UserEntity user = UserEntityFaker.newUser()
                .withEmail("test@example.com")
                .withActivated(true)
                .withEmailConfirmed(true)
                .get()
                .withPassword(passwordEncoder.encode(password));

        userRepository.save(user).block();

        final EmailPasswordLoginRequestDto loginRequest = EmailPasswordLoginRequestDto.builder()
                .email("test@example.com")
                .password(password)
                .build();

        final WebTestClient.ResponseSpec response = pianoClient.login(loginRequest);

        response.expectStatus().isOk()
                .expectBody(TokensDto.class)
                .value(tokens -> assertThat(tokens.accessToken()).isNotEmpty());
    }

    @Test
    void shouldReturn403ForbiddenIfEmailIsNotConfirmed() {
        final String password = "password123";
        final UserEntity user = UserEntityFaker.newUser()
                .withEmail("test@example.com")
                .withActivated(true)
                .withEmailConfirmed(false)
                .get()
                .withPassword(passwordEncoder.encode(password));

        userRepository.save(user).block();

        final EmailPasswordLoginRequestDto loginRequest = EmailPasswordLoginRequestDto.builder()
                .email("test@example.com")
                .password(password)
                .build();

        final WebTestClient.ResponseSpec response = pianoClient.login(loginRequest);

        response.expectStatus().isForbidden()
                .expectBody(ExceptionMessageDto.class)
                .value(message -> assertThat(message.description()).isEqualTo("Email is not confirmed"));
    }

    @Test
    void shouldReturn403ForbiddenIfAccountIsNotActivated() {
        final String password = "password123";
        final UserEntity user = UserEntityFaker.newUser()
                .withEmail("test@example.com")
                .withActivated(false)
                .withEmailConfirmed(true)
                .get()
                .withPassword(passwordEncoder.encode(password));

        userRepository.save(user).block();

        final EmailPasswordLoginRequestDto loginRequest = EmailPasswordLoginRequestDto.builder()
                .email("test@example.com")
                .password(password)
                .build();

        final WebTestClient.ResponseSpec response = pianoClient.login(loginRequest);

        response.expectStatus().isForbidden()
                .expectBody(ExceptionMessageDto.class)
                .value(message -> assertThat(message.description()).isEqualTo("Email is not confirmed"));
    }

    @Test
    void shouldReturn401IfPasswordIsIncorrect() {
        final String password = "password123";
        final UserEntity user = UserEntityFaker.newUser()
                .withEmail("test@example.com")
                .get()
                .withPassword(passwordEncoder.encode(password));

        userRepository.save(user).block();

        final EmailPasswordLoginRequestDto loginRequest = EmailPasswordLoginRequestDto.builder()
                .email("test@example.com")
                .password("wrongpassword")
                .build();

        final WebTestClient.ResponseSpec response = pianoClient.login(loginRequest);

        response.expectStatus().isUnauthorized();
    }

    @Test
    void shouldReturn401IfUserDoesNotExist() {
        final EmailPasswordLoginRequestDto loginRequest = EmailPasswordLoginRequestDto.builder()
                .email("nonexistent@example.com")
                .password("password123")
                .build();

        final WebTestClient.ResponseSpec response = pianoClient.login(loginRequest);

        response.expectStatus().isUnauthorized();
    }
}
