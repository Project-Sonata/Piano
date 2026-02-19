package com.odeyalo.sonata.piano.api;

import com.odeyalo.sonata.piano.api.dto.EmailPasswordLoginRequestDto;
import com.odeyalo.sonata.piano.api.dto.response.TokensDto;
import com.odeyalo.sonata.piano.service.login.LoginManager;
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
@RequestMapping("/v1/login")
public final class LoginController {
    private final LoginManager loginManager;

    public LoginController(LoginManager loginManager) {
        this.loginManager = loginManager;
    }

    @PostMapping(value = "/email", consumes = APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<TokensDto>> loginWithEmail(@RequestBody @NotNull final EmailPasswordLoginRequestDto body) {
        return loginManager.login(body.email(), body.password())
                .map(HttpStatuses::ok)
                .defaultIfEmpty(ResponseEntity.status(401).build());
    }
}
