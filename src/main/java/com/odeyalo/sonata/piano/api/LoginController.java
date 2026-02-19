package com.odeyalo.sonata.piano.api;

import com.odeyalo.sonata.piano.api.dto.EmailPasswordLoginRequestDto;
import com.odeyalo.sonata.piano.api.dto.response.TokensDto;
import com.odeyalo.sonata.piano.model.Email;
import com.odeyalo.sonata.piano.model.LoginCredentials;
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

    public LoginController(final LoginManager loginManager) {
        this.loginManager = loginManager;
    }

    @PostMapping(value = "/email", consumes = APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<TokensDto>> loginWithEmail(@RequestBody @NotNull final EmailPasswordLoginRequestDto body) {
        final LoginCredentials credentials = LoginCredentials.of(
                Email.valueOf(body.email()),
                body.password()
        );
        return loginManager.login(credentials)
                .map(HttpStatuses::ok)
                .defaultIfEmpty(HttpStatuses.unauthorized());
    }
}
