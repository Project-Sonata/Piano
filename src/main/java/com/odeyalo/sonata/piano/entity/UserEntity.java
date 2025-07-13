package com.odeyalo.sonata.piano.entity;

import com.odeyalo.sonata.piano.model.Gender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import lombok.With;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDate;

@Value
@Builder
@Table("users")
@With
public class UserEntity {
    @Id
    Long id;
    @NotNull
    @Column("external_id")
    String externalId;
    @NotNull
    @Column("email")
    String email;
    @NotNull
    @Column("password")
    String password;
    @NotNull
    @Column("gender")
    Gender gender;
    @Column("is_activated")
    boolean activated;
    @Column("is_email_confirmed")
    boolean emailConfirmed;
    @NotNull
    @Column("birthdate")
    LocalDate birthdate;

    @PersistenceCreator
    public UserEntity(final Long id, @NotNull final String externalId, @NotNull final String email, @NotNull final String password, @NotNull final Gender gender, final boolean activated, final boolean emailConfirmed, @NotNull final LocalDate birthdate) {
        this.id = id;
        this.externalId = externalId;
        this.email = email;
        this.password = password;
        this.gender = gender;
        this.activated = activated;
        this.emailConfirmed = emailConfirmed;
        this.birthdate = birthdate;
    }

    @NotNull
    public static UserEntityBuilder create() {
        return builder();
    }
}
