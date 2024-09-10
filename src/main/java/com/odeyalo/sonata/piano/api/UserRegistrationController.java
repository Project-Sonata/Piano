package com.odeyalo.sonata.piano.api;

import com.odeyalo.sonata.piano.api.dto.EmailConfirmationRequiredResponseDto;
import com.odeyalo.sonata.piano.api.dto.ExceptionMessage;
import com.odeyalo.sonata.piano.api.dto.RegistrationFormDto;
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

    @PostMapping("/email")
    public Mono<ResponseEntity<?>> emailRegistrationStrategy(@RequestBody @NotNull final RegistrationFormDto registrationFormDto) {
        if ( !EmailValidator.getInstance().isValid(registrationFormDto.email()) ) {
            return Mono.just(
                    ResponseEntity
                            .badRequest()
                            .body(ExceptionMessage.of("Email has invalid format"))
            );
        }

        String pattern = "^(?=.*[0-9])(?=.*[a-zA-Z]).{8,}$";

        if ( !Pattern.compile(pattern).matcher(registrationFormDto.password()).matches() ) {
            return Mono.just(
                    ResponseEntity
                            .badRequest()
                            .body(ExceptionMessage.of("Password must contain at least 8 characters and at least 1 number"))
            );
        }

        if ( LocalDate.now().minusYears(13).isBefore(registrationFormDto.birthdate()) ) {
            return Mono.just(
                    ResponseEntity
                            .badRequest()
                            .body(ExceptionMessage.of("User must be older than 13 years"))
            );
        }

        return Mono.just(
                ResponseEntity.ok(
                        new EmailConfirmationRequiredResponseDto()
                )
        );
    }
}
