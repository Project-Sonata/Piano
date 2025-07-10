package com.odeyalo.sonata.piano.support.html;

import org.jetbrains.annotations.NotNull;
import reactor.core.publisher.Mono;

import java.util.Map;

public interface TemplateEngine {

    @NotNull
    Mono<String> render(@NotNull String templateName,
                        @NotNull Map<String, Object> model);
}
