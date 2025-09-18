package com.odeyalo.sonata.piano.config;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class ExternalApiClientConfiguration {

    @Bean
    public WebClient remoteSonataProfilesWebClient(@Value("${sonata.profiles.url}") @NotNull final String url) {
        return WebClient.builder()
                .baseUrl(url)
                .build();
    }
}
