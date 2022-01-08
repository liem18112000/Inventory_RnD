package com.fromlabs.inventory.apisecurity.token.service.matchstrategy;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * {@inheritDoc}
 */
public class BcryptBasedMatchStrategy implements InternalAPIKeyMatchStrategy {

    protected final BCryptPasswordEncoder encoder;

    public BcryptBasedMatchStrategy(BCryptPasswordEncoder encoder) {
        this.encoder = encoder;
    }

    public BcryptBasedMatchStrategy() {
        this.encoder = new BCryptPasswordEncoder();
    }

    /**
     * Check whether an API is match to a provided value
     *
     * @param apiKey        API key
     * @param comparedValue Compared value
     * @return true if it is a match. Otherwise, false
     */
    @Override
    public boolean isMatch(Object apiKey, Object comparedValue) {
        final String apiKeyString = String.valueOf(apiKey);
        final String comparedValueString = String.valueOf(comparedValue);
        return this.encoder.matches(apiKeyString, comparedValueString);
    }
}
