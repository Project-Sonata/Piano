package com.odeyalo.sonata.piano.support.jwt;

import io.jsonwebtoken.Claims;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Singular;
import lombok.Value;
import org.jetbrains.annotations.NotNull;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.odeyalo.sonata.piano.support.jwt.JwtTokenGenerator.GenerationOptions.DefaultClaimsOverridePolicy.DO_NOT_OVERRIDE;

/**
 * Generate JWT tokens only.
 */
public interface JwtTokenGenerator {

    GenerationOptions DEFAULT_OPTIONS = GenerationOptions.useDefault();

    Collection<String> DEFAULT_CLAIMS = List.of(Claims.ID, Claims.ISSUED_AT, Claims.EXPIRATION);

    /**
     * Generate jwt token and return it
     *
     * @param options - options to generate JWT token with
     * @return - mono with JwtToken
     */
    @NotNull
    Mono<JwtToken> generateJwt(@NotNull GenerationOptions options);

    @Value
    @AllArgsConstructor(staticName = "of")
    @Builder
    class GenerationOptions {
        @Singular
        Map<String, Object> additionalClaims;
        @Builder.Default
        DefaultClaimsOverridePolicy defaultClaimsOverridePolicy = DefaultClaimsOverridePolicy.DO_NOT_OVERRIDE;
        @Builder.Default
        Duration lifetime = Duration.ofMinutes(15);

        public static GenerationOptions useDefault() {
            return builder().build();
        }

        @NotNull
        public Map<String, Object> getNormalizedClaims() {
            if ( defaultClaimsOverridePolicy() != DO_NOT_OVERRIDE ) {
                return additionalClaims();
            }
            return filterOutDefaultClaims();
        }

        @NotNull
        private Map<String, Object> filterOutDefaultClaims() {
            return additionalClaims().entrySet().stream()
                    .filter(entry -> !DEFAULT_CLAIMS.contains(entry.getKey()))
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        }

        public enum DefaultClaimsOverridePolicy {
            OVERRIDE,
            DO_NOT_OVERRIDE
        }
    }
}
