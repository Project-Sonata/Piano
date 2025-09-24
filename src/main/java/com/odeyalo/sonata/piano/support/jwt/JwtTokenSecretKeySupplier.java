package com.odeyalo.sonata.piano.support.jwt;

import javax.crypto.SecretKey;
import java.util.function.Supplier;

/**
 * Supply secret key for Jwt token only
 */
public interface JwtTokenSecretKeySupplier extends Supplier<SecretKey> {
}
