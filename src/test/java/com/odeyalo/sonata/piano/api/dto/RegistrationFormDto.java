package com.odeyalo.sonata.piano.api.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Value;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import testing.RegistrationFormDtoFaker;

import java.security.cert.CertPathBuilder;
import java.time.LocalDate;
import java.util.stream.DoubleStream;

@Data
@Accessors
@Builder
@AllArgsConstructor(onConstructor_ = {@JsonCreator(mode = JsonCreator.Mode.PROPERTIES)})
public class RegistrationFormDto {
    @Nullable
    String email;
    @Nullable
    String password;
    @Nullable
    String gender;
    @Nullable
    String birthdate;
    @Nullable
    String enableNotification;

    public static final String MALE = "male";
    public static final String FEMALE = "female";
    public static final String PREFER_NOT_TO_SAY = "none";
    public static final String NOTIFICATION_ENABLED = "true";
    public static final String NOTIFICATION_DISABLED = "false";

    public static RegistrationFormDto randomForm() {
        return RegistrationFormDtoFaker.randomForm().get();
    }

    public static class RegistrationFormDtoBuilder {
        private String gender;
        private String enableNotification;

        public RegistrationFormDtoBuilder male() {
            this.gender = MALE;
            return this;
        }

        public RegistrationFormDtoBuilder female() {
            this.gender = FEMALE;
            return this;
        }

        public RegistrationFormDtoBuilder preferNotToSay() {
            this.gender = PREFER_NOT_TO_SAY;
            return this;
        }

        public RegistrationFormDtoBuilder withEnabledNotifications() {
            this.enableNotification = NOTIFICATION_ENABLED;
            return this;
        }

        public RegistrationFormDtoBuilder withDisabledNotifications() {
            this.enableNotification = NOTIFICATION_DISABLED;
            return this;
        }

        public RegistrationFormDtoBuilder withCustomEnableNotification(@Nullable String enableNotification) {
            this.enableNotification = enableNotification;
            return this;
        }

        public RegistrationFormDtoBuilder birthdate(@NotNull LocalDate birthdate) {
            this.birthdate = birthdate.toString();
            return this;
        }
    }
}
