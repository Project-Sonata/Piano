package com.odeyalo.sonata.piano.service.confirmation;

import com.odeyalo.sonata.piano.model.Email;
import com.odeyalo.sonata.piano.service.mail.EmailMessage;
import com.odeyalo.sonata.piano.support.html.TemplateEngine;
import org.jetbrains.annotations.NotNull;
import reactor.core.publisher.Mono;

import java.util.Map;

public final class HtmlEmailConfirmationMessageTemplateFactory implements EmailConfirmationMessageTemplateFactory {
    private final TemplateEngine templateEngine;

    public HtmlEmailConfirmationMessageTemplateFactory(final TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    @Override
    @NotNull
    public Mono<EmailMessage> createEmailMessageAsync(@NotNull final Email to,
                                                      @NotNull final ConfirmationCode code) {
        final Map<String, Object> model = Map.of("confirmationCode", code.value());

        return templateEngine.render("confirmation", model)
                .map(content -> EmailMessage.of(to, "Sonata confirmation code", content, true));
    }
}
