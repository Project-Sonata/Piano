package testing;

import com.github.javafaker.Faker;
import com.odeyalo.sonata.piano.model.*;
import org.jetbrains.annotations.NotNull;

public final class UserFaker {
    private final User.UserBuilder builder = User.builder();
    private final Faker faker = Faker.instance();

    public UserFaker() {

        Birthdate birthdate = Birthdate.of(faker.date().birthday(18, 60));

        builder
                .id(UserId.random())
                .email(Email.valueOf(faker.internet().emailAddress()))
                .password(faker.internet().password())
                .activated(true)
                .emailConfirmed(faker.random().nextBoolean())
                .birthdate(birthdate)
                .gender(faker.options().option(Gender.class));
    }

    public static UserFaker create() {
        return new UserFaker();
    }

    public UserFaker withEmail(final String email) {
        builder.email(Email.valueOf(email));
        return this;
    }

    @NotNull
    public User get() {
        return builder.build();
    }
}
