package com.odeyalo.sonata.piano.support.html;

import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.context.Context;
import reactor.core.publisher.Mono;

import java.util.Locale;
import java.util.Map;

@Component
public final class ThymeleafTemplateEngine implements TemplateEngine {
    private final ITemplateEngine templateEngine;

    public ThymeleafTemplateEngine(final ITemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    @Override
    @NotNull
    public Mono<String> render(@NotNull final String templateName,
                               @NotNull final Map<String, Object> model) {

        return Mono.fromCallable(() -> {
            final Context context = new Context(Locale.ENGLISH, model);
            return templateEngine.process(templateName, context);
        });
    }
}
