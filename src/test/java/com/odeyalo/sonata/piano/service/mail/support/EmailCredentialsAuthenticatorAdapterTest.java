package com.odeyalo.sonata.piano.service.mail.support;

import jakarta.mail.PasswordAuthentication;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class EmailCredentialsAuthenticatorAdapterTest {

    @Test
    void shouldUseProvidedCredentials() {
        final EmailCredentialsAuthenticatorAdapter testable = new EmailCredentialsAuthenticatorAdapter(
                new EmailCredentials("hello@gmail.com", "coolpassword")
        );

        final PasswordAuthentication authentication = testable.getPasswordAuthentication();

        assertThat(authentication).isNotNull();
        assertThat(authentication.getUserName()).isEqualTo("hello@gmail.com");
        assertThat(authentication.getPassword()).isEqualTo("coolpassword");
    }
}