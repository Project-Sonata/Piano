package com.odeyalo.sonata.piano.service.mail;

import com.odeyalo.sonata.piano.service.mail.support.SmtpSessionFactory;
import jakarta.mail.*;
import jakarta.mail.internet.MimeMessage;
import org.jetbrains.annotations.NotNull;
import org.springframework.mail.javamail.MimeMessageHelper;
import reactor.core.publisher.Mono;

import java.util.Properties;

public final class JavaEmailTransport implements EmailTransport {
    private final SmtpSessionFactory smtpSessionFactory;

    public JavaEmailTransport(final SmtpSessionFactory smtpSessionFactory) {
        this.smtpSessionFactory = smtpSessionFactory;
    }

    @Override
    @NotNull
    public Mono<Void> sendEmail(@NotNull final EmailMessage payload) {
        return smtpSessionFactory.getSession().handle((session, sink) -> {

            final MimeMessage mimeMessage = new MimeMessage(session);
            final MimeMessageHelper message = new MimeMessageHelper(mimeMessage);

            try {
                message.setFrom("greetings@sonata.com");
                message.setTo(payload.to().asString());
                message.setSubject(payload.subject());

                if ( payload.html() ) {
                    mimeMessage.setContent(payload.body(), "text/html; charset=utf-8");
                } else {
                    mimeMessage.setContent(payload.body(), "text/plain; charset=utf-8");
                }

                Transport.send(mimeMessage, mimeMessage.getAllRecipients());
            } catch (final MessagingException e) {
                sink.error(new RuntimeException(e));
            }
        });
    }
}
