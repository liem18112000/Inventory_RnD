/*
 * Copyright (c) 2021. Liem Doan
 */

package com.fromlabs.inventory.notificationservice.common.transaction;

import lombok.Builder;

import java.time.Instant;

public class RequestTransactionResult implements TransactionResult {

    protected Object finalResult;
    protected boolean success;
    protected String message;
    protected final String timestamp    = Instant.now().toString();
    protected String briefTemplate      = "Brief transaction result :\n\tResult : {}\n\tSuccess : {}\n\tMessage : {}\n\tTimestamp : {}";

    public RequestTransactionResult() {}

    @Builder
    public RequestTransactionResult(
            Object finalResult,
            boolean success,
            String message
    ) {
        this.finalResult = finalResult;
        this.success = success;
        this.message = message;
    }

    public Object getTransactionResult() {
        return finalResult;
    }

    public boolean isTransactionSuccess() {
        return success;
    }

    public Object getTransactionMessage() {
        return message;
    }

    public String brief() {
        return String.format(briefTemplate, finalResult, success, message, timestamp);
    }
}
