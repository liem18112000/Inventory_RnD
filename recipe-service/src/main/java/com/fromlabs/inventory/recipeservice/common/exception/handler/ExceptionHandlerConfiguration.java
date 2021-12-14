package com.fromlabs.inventory.recipeservice.common.exception.handler;

import org.springframework.http.HttpStatus;

import java.util.Set;

/**
 * Base Configuration for Exception handler
 * @see ExceptionHandler
 * @see HttpStatus
 * @see Throwable
 */
public interface ExceptionHandlerConfiguration {
    /**
     * ExceptionHandlerConfiguration status
     * @param sts           HttpStatus
     * @param exceptionName String of Exception name
     * @return ExceptionHandlerConfiguration
     * @see HttpStatus
     */
    ExceptionHandlerConfiguration status(HttpStatus sts, String exceptionName);

    /**
     * ExceptionHandlerConfiguration instruction
     * @param ins String of instruction
     * @param exceptionName String of Exception name
     * @return ExceptionHandlerConfiguration
     */
    ExceptionHandlerConfiguration instruction(String ins, String exceptionName);

    /**
     * ExceptionHandlerConfiguration status
     * @param sts           HttpStatus
     * @param throwable     an Exception
     * @return ExceptionHandlerConfiguration
     */
    ExceptionHandlerConfiguration status(HttpStatus sts, Throwable throwable);

    /**
     *
     * @param ins String of instruction
     * @param throwable     an Exception
     * @return ExceptionHandlerConfiguration
     * @see Throwable
     */
    ExceptionHandlerConfiguration instruction(String ins, Throwable throwable);

    /**
     * getStatus with throwable
     * @param throwable     an Exception
     * @return HttpStatus
     */
    HttpStatus getStatus(Throwable throwable);

    /**
     * getInstruction with throwable
     * @param throwable     an Exception
     * @return Set&lt;String&gt;
     */
    Set<String> getInstruction(Throwable throwable);

    /**
     * getStatus with exceptionName
     * @param exceptionName String of Exception name
     * @return HttpStatus
     * @see HttpStatus
     */
    HttpStatus getStatus(String exceptionName);

    /**
     * getInstruction with exceptionName
     * @param exceptionName String of Exception name
     * @return Set&lt;String&gt;
     */
    Set<String> getInstruction(String exceptionName);
}
