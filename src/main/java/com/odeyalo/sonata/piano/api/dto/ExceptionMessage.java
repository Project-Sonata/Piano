package com.odeyalo.sonata.piano.api.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import org.jetbrains.annotations.NotNull;

@Value
@Builder
@AllArgsConstructor(staticName = "of", onConstructor_ = {@JsonCreator(mode = JsonCreator.Mode.PROPERTIES)})
public class ExceptionMessage {
    @NotNull
    @JsonProperty("description")
    String description;
}
