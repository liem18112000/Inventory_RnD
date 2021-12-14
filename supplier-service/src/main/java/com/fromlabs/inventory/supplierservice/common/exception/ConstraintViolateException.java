/*
 * Copyright (c) 2021. Liem Doan
 */

package com.fromlabs.inventory.supplierservice.common.exception;

/**
 * Exception is throw when the any kind of constrain is violated
 */
public class ConstraintViolateException extends Exception{
    public ConstraintViolateException() {
    }

    public ConstraintViolateException(String message) {
        super(message);
    }

    public ConstraintViolateException(String message, Throwable cause) {
        super(message, cause);
    }

    public ConstraintViolateException(Throwable cause) {
        super(cause);
    }

    public ConstraintViolateException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
