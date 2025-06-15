package com.odeyalo.sonata.piano.service.confirmation;

import com.odeyalo.sonata.piano.exception.InvalidConfirmationCodeException;
import com.odeyalo.sonata.piano.model.ConfirmationStatus;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import reactor.core.publisher.Mono;

@AllArgsConstructor
public final class EmailConfirmationManager {
    private final ConfirmationCodeLoader confirmationCodeLoader;

    @NotNull
    public Mono<ConfirmationStatus> confirmEmail(@NotNull final String codeValue) {
        return confirmationCodeLoader.loadConfirmationCodeByValue(codeValue)
                .map(code -> ConfirmationStatus.OK)
                .defaultIfEmpty(ConfirmationStatus.DENIED)
                .onErrorReturn(InvalidConfirmationCodeException.class, ConfirmationStatus.DENIED);
    }
}
