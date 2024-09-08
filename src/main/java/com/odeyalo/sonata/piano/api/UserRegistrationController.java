package com.odeyalo.sonata.piano.api;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/v1/signup")
public final class UserRegistrationController {

    @PostMapping("/email")
    public Mono<Void> emailRegistrationStrategy() {
        return Mono.empty();
    }
}
