package com.odeyalo.sonata.piano.config;

import com.odeyalo.sonata.piano.service.mail.EmailTransport;
import com.odeyalo.sonata.piano.service.mail.JavaEmailTransport;
import com.odeyalo.sonata.piano.service.mail.support.DefaultSmtpSessionFactory;
import com.odeyalo.sonata.piano.service.mail.support.EmailCredentials;
import com.odeyalo.sonata.piano.service.mail.support.LocalSmtpSessionFactory;
import com.odeyalo.sonata.piano.service.mail.support.SmtpSessionFactory;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class EmailConfiguration {
    private final Logger logger = LoggerFactory.getLogger(EmailConfiguration.class);

    @Bean
    @Profile({"prod"})
    public SmtpSessionFactory smtpSessionFactory(@Value("${sonata.mail.username}") final String username,
                                                 @Value("${sonata.mail.password}") final String password
    ) {
        return DefaultSmtpSessionFactory.gmail(
                new EmailCredentials(username, password)
        );
    }

    @Bean
    @Profile({"dev", "local", "test"})
    public SmtpSessionFactory localSmtpSessionFactory() {
        return new LocalSmtpSessionFactory();
    }

    @Bean
    @Profile({"prod"})
    public EmailTransport emailTransport(@NotNull final SmtpSessionFactory smtpSessionFactory) {
        logger.info("A JavaEmailTransport with {} will be used to send email messages", smtpSessionFactory);
        return new JavaEmailTransport(smtpSessionFactory);
    }
}
