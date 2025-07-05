package com.odeyalo.sonata.piano.config;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ContentConversionConfiguration {

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer simpleCustomizer() {
        return (mapperBuilder) -> {
            mapperBuilder
                    .featuresToEnable(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS)
                    .modulesToInstall(new JavaTimeModule(), new ParameterNamesModule());
        };
    }

}
