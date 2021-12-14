package com.fromlabs.inventory.supplierservice.common.exception;

/**
 * Exception is thrown when the validation is failed for any context
 */
public class FailValidateException extends Exception{
    public FailValidateException() {
    }

    public FailValidateException(String message) {
        super(message);
    }

    public FailValidateException(String message, Throwable cause) {
        super(message, cause);
    }

    public FailValidateException(Throwable cause) {
        super(cause);
    }

    public FailValidateException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
