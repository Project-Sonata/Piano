package com.odeyalo.sonata.piano.service.support;

import org.jetbrains.annotations.NotNull;

public interface PasswordEncoder {

    /**
     * Encode the raw password.
     * @param password password to encode
     * @return  encoded password
     */
    @NotNull
    String encode(@NotNull CharSequence password);

    /**
     * Verify the encoded password obtained from storage matches
     * the submitted raw password after it too is encoded.
     * The stored password itself is never decoded.
     *
     * @param password the raw password to encode and match
     * @param encodedPassword the encoded password from storage to compare with
     * @return true if the passwords match, false if they do not
     */
    boolean matches(@NotNull CharSequence password, @NotNull String encodedPassword);

}
