package testing;

import com.github.javafaker.Faker;
import com.odeyalo.sonata.piano.api.dto.RegistrationFormDto;

import java.time.LocalDate;
import java.time.ZoneId;

import static com.odeyalo.sonata.piano.api.dto.RegistrationFormDto.*;

public final class RegistrationFormDtoFaker {
    private final Faker faker = Faker.instance();
    private final RegistrationFormDto.RegistrationFormDtoBuilder builder = RegistrationFormDto.builder();

    public RegistrationFormDtoFaker() {
        final LocalDate userBirthdate = faker.date().birthday(14, 100).toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
        builder
                .email(faker.internet().emailAddress())
                .password(faker.internet().password(8, 24, true, false, true))
                .withCustomEnableNotification(faker.options().option(NOTIFICATION_ENABLED, NOTIFICATION_DISABLED))
                .gender(faker.options().option(MALE, FEMALE, PREFER_NOT_TO_SAY))
                .birthdate(userBirthdate);
    }

    public static RegistrationFormDtoFaker randomForm() {
        return new RegistrationFormDtoFaker();
    }

    public RegistrationFormDto get() {
        return builder.build();
    }
}
