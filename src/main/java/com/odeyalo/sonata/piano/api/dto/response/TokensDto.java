package com.odeyalo.sonata.piano.api.dto.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import org.jetbrains.annotations.NotNull;

@Value
@Builder
@AllArgsConstructor(onConstructor_ = {@JsonCreator(mode = JsonCreator.Mode.PROPERTIES)})
public class TokensDto {
    @NotNull
    @JsonProperty("access_token")
    String accessToken;
}
