package com.fromlabs.inventory.apisecurity.token.service.hashstrategy;

import io.sentry.Sentry;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.StringUtils;

/**
 * Bcrypt-based hash strategy
 * @author Liem
 */
public class BcryptHashStrategy implements InternalAPIKeyHashStrategy {

    @Override
    public String hash(String rawValue) throws IllegalArgumentException {
        final var encoder = new BCryptPasswordEncoder();
        if(!StringUtils.hasText(rawValue)) {
            Sentry.captureMessage("Value need to be hashed is blank");
            throw new IllegalArgumentException("Value need to be hashed is blank");
        }
        return encoder.encode(rawValue);
    }
}
