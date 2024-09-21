package com.odeyalo.sonata.piano.api;

import com.odeyalo.sonata.piano.api.dto.EmailConfirmationRequiredResponseDto;
import com.odeyalo.sonata.piano.api.dto.ExceptionMessage;
import com.odeyalo.sonata.piano.service.registration.email.EmailPasswordRegistrationManager;
import com.odeyalo.sonata.piano.service.registration.email.RegistrationForm;
import com.odeyalo.sonata.piano.support.web.HttpStatuses;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

@RestController
@RequestMapping("/v1/signup")
public final class UserRegistrationController {
    private final EmailPasswordRegistrationManager registrationManager;

    public UserRegistrationController(final EmailPasswordRegistrationManager registrationManager) {
        this.registrationManager = registrationManager;
    }

    @PostMapping("/email")
    public Mono<ResponseEntity<?>> emailRegistrationStrategy(@NotNull final RegistrationForm registrationForm) {

        if ( LocalDate.now().minusYears(13).isBefore(registrationForm.birthdate().toLocalDate()) ) {
            return Mono.just(
                    ResponseEntity
                            .badRequest()
                            .body(ExceptionMessage.of("User must be older than 13 years"))
            );
        }


        return registrationManager.registerUser(registrationForm)
                .map(it -> new EmailConfirmationRequiredResponseDto())
                .map(HttpStatuses::ok);
    }
}
