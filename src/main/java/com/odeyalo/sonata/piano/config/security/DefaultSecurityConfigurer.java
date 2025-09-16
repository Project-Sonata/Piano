package com.odeyalo.sonata.piano.config.security;

import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.config.CorsRegistry;
import org.springframework.web.reactive.config.WebFluxConfigurer;

@Component
public class DefaultSecurityConfigurer implements WebFluxConfigurer {

    @Override
    public void addCorsMappings(@NotNull final CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("*")
                .allowedMethods("*");
    }
}
