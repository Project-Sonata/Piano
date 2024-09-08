package com.odeyalo.sonata.piano.api;

import org.apache.commons.validator.routines.EmailValidator;
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

    @PostMapping("/email")
    public Mono<ResponseEntity<?>> emailRegistrationStrategy(@RequestBody @NotNull final RegistrationFormDto registrationFormDto) {
        if ( !EmailValidator.getInstance().isValid(registrationFormDto.email()) ) {
            return Mono.just(
                    ResponseEntity
                            .badRequest()
                            .body(ExceptionMessage.of("Email has invalid format"))
            );
        }

        return Mono.empty();
    }
}
