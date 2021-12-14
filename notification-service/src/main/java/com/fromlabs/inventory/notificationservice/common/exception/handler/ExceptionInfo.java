/*
 * Copyright (c) 2021. Liem Doan
 */

package com.fromlabs.inventory.notificationservice.common.exception.handler;

import lombok.Builder;
import lombok.Getter;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.http.HttpStatus;

import java.time.Instant;
import java.util.Objects;
import java.util.Set;

/**
 * Default Exception handler for Exception Handler
 * @see ExceptionHandler
 */
@Getter
@Builder
public class ExceptionInfo {
    private String exceptionName;
    private String fullExceptionName;
    private String message;
    private Set<String> instruction;
    private int httpCode;
    private String[] stackFrame;
    private String httpReason;
    private String handleResult;
    private Instant timestamp;

    /**
     * Generate info for exception information wrapper
     * @param throwable     an Exception
     * @param status        HttpStatus
     * @param instruction   Set&lt;String&gt;
     * @param handlerResult Object
     * @return ExceptionInfo
     * @see Throwable
     * @see HttpStatus
     */
    static public ExceptionInfo getInfo(Throwable throwable, HttpStatus status, Set<String> instruction, Object handlerResult){
        return ExceptionInfo.builder()
                .exceptionName(throwable.getClass().getSimpleName())
                .fullExceptionName(throwable.getClass().getName())
                .httpCode(status.value())
                .httpReason(status.getReasonPhrase())
                .message(ExceptionUtils.getMessage(throwable))
                .instruction(instruction)
                .stackFrame(ExceptionUtils.getStackFrames(throwable))
                .handleResult(Objects.isNull(handlerResult)? "NA" : String.valueOf(handlerResult))
                .timestamp(Instant.now())
                .build();
    }
}
