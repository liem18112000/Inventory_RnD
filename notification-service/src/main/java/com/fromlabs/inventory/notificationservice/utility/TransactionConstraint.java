/*
 * Copyright (c) 2021. Liem Doan
 */

package com.fromlabs.inventory.notificationservice.utility;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;


/**
 * Transaction Constraints layers
 */
@UtilityClass
@Slf4j
public class TransactionConstraint {

    /**
     * Log wrapper
     * @param result    boolean
     * @param message   Log message
     * @return          boolean
     */
    private boolean logWrapper(boolean result, String message) {
        log.info(message, result);
        return result;
    }
}
