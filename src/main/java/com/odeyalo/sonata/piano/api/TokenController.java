package com.odeyalo.sonata.piano.api;

import com.odeyalo.sonata.piano.support.jwt.JwtTokenManager;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.Map;

@RestController
@RequestMapping("/v1/tokens")
public final class TokenController {
    private final JwtTokenManager jwtTokenManager;

    public TokenController(final JwtTokenManager jwtTokenManager) {
        this.jwtTokenManager = jwtTokenManager;
    }

    @PostMapping("/access")
    public Mono<ResponseEntity<?>> validateAccessToken(@RequestBody final Map<String, Object> body) {
        final String accessToken = (String) body.get("access_token");
        return jwtTokenManager.parseToken(accessToken)
                .map(metadata -> {
                    if ( metadata.remainingLifetime().isExpired() ) {
                        return ResponseEntity.badRequest().build();
                    }
                    final String userId = metadata.get("user_id", String.class);

                    return ResponseEntity.ok(Map.of(
                            "expires_at", metadata.remainingLifetime().expiresAt().getEpochSecond(),
                            "user_id", userId
                    ));
                });
    }
}
