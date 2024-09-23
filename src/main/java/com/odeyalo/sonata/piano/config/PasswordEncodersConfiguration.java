package com.odeyalo.sonata.piano.config;

import com.odeyalo.sonata.piano.service.support.PasswordEncoder;
import com.odeyalo.sonata.piano.service.support.TestingPasswordEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PasswordEncodersConfiguration {


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new TestingPasswordEncoder();
    }

}
