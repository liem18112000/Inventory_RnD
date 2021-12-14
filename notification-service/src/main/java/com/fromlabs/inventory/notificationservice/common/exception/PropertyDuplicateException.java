/*
 * Copyright (c) 2021. Liem Doan
 */

package com.fromlabs.inventory.notificationservice.common.exception;

/**
 * Exception is thrown when the property of attribute which are unique is duplicated by other object
 */
public class PropertyDuplicateException extends Exception {

    public PropertyDuplicateException() {
    }

    public PropertyDuplicateException(String message) {
        super(message);
    }

    public PropertyDuplicateException(String message, Throwable cause) {
        super(message, cause);
    }

    public PropertyDuplicateException(Throwable cause) {
        super(cause);
    }

    public PropertyDuplicateException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
