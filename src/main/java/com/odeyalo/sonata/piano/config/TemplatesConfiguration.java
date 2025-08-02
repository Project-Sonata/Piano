package com.odeyalo.sonata.piano.config;

import com.odeyalo.sonata.piano.support.html.ThymeleafTemplateEngine;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TemplatesConfiguration {

    @Bean
    public ThymeleafTemplateEngine thymeleafTemplateEngine(org.thymeleaf.TemplateEngine templateEngine) {
        return new ThymeleafTemplateEngine(templateEngine);
    }
}
