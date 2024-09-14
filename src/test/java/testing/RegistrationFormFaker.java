package testing;

import com.github.javafaker.Faker;
import com.odeyalo.sonata.piano.model.Email;
import com.odeyalo.sonata.piano.model.InputPassword;
import com.odeyalo.sonata.piano.service.RegistrationForm;
import org.jetbrains.annotations.NotNull;

public final class RegistrationFormFaker {
    private final RegistrationForm.RegistrationFormBuilder builder = RegistrationForm.builder();
    private final Faker faker = Faker.instance();

    public RegistrationFormFaker() {
        InputPassword randomPassword = InputPassword.valueOf(faker.internet().password(8, 32, true, false, true));

        builder
                .email(Email.valueOf(faker.internet().emailAddress()))
                .password(randomPassword);
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
}
