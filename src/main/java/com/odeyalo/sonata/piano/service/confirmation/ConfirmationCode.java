package com.odeyalo.sonata.piano.service.confirmation;

import com.odeyalo.sonata.piano.model.User;
import lombok.Builder;
import lombok.Value;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;

@Value
@Builder
public class ConfirmationCode {
     @NotNull
     String value;
     @NotNull
     Instant issuedAt;
     @NotNull
     Instant expiresIn;
     @NotNull
     User generatedFor;

     public boolean isExpired() {
          return Instant.now().isAfter(expiresIn);
     }
}
