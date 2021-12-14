/*
 * Copyright (c) 2021. Liem Doan
 */

package com.fromlabs.inventory.supplierservice.common.exception;

/**
 * Exception is thrown when the feature or function is not yet implemented
 */
public class NotImplementException extends Exception{
    public NotImplementException() {
    }

    public NotImplementException(String message) {
        super(message);
    }

    public NotImplementException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotImplementException(Throwable cause) {
        super(cause);
    }

    public NotImplementException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
