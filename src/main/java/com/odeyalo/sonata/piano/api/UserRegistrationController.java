package com.odeyalo.sonata.piano.api;

import com.odeyalo.sonata.piano.api.dto.EmailConfirmationRequiredResponseDto;
import com.odeyalo.sonata.piano.service.registration.email.EmailPasswordRegistrationManager;
import com.odeyalo.sonata.piano.service.registration.email.RegistrationForm;
import com.odeyalo.sonata.piano.support.web.HttpStatuses;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/v1/signup")
public final class UserRegistrationController {
    private final EmailPasswordRegistrationManager registrationManager;

    public UserRegistrationController(final EmailPasswordRegistrationManager registrationManager) {
        this.registrationManager = registrationManager;
    }

    @PostMapping("/email")
    public Mono<ResponseEntity<?>> emailRegistrationStrategy(@NotNull final RegistrationForm registrationForm) {
        return registrationManager.registerUser(registrationForm)
                .map(it -> new EmailConfirmationRequiredResponseDto())
                .map(HttpStatuses::ok);
    }
}
