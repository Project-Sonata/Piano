package com.odeyalo.sonata.piano.service.confirmation;

import com.odeyalo.sonata.piano.exception.InvalidConfirmationCodeException;
import com.odeyalo.sonata.piano.model.ConfirmationStatus;
import com.odeyalo.sonata.piano.model.User;
import com.odeyalo.sonata.piano.service.UserService;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import static com.odeyalo.sonata.piano.model.ConfirmationStatus.DENIED;
import static com.odeyalo.sonata.piano.model.ConfirmationStatus.OK;

@Service
public final class EmailConfirmationManager {
    private final ConfirmationCodeLoader confirmationCodeLoader;
    private final UserService userService;

    public EmailConfirmationManager(final ConfirmationCodeLoader confirmationCodeLoader, final UserService userService) {
        this.confirmationCodeLoader = confirmationCodeLoader;
        this.userService = userService;
    }

    @NotNull
    public Mono<ConfirmationStatus> confirmEmail(@NotNull final String codeValue) {
        return confirmationCodeLoader.loadConfirmationCodeByValue(codeValue)
                .flatMap(this::confirmUserRegistration).map(it -> OK)
                .defaultIfEmpty(DENIED)
                .onErrorReturn(InvalidConfirmationCodeException.class, DENIED);
    }

    @NotNull
    private Mono<User> confirmUserRegistration(@NotNull final ConfirmationCode code) {
        final User activatedUser = code.generatedFor()
                .activate();
        return userService.save(activatedUser);
    }
}
