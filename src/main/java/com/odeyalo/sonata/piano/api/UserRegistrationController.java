package com.odeyalo.sonata.piano.api;

import com.odeyalo.sonata.piano.api.dto.EmailConfirmationCodeDto;
import com.odeyalo.sonata.piano.api.dto.EmailConfirmationRequiredResponseDto;
import com.odeyalo.sonata.piano.api.dto.response.TokensDto;
import com.odeyalo.sonata.piano.exception.InvalidConfirmationCodeException;
import com.odeyalo.sonata.piano.service.confirmation.EmailConfirmationGateway;
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

import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;


@RestController
@RequestMapping("/v1/signup")
public final class UserRegistrationController {
    private final EmailPasswordRegistrationManager registrationManager;
    private final EmailConfirmationManager confirmationManager;
    private final EmailConfirmationGateway emailConfirmationGateway;

    public UserRegistrationController(final EmailPasswordRegistrationManager registrationManager,
                                      final EmailConfirmationManager confirmationManager,
                                      final EmailConfirmationGateway emailConfirmationGateway) {
        this.registrationManager = registrationManager;
        this.confirmationManager = confirmationManager;
        this.emailConfirmationGateway = emailConfirmationGateway;
    }

    @PostMapping(value = "/email", consumes = APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<?>> emailRegistrationStrategy(@NotNull final RegistrationForm registrationForm) {
        return registrationManager.registerUser(registrationForm)
                .map(it -> new EmailConfirmationRequiredResponseDto())
                .map(HttpStatuses::ok);
    }

    @PostMapping(value = "/email/confirm", consumes = APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<TokensDto>> confirmUserEmail(@RequestBody final EmailConfirmationCodeDto body) {
        return emailConfirmationGateway.confirmEmail(body.code())
                .map(tokens -> new TokensDto(tokens.accessToken()))
                .map(HttpStatuses::ok)
                .onErrorReturn(InvalidConfirmationCodeException.class, HttpStatuses.badRequest());
    }
}
