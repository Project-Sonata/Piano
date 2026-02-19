package testing.api.client;

import com.odeyalo.sonata.piano.api.dto.EmailPasswordLoginRequestDto;
import com.odeyalo.sonata.piano.api.exchange.dto.RegistrationFormDto;
import com.odeyalo.sonata.piano.api.exchange.dto.RegistrationResponseDto;
import org.jetbrains.annotations.NotNull;
import org.springframework.test.web.reactive.server.WebTestClient;

public interface PianoClient {

    @NotNull
    RegistrationResponseDto sendRegistrationForm(@NotNull final RegistrationFormDto form);

    @NotNull
    WebTestClient.ResponseSpec login(@NotNull final EmailPasswordLoginRequestDto loginRequest);

}
