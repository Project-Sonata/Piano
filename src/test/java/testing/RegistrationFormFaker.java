package testing;

import com.github.javafaker.Faker;
import com.odeyalo.sonata.piano.model.Birthdate;
import com.odeyalo.sonata.piano.model.Email;
import com.odeyalo.sonata.piano.model.Gender;
import com.odeyalo.sonata.piano.model.InputPassword;
import com.odeyalo.sonata.piano.service.RegistrationForm;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;

public final class RegistrationFormFaker {
    private final RegistrationForm.RegistrationFormBuilder builder = RegistrationForm.builder();
    private final Faker faker = Faker.instance();

    public RegistrationFormFaker() {
        InputPassword randomPassword = InputPassword.valueOf(faker.internet().password(8, 32, true, false, true) + "123");
        Birthdate birthdate = Birthdate.of(faker.date().birthday(18, 60));

        builder
                .email(Email.valueOf(faker.internet().emailAddress()))
                .password(randomPassword)
                .birthdate(birthdate)
                .gender(faker.options().option(Gender.class));
    }


    public static RegistrationFormFaker create() {
        return new RegistrationFormFaker();
    }

    public RegistrationForm get() {
        return builder.build();
    }

    @NotNull
    public RegistrationFormFaker withEmail(@NotNull final String email) {
        builder.email(Email.valueOf(email));
        return this;
    }

    public RegistrationFormFaker withPassword(@NotNull final String password) {
        builder.password(InputPassword.valueOf(password));
        return this;
    }

    public RegistrationFormFaker withGender(@NotNull final Gender gender) {
        builder.gender(gender);
        return this;
    }

    public RegistrationFormFaker withBirthdate(@NotNull final LocalDate birthdate) {
        builder.birthdate(Birthdate.of(birthdate));
        return this;
    }
}
