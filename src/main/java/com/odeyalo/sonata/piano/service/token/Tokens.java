package com.odeyalo.sonata.piano.service.token;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import org.jetbrains.annotations.NotNull;

@Value
@Builder
@AllArgsConstructor
public class Tokens {
    @NotNull
    String accessToken;
}
