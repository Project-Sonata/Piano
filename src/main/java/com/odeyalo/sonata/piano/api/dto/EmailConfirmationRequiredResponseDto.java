package com.odeyalo.sonata.piano.api.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import org.jetbrains.annotations.NotNull;

@Value
@Builder
@AllArgsConstructor(onConstructor_ = {@JsonCreator(mode = JsonCreator.Mode.PROPERTIES)})
public class EmailConfirmationRequiredResponseDto {

    @NotNull
    @JsonProperty("action")
    public String getAction() {
        return "email_confirmation_required";
    }
}
