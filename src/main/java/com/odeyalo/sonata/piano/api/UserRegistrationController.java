package com.odeyalo.sonata.piano.api;

import com.odeyalo.sonata.piano.api.dto.EmailConfirmationRequiredResponseDto;
import com.odeyalo.sonata.piano.api.dto.ExceptionMessage;
import com.odeyalo.sonata.piano.api.dto.RegistrationFormDto;
import com.odeyalo.sonata.piano.model.Birthdate;
import com.odeyalo.sonata.piano.model.Email;
import com.odeyalo.sonata.piano.model.InputPassword;
import com.odeyalo.sonata.piano.service.EmailPasswordRegistrationManager;
import com.odeyalo.sonata.piano.service.RegistrationForm;
import com.odeyalo.sonata.piano.support.web.HttpStatuses;
import org.apache.commons.validator.routines.EmailValidator;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/v1/signup")
public final class UserRegistrationController {
    private final EmailPasswordRegistrationManager registrationManager;

    public UserRegistrationController(final EmailPasswordRegistrationManager registrationManager) {
        this.registrationManager = registrationManager;
    }

    @PostMapping("/email")
    public Mono<ResponseEntity<?>> emailRegistrationStrategy(@RequestBody @NotNull final RegistrationFormDto registrationFormDto) {
        if ( !EmailValidator.getInstance().isValid(registrationFormDto.email()) ) {
            return Mono.just(
                    ResponseEntity
                            .badRequest()
                            .body(ExceptionMessage.of("Email has invalid format"))
            );
        }

        if ( LocalDate.now().minusYears(13).isBefore(registrationFormDto.birthdate()) ) {
            return Mono.just(
                    ResponseEntity
                            .badRequest()
                            .body(ExceptionMessage.of("User must be older than 13 years"))
            );
        }

        RegistrationForm form = RegistrationForm.builder()
                .email(Email.valueOf(registrationFormDto.email()))
                .gender(registrationFormDto.gender())
                .password(InputPassword.valueOf(registrationFormDto.password()))
                .birthdate(Birthdate.of(registrationFormDto.birthdate()))
                .build();

        return registrationManager.registerUser(form)
                .map(it -> new EmailConfirmationRequiredResponseDto())
                .map(HttpStatuses::ok);
    }
}
