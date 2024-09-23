package com.odeyalo.sonata.piano.config;

import com.odeyalo.sonata.piano.model.factory.DefaultUserFactory;
import com.odeyalo.sonata.piano.model.factory.UserFactory;
import com.odeyalo.sonata.piano.service.confirmation.ConfirmationCodeFactory;
import com.odeyalo.sonata.piano.service.confirmation.EmailConfirmationMessageTemplateFactory;
import com.odeyalo.sonata.piano.service.confirmation.PlainTextEmailConfirmationMessageTemplateFactory;
import com.odeyalo.sonata.piano.service.confirmation.SimpleConfirmationCodeFactory;
import com.odeyalo.sonata.piano.service.support.PasswordEncoder;
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
    public EmailConfirmationMessageTemplateFactory emailConfirmationMessageTemplateFactory() {
        return new PlainTextEmailConfirmationMessageTemplateFactory();
    }
}
