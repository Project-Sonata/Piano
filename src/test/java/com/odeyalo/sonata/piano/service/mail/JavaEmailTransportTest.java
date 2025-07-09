package com.odeyalo.sonata.piano.service.mail;

import com.odeyalo.sonata.piano.model.Email;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import name.bychkov.junit5.FakeSmtpJUnitExtension;
import org.assertj.core.api.InstanceOfAssertFactories;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.io.IOException;
import java.time.Duration;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class JavaEmailTransportTest {

    @RegisterExtension
    static FakeSmtpJUnitExtension smtpServer = new FakeSmtpJUnitExtension();

    @Test
    void shouldSendEmailToSmtpServer() throws MessagingException, IOException {
        // given
        final JavaEmailTransport testable = new JavaEmailTransport();

        // when
        testable.sendEmail(
                EmailMessage.of(
                        Email.valueOf("cooluser@mail.com"),
                        "Greetings!",
                        "Welcome to our platform!",
                        false
                )
        ).block(Duration.ofSeconds(30));


        // then
        final List<MimeMessage> receivedMessages = smtpServer.getMessages();
        assertThat(receivedMessages).hasSize(1);

        final MimeMessage receivedMessage = receivedMessages.get(0);

        assertThat(receivedMessage.getAllRecipients())
                .hasSize(1)
                .allSatisfy(address -> assertThat(address).isEqualTo(new InternetAddress("cooluser@mail.com")));

        assertThat(receivedMessage.getSubject()).isEqualTo("Greetings!");
        assertThat(receivedMessage.getContent()).isEqualTo("Welcome to our platform!\r\n");
    }
}