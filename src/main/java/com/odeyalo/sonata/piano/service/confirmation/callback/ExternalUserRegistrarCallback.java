package com.odeyalo.sonata.piano.service.confirmation.callback;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.odeyalo.sonata.piano.model.User;
import com.odeyalo.sonata.piano.service.confirmation.ConfirmationCode;
import lombok.Builder;
import lombok.Value;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

/**
 * A callback that register the user in Piano-Profiles after successful email confirmation
 */
@Component
public final class ExternalUserRegistrarCallback implements UserEmailConfirmationCallback {
    private final WebClient webClient;

    public ExternalUserRegistrarCallback(final WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    @NotNull
    public Mono<Void> onSuccess(@NotNull final User user,
                                @NotNull final ConfirmationCode code) {

        final CreateUserInfoDto payload = CreateUserInfoDto.from(user);

        return webClient.post()
                .uri("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(payload)
                .retrieve()
                .toBodilessEntity().then();
    }

    @Value
    @Builder
    public static class CreateUserInfoDto {
        @JsonProperty("sonata_id")
        String id;
        @JsonProperty("username")
        String username;
        @JsonProperty("birthdate")
        LocalDate birthdate;
        @JsonProperty("email")
        String email;
        @JsonProperty("gender")
        String gender;
        @JsonProperty("country")
        String countryCode;

        @NotNull
        public static CreateUserInfoDto from(@NotNull final User user) {
            return CreateUserInfoDto.builder()
                    .id(user.id().value())
                    .username(user.id().value())
                    .birthdate(user.birthdate().value())
                    .email(user.email().value())
                    .gender(user.gender().name())
                    .countryCode("UA") // TODO: change me
                    .build();
        }
    }
}
