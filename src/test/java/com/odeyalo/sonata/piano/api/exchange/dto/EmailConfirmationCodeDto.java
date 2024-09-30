package com.odeyalo.sonata.piano.api.exchange.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import lombok.With;
import org.jetbrains.annotations.Nullable;

@Value
@Builder
@With
@AllArgsConstructor(onConstructor_ = {@JsonCreator(mode = JsonCreator.Mode.PROPERTIES)})
public class EmailConfirmationCodeDto {
    @Nullable
    String code;

    public @Nullable String getCode() {
        return code;
    }
}
