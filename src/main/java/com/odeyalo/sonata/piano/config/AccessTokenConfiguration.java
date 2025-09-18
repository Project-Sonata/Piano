package com.odeyalo.sonata.piano.config;

import com.odeyalo.sonata.piano.support.jwt.JwtTokenSecretKeySupplier;
import com.odeyalo.sonata.piano.support.jwt.StaticJwtTokenSecretKeySupplier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;

@Configuration
public class AccessTokenConfiguration {

    @Bean
    public JwtTokenSecretKeySupplier jwtTokenSecretKeySupplier() throws NoSuchAlgorithmException {
        final KeyGenerator generator = KeyGenerator.getInstance("HmacSHA256");
        generator.init(256);
        final SecretKey secretKey = generator.generateKey();

        return StaticJwtTokenSecretKeySupplier.withStaticValue(secretKey);
    }
}
