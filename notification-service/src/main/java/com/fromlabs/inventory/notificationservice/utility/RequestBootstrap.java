package com.fromlabs.inventory.notificationservice.utility;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

/**
 * Request bootstrap layer
 */
@Slf4j
@UtilityClass
public class RequestBootstrap {

    /**
     * Log wrapper
     * @param result    Object
     * @param message   Log message
     * @return  boolean
     */
    private Object logWrapper(Object result, String message) {
        log.info(message, result);
        return result;
    }
}
