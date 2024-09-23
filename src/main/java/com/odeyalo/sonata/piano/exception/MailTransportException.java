package com.odeyalo.sonata.piano.exception;

import lombok.experimental.StandardException;

/**
 * Exception thrown when there is a failure during the email transport process.
 * <p>
 * This exception indicates that an error occurred while attempting to send an email,
 * such as network issues, invalid email configurations, or failures in the underlying
 * email transport mechanism.
 * </p>
 */
@StandardException
public class MailTransportException extends RuntimeException {
}
