package testing;

import com.github.javafaker.Faker;
import com.odeyalo.sonata.piano.model.Email;
import com.odeyalo.sonata.piano.service.RegistrationForm;
import org.jetbrains.annotations.NotNull;

public final class RegistrationFormFaker {
    private final RegistrationForm.RegistrationFormBuilder builder = RegistrationForm.builder();
    private final Faker faker = Faker.instance();

    public RegistrationFormFaker() {
        builder
                .email(Email.valueOf(faker.internet().emailAddress()));
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
}
