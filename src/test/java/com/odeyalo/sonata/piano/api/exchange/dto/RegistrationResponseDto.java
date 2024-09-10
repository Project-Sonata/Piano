package com.odeyalo.sonata.piano.api.exchange.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
@AllArgsConstructor(onConstructor_ = {@JsonCreator(mode = JsonCreator.Mode.PROPERTIES)})
public class RegistrationResponseDto {
    @JsonProperty("action")
    String action;

    public String getAction() {
        return action;
    }
}
