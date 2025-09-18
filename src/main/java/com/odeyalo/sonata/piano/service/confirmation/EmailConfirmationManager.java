package com.odeyalo.sonata.piano.service.confirmation;

import com.odeyalo.sonata.piano.exception.InvalidConfirmationCodeException;
import com.odeyalo.sonata.piano.model.ConfirmationStatus;
import com.odeyalo.sonata.piano.model.User;
import com.odeyalo.sonata.piano.service.UserService;
import com.odeyalo.sonata.piano.service.confirmation.callback.UserEmailConfirmationCallback;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;

import static com.odeyalo.sonata.piano.model.ConfirmationStatus.DENIED;
import static com.odeyalo.sonata.piano.model.ConfirmationStatus.OK;

@Service
public final class EmailConfirmationManager {
    private final ConfirmationCodeLoader confirmationCodeLoader;
    private final UserService userService;
    private final List<UserEmailConfirmationCallback> callbacks;

    public EmailConfirmationManager(final ConfirmationCodeLoader confirmationCodeLoader, final UserService userService) {
        this.confirmationCodeLoader = confirmationCodeLoader;
        this.userService = userService;
        this.callbacks = Collections.emptyList();
    }

    @Autowired
    public EmailConfirmationManager(final ConfirmationCodeLoader confirmationCodeLoader,
                                    final UserService userService,
                                    final List<UserEmailConfirmationCallback> callbacks) {
        this.confirmationCodeLoader = confirmationCodeLoader;
        this.userService = userService;
        this.callbacks = callbacks;
    }

    @NotNull
    public Mono<ConfirmationStatus> confirmEmail(@NotNull final String codeValue) {
        final Function<ConfirmationCode, Mono<ConfirmationStatus>> onUserSuccess = code -> confirmUserRegistration(code)
                .flatMap(user ->
                        Flux.fromIterable(callbacks)
                                .flatMap(callback -> callback.onSuccess(user, code))
                                .then()
                                .thenReturn(OK)
                );

        return confirmationCodeLoader.loadConfirmationCodeByValue(codeValue)
                .flatMap(onUserSuccess)
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
