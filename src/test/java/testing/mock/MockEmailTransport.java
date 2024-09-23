package testing.mock;

import com.odeyalo.sonata.piano.service.mail.EmailMessage;
import com.odeyalo.sonata.piano.service.mail.EmailTransport;
import org.jetbrains.annotations.NotNull;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Simple mock for unit tests
 */
public class MockEmailTransport implements EmailTransport {
    private final List<EmailMessage> sentMessages = new CopyOnWriteArrayList<>();

    @Override
    @NotNull
    public Mono<Void> sendEmail(@NotNull final EmailMessage message) {
        return Mono.fromRunnable(() -> sentMessages.add(message));
    }

    @NotNull
    public List<EmailMessage> getSentMessages() {
        return sentMessages;
    }

    @NotNull
    public EmailMessage getFirstMessage() {
        return sentMessages.get(0);
    }
}
