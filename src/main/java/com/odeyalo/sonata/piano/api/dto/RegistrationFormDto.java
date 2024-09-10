package com.odeyalo.sonata.piano.api.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.odeyalo.sonata.piano.api.model.Gender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;

@Value
@Builder
@AllArgsConstructor(onConstructor_ = {@JsonCreator(mode = JsonCreator.Mode.PROPERTIES)})
public class RegistrationFormDto {
    @JsonProperty("email")
    @NotNull
    String email;
    @JsonProperty("password")
    @NotNull
    String password;
    @JsonProperty("birthdate")
    @NotNull
    LocalDate birthdate;
    @NotNull
    Gender gender;
    @JsonProperty("enable_notification")
    boolean enableNotification;
}
