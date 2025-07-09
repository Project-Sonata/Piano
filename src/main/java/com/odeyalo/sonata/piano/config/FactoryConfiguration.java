package com.odeyalo.sonata.piano.config;

import com.odeyalo.sonata.piano.model.factory.DefaultUserFactory;
import com.odeyalo.sonata.piano.model.factory.UserFactory;
import com.odeyalo.sonata.piano.service.confirmation.*;
import com.odeyalo.sonata.piano.service.support.PasswordEncoder;
import com.odeyalo.sonata.piano.support.html.TemplateEngine;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FactoryConfiguration {

    @Bean
    public UserFactory userFactory(final PasswordEncoder passwordEncoder) {
        return new DefaultUserFactory(passwordEncoder);
    }

    @Bean
    public ConfirmationCodeFactory confirmationCodeFactory() {
        return new SimpleConfirmationCodeFactory();
    }

    @Bean
    public EmailConfirmationMessageTemplateFactory emailConfirmationMessageTemplateFactory(@NotNull TemplateEngine templateEngine) {
        return new HtmlEmailConfirmationMessageTemplateFactory(templateEngine);
    }
}
