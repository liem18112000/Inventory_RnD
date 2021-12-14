/*
 * Copyright (c) 2021. Liem Doan
 */

package com.fromlabs.inventory.recipeservice.common.exception;

/**
 * Exception is thrown when the C.R.U.D or any type of other transact is failed
 */
public class FailTransactionException extends Exception{
    public FailTransactionException() {
    }

    public FailTransactionException(String message) {
        super(message);
    }

    public FailTransactionException(String message, Throwable cause) {
        super(message, cause);
    }

    public FailTransactionException(Throwable cause) {
        super(cause);
    }

    public FailTransactionException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
