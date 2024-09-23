package com.odeyalo.sonata.piano.config;

import com.odeyalo.sonata.piano.service.registration.policy.BirthdatePolicy;
import com.odeyalo.sonata.piano.service.registration.policy.SimplePridicateBirthdatePolicy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UserRegistrationPolicyConfiguration {

    @Bean
    public BirthdatePolicy birthdatePolicy() {
        return new SimplePridicateBirthdatePolicy(
                birthdate -> birthdate.isOlderThan(13)
        );
    }
}
