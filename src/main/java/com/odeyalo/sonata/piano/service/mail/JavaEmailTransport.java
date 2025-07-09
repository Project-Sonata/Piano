package com.odeyalo.sonata.piano.service.mail;

import jakarta.mail.*;
import jakarta.mail.internet.MimeMessage;
import org.jetbrains.annotations.NotNull;
import org.springframework.mail.javamail.MimeMessageHelper;
import reactor.core.publisher.Mono;

import java.util.Properties;

public final class JavaEmailTransport implements EmailTransport {

    @Override
    @NotNull
    public Mono<Void> sendEmail(@NotNull final EmailMessage payload) {
        return Mono.fromRunnable(() -> {

            Properties props = new Properties();
            props.put("mail.smtp.host", "localhost");
            props.put("mail.smtp.port", "25");
            Session session = Session.getInstance(props, null);

            MimeMessage mimeMessage = new MimeMessage(session);
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage);

            try {
                message.setFrom("greetings@sonata.com");
                message.setTo(payload.to().asString());
                message.setSubject(payload.subject());
                message.setText(payload.body());

                Transport.send(mimeMessage, mimeMessage.getAllRecipients());
            } catch (final MessagingException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
