package testing.faker;


import com.github.javafaker.Faker;
import com.odeyalo.sonata.piano.entity.UserEntity;
import com.odeyalo.sonata.piano.model.*;
import org.jetbrains.annotations.NotNull;

public final class UserEntityFaker {
    private final UserEntity.UserEntityBuilder builder = UserEntity.builder();
    private final Faker faker = Faker.instance();

    public UserEntityFaker() {

        Birthdate birthdate = Birthdate.of(faker.date().birthday(18, 60));

        builder
                .externalId(UserId.random().value())
                .email(faker.internet().emailAddress())
                .password(faker.internet().password())
                .activated(true)
                .emailConfirmed(faker.random().nextBoolean())
                .birthdate(birthdate.toLocalDate())
                .gender(faker.options().option(Gender.class));
    }

    @NotNull
    public static UserEntityFaker newUser() {
        return new UserEntityFaker();
    }

    @NotNull
    public static UserEntityFaker existingUser(long internalId) {
        return new UserEntityFaker()
                .withInternalId(internalId);
    }

    @NotNull
    public UserEntityFaker withInternalId(final Long id) {
        builder.id(id);
        return this;
    }

    @NotNull
    public UserEntityFaker withExternalId(final String externalId) {
        builder.externalId(externalId);
        return this;
    }

    @NotNull
    public UserEntityFaker withEmail(final String email) {
        builder.email(email);
        return this;
    }

    @NotNull
    public UserEntityFaker withActivated(final boolean activated) {
        builder.activated(activated);
        return this;
    }

    @NotNull
    public UserEntity get() {
        return builder.build();
    }
}