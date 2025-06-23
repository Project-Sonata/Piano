package com.odeyalo.sonata.piano.api;

import com.odeyalo.sonata.piano.api.dto.EmailConfirmationCodeDto;
import com.odeyalo.sonata.piano.api.dto.EmailConfirmationRequiredResponseDto;
import com.odeyalo.sonata.piano.service.confirmation.EmailConfirmationManager;
import com.odeyalo.sonata.piano.service.registration.email.EmailPasswordRegistrationManager;
import com.odeyalo.sonata.piano.service.registration.email.RegistrationForm;
import com.odeyalo.sonata.piano.support.web.HttpStatuses;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/v1/signup")
public final class UserRegistrationController {
    private final EmailPasswordRegistrationManager registrationManager;
    private final EmailConfirmationManager confirmationManager;

    public UserRegistrationController(final EmailPasswordRegistrationManager registrationManager,
                                      final EmailConfirmationManager confirmationManager) {
        this.registrationManager = registrationManager;
        this.confirmationManager = confirmationManager;
    }

    @PostMapping("/email")
    public Mono<ResponseEntity<?>> emailRegistrationStrategy(@NotNull final RegistrationForm registrationForm) {
        return registrationManager.registerUser(registrationForm)
                .map(it -> new EmailConfirmationRequiredResponseDto())
                .map(HttpStatuses::ok);
    }


    @PostMapping("/email/confirm")
    public Mono<ResponseEntity<?>> confirmUserEmail(@RequestBody EmailConfirmationCodeDto body) {
        return confirmationManager.confirmEmail(body.code())
                .map(decision -> {
                    if ( decision.isConfirmed() ) {
                        return HttpStatuses.ok();
                    }
                    return HttpStatuses.badRequest();
                });
    }
}
