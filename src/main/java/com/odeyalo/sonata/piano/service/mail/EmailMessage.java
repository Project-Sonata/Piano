package com.odeyalo.sonata.piano.service.mail;

import com.odeyalo.sonata.piano.model.Email;
import lombok.Builder;
import lombok.Value;
import org.jetbrains.annotations.NotNull;

/**
 * Represents an email message with the necessary details for sending.
 * Note, that this class is immutable and can't be changed after creation!
 * <p>
 * This class holds the key information required to compose and send an email,
 * including the recipient's email address, subject, and the message body, etc.
 * </p>
 *
 * <p>
 * To make the {@link EmailMessage} an HTML message,
 * set the {@link #html} flag to {@code true}.
 * By default, HTML mode is set to {@code false}
 * </p>
 */
@Value
@Builder
public class EmailMessage {
    @NotNull
    Email to;
    @NotNull
    String subject;
    @NotNull
    String body;

    boolean html;
}
