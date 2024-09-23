package com.odeyalo.sonata.piano.service.confirmation;

import com.odeyalo.sonata.piano.model.Email;
import com.odeyalo.sonata.piano.service.mail.EmailMessage;
import org.jetbrains.annotations.NotNull;

public interface EmailConfirmationMessageTemplateFactory {


    @NotNull
    EmailMessage createEmailMessage(@NotNull final Email to,
                                    @NotNull final ConfirmationCode code);
}
