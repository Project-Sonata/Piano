package com.odeyalo.sonata.piano.config;

import com.odeyalo.sonata.piano.service.confirmation.ConfirmationCodeFactory;
import com.odeyalo.sonata.piano.service.confirmation.ConfirmationCodeService;
import com.odeyalo.sonata.piano.service.confirmation.InMemoryConfirmationCodeService;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EmailConfirmationConfiguration {
    private final Logger logger = LoggerFactory.getLogger(EmailConfirmationConfiguration.class);

    @Bean
    public ConfirmationCodeService confirmationCodeLoader(@NotNull final ConfirmationCodeFactory confirmationCodeFactory) {
        logger.info("Using in-memory confirmation code loader... Safe for dev purposes, but should be avoided in prod");
        return new InMemoryConfirmationCodeService(confirmationCodeFactory);
    }

}
