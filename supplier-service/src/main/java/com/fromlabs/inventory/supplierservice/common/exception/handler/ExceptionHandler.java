/*
 * Copyright (c) 2021. Liem Doan
 */

package com.fromlabs.inventory.supplierservice.common.exception.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * Exception Handler are created to solve the issue which define a united way to handle
 * all type of exception while developer handle the process business logic
 * <p>
 * The simple ideas are that developer can define the strategy for solving each type of
 * exception just only once and the rest will follow the predefined rules
 * @param <T> A generic type
 */
public interface ExceptionHandler<T> {

    /**
     * Configuration is set up so that the strategy and rule are applied
     * to solve any predefined exception
     * @param cnf ExceptionHandlerConfiguration
     * @return ExceptionHandler
     * @see ExceptionHandler
     * @see ExceptionHandlerConfiguration
     */
    ExceptionHandler<T> config(ExceptionHandlerConfiguration cnf);

    /**
     * The main handler function for tackling the Exceptions
     * @param throwable an Exception
     * @return          ExceptionHandler
     * @see ExceptionHandler
     * @see Throwable
     */
    ExceptionHandler<T> handle(Throwable throwable);

    /**
     * Get response as generic type
     * @param throwable an Exception
     * @param <T>       a Generic type
     * @return T
     * @see Throwable
     */
    <T> T getResponse(Throwable throwable);

    /**
     * Get response as generic type with http status
     * @param throwable an Exception
     * @param status    HttpStatus
     * @param <T>       a Generic type
     * @return T
     * @see Throwable
     * @see HttpStatus
     */
    <T> T getResponse(Throwable throwable, HttpStatus status);

    /**
     * Get response as generic type with extra message
     * @param throwable an Exception
     * @param instruction    Extra instruction
     * @param <T>       a Generic type
     * @return T
     * @see Throwable
     */
    <T> T getResponse(Throwable throwable,String instruction);

    /**
     * Get response as generic type with extra message and http status
     * @param throwable an Exception
     * @param status    HttpStatus
     * @param instruction    Extra instruction
     * @param <T> a generic Datatype
     * @return T
     */
    <T> T getResponse(Throwable throwable, HttpStatus status, String instruction);

    /**
     * Get response to fetch error when an Exception occur
     * @param status    HttpStatus
     * @param info      Special wrapper of exception information
     * @return ResponseEntity
     * @see ResponseEntity
     * @see ExceptionInfo
     * @see HttpStatus
     */
    default ResponseEntity<?> response(HttpStatus status, ExceptionInfo info){
        return new ResponseEntity<>(info, status);};

    /**
     * Get response to fetch error when an Exception occur
     * @param info      Special wrapper of exception information
     * @return ExceptionInfo
     * @see ExceptionInfo
     * @see HttpStatus
     */
    default ExceptionInfo response(ExceptionInfo info){
        return info;
    }
}
