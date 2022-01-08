package com.fromlabs.inventory.apisecurity.token.service.matchstrategy;

/**
 * Internal API key match strategy
 */
public interface InternalAPIKeyMatchStrategy {

    /**
     * Check whether an API is match to a provided value
     * @param apiKey API key
     * @param comparedValue Compared value
     * @return true if it is a match. Otherwise, false
     */
    boolean isMatch(Object apiKey, Object comparedValue);
}
