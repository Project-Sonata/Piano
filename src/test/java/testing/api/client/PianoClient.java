package testing.api.client;

import com.odeyalo.sonata.piano.api.exchange.dto.RegistrationFormDto;
import com.odeyalo.sonata.piano.api.exchange.dto.RegistrationResponseDto;
import org.jetbrains.annotations.NotNull;

public interface PianoClient {

    @NotNull
    RegistrationResponseDto sendRegistrationForm(@NotNull RegistrationFormDto form);

}
