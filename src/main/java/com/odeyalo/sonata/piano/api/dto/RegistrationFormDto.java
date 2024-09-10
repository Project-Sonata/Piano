package com.odeyalo.sonata.piano.api.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import java.time.LocalDate;

@Value
@Builder
@AllArgsConstructor(onConstructor_ = {@JsonCreator(mode = JsonCreator.Mode.PROPERTIES)})
public class RegistrationFormDto {
    @JsonProperty("email")
    String email;
    @JsonProperty("password")
    String password;
    @JsonProperty("birthdate")
    LocalDate birthdate;
}
