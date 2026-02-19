package com.odeyalo.sonata.piano.service.login;

import com.odeyalo.sonata.piano.entity.UserEntity;
import com.odeyalo.sonata.piano.repository.UserRepository;
import com.odeyalo.sonata.piano.service.support.PasswordEncoder;
import com.odeyalo.sonata.piano.support.jwt.JwtToken;
import com.odeyalo.sonata.piano.support.jwt.JwtTokenManager;
import com.odeyalo.sonata.piano.support.jwt.Lifetime;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import testing.faker.UserEntityFaker;

import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DefaultLoginManagerTest {

    @Mock
    UserRepository userRepository;

    @Mock
    PasswordEncoder passwordEncoder;

    @Mock
    JwtTokenManager jwtTokenManager;

    @InjectMocks
    DefaultLoginManager loginManager;

    @Test
    void login_withValidCredentials_shouldReturnTokens() {
        String email = "test@example.com";
        String password = "password";
        String encodedPassword = "encodedPassword";
        UserEntity user = UserEntityFaker.newUser()
                .withEmail(email)
                .get()
                .withPassword(encodedPassword);

        when(userRepository.findByEmail(email)).thenReturn(Mono.just(user));
        when(passwordEncoder.matches(password, encodedPassword)).thenReturn(true);
        Lifetime lifetime = Lifetime.lasting(java.time.Duration.ofMinutes(15));
        when(jwtTokenManager.generateJwt(any())).thenReturn(Mono.just(JwtToken.of("token", lifetime, Map.of())));

        StepVerifier.create(loginManager.login(email, password))
                .expectNextMatches(tokens -> tokens.accessToken().equals("token"))
                .verifyComplete();
    }

    @Test
    void login_withInvalidPassword_shouldReturnEmpty() {
        String email = "test@example.com";
        String password = "wrongPassword";
        String encodedPassword = "encodedPassword";
        UserEntity user = UserEntityFaker.newUser()
                .withEmail(email)
                .get()
                .withPassword(encodedPassword);

        when(userRepository.findByEmail(email)).thenReturn(Mono.just(user));
        when(passwordEncoder.matches(password, encodedPassword)).thenReturn(false);

        StepVerifier.create(loginManager.login(email, password))
                .verifyComplete();
    }

    @Test
    void login_withNonExistentUser_shouldReturnEmpty() {
        String email = "nonexistent@example.com";
        String password = "password";

        when(userRepository.findByEmail(email)).thenReturn(Mono.empty());

        StepVerifier.create(loginManager.login(email, password))
                .verifyComplete();
    }
}
