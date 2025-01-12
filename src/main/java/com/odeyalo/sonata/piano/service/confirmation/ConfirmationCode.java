package com.odeyalo.sonata.piano.service.confirmation;

import com.odeyalo.sonata.piano.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;

@Value
@Builder
@AllArgsConstructor
public class ConfirmationCode {
     @NotNull
     String value;
     @NotNull
     Instant issuedAt;
     @NotNull
     Instant expiresIn;
     @NotNull
     User generatedFor;

     public ConfirmationCode(@NotNull final String value,
                             @NotNull final User generatedFor) {
          this.value = value;
          this.issuedAt = Instant.now();
          this.expiresIn = Instant.now().plusSeconds(360);
          this.generatedFor = generatedFor;
     }

     public boolean isExpired() {
          return Instant.now().isAfter(expiresIn);
     }
}
