package com.odeyalo.sonata.piano.service.confirmation;

import com.odeyalo.sonata.piano.exception.InvalidConfirmationCodeException;
import com.odeyalo.sonata.piano.model.User;
import org.jetbrains.annotations.NotNull;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

public final class InMemoryConfirmationCodeService implements ConfirmationCodeService {
    private final Map<String, ConfirmationCode> confirmationCodes;
    private final ConfirmationCodeFactory confirmationCodeFactory;

    public InMemoryConfirmationCodeService(final ConfirmationCodeFactory confirmationCodeFactory) {
        this.confirmationCodeFactory = confirmationCodeFactory;
        this.confirmationCodes = new ConcurrentHashMap<>();
    }

    public InMemoryConfirmationCodeService(List<ConfirmationCode> confirmationCodes, final ConfirmationCodeFactory confirmationCodeFactory) {

        this.confirmationCodes = confirmationCodes.stream().collect(Collectors.toMap(
                ConfirmationCode::value,
                Function.identity()
        ));
        this.confirmationCodeFactory = confirmationCodeFactory;
    }

    @Override
    @NotNull
    public Mono<ConfirmationCode> newConfirmationCodeFor(final @NotNull User user) {
        return Mono.fromCallable(() -> {
            ConfirmationCode code = confirmationCodeFactory.newConfirmationCodeFor(user);
            confirmationCodes.put(code.value(), code);
            return code;
        });
    }

    @Override
    @NotNull
    public Mono<ConfirmationCode> loadConfirmationCodeByValue(final @NotNull String value) {
        return Mono.justOrEmpty(confirmationCodes.get(value))
                .flatMap(confirmationCode -> {
                    if ( confirmationCode.isExpired() ) {
                        return Mono.error(new InvalidConfirmationCodeException("Confirmation code is expired and can't be used!"));
                    }
                    return Mono.just(confirmationCode);
                });
    }
}
