package testing.mock;

import com.odeyalo.sonata.piano.model.Email;
import com.odeyalo.sonata.piano.service.confirmation.ConfirmationCode;
import com.odeyalo.sonata.piano.service.confirmation.EmailConfirmationMessageTemplateFactory;
import com.odeyalo.sonata.piano.service.mail.EmailMessage;
import org.jetbrains.annotations.NotNull;

public final class PrefixBodyEmailConfirmationMessageTemplateFactory implements EmailConfirmationMessageTemplateFactory {
    private final String bodyPrefix;

    public PrefixBodyEmailConfirmationMessageTemplateFactory(final String bodyPrefix) {
        this.bodyPrefix = bodyPrefix;
    }

    @Override
    public @NotNull EmailMessage createEmailMessage(final @NotNull Email to, final @NotNull ConfirmationCode code) {
        return EmailMessage.builder()
                .to(to)
                .body(bodyPrefix + code.value())
                .subject("Your confirmation code for Sonata")
                .html(false)
                .build();
    }
}
