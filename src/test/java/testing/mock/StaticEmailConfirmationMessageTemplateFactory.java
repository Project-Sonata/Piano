package testing.mock;

import com.odeyalo.sonata.piano.model.Email;
import com.odeyalo.sonata.piano.service.confirmation.ConfirmationCode;
import com.odeyalo.sonata.piano.service.confirmation.EmailConfirmationMessageTemplateFactory;
import com.odeyalo.sonata.piano.service.mail.EmailMessage;
import org.jetbrains.annotations.NotNull;

public final class StaticEmailConfirmationMessageTemplateFactory implements EmailConfirmationMessageTemplateFactory {
    private final EmailMessage stub;

    public StaticEmailConfirmationMessageTemplateFactory(final EmailMessage stub) {
        this.stub = stub;
    }

    @Override
    public @NotNull EmailMessage createEmailMessage(final @NotNull Email to,
                                                    final @NotNull ConfirmationCode code) {
        return stub;
    }
}
