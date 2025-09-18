package com.odeyalo.sonata.piano.service.confirmation;

import com.odeyalo.sonata.piano.exception.InvalidConfirmationCodeException;
import com.odeyalo.sonata.piano.model.ConfirmationStatus;
import com.odeyalo.sonata.piano.service.token.Tokens;
import com.odeyalo.sonata.piano.service.token.TokensGenerator;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public final class EmailConfirmationGateway {
    private final EmailConfirmationManager confirmationManager;
    private final ConfirmationCodeLoader confirmationCodeLoader;
    private final TokensGenerator tokensGenerator;

    public EmailConfirmationGateway(final EmailConfirmationManager confirmationManager,
                                    final ConfirmationCodeLoader confirmationCodeLoader,
                                    final TokensGenerator tokensGenerator) {
        this.confirmationManager = confirmationManager;
        this.confirmationCodeLoader = confirmationCodeLoader;
        this.tokensGenerator = tokensGenerator;
    }

    @NotNull
    public Mono<Tokens> confirmEmail(@NotNull final String code) {
        return confirmationManager.confirmEmail(code)
                .flatMap(status -> {
                    if ( status == ConfirmationStatus.DENIED ) {
                        return Mono.error(new InvalidConfirmationCodeException());
                    }
                    return confirmationCodeLoader.loadConfirmationCodeByValue(code).map(ConfirmationCode::generatedFor);
                }).flatMap(tokensGenerator::generateTokensFor);
    }

}
