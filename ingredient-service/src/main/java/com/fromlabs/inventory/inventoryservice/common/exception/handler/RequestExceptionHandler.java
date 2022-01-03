/*
 * Copyright (c) 2021. Liem Doan
 */

package com.fromlabs.inventory.inventoryservice.common.exception.handler;


import com.fromlabs.inventory.inventoryservice.common.exception.FailTransactionException;
import com.fromlabs.inventory.inventoryservice.common.exception.NotImplementException;
import com.fromlabs.inventory.inventoryservice.common.exception.ObjectNotFoundException;
import com.fromlabs.inventory.inventoryservice.common.exception.RequestNotFoundException;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Set;

/**
 * Exception Handler are created to solve the issue which define a united way to handle
 * all type of exception while developer handle the process business logic
 * <p>
 * The simple ideas are that developer can define the strategy for solving each type of
 * exception just only once and the rest will follow the predefined rules
 * <p>
 * In this special case, the exception handler is mainly user for solve simple case
 * of predefined Exception in Services and Controllers
 * @see ExceptionHandler
 * @see ResponseEntity
 */
@Getter
@Slf4j
public class RequestExceptionHandler implements ExceptionHandler<ResponseEntity<?>> {
    public static RequestExceptionHandler ServerExceptionHandler() {
        return new RequestExceptionHandler();
    }

    protected Object handlerResult;
    protected ExceptionHandlerConfiguration configuration;
    protected Logger logger = LoggerFactory.getLogger(RequestExceptionHandler.class);

    /**
     * Configuration method
     * @param cnf ExceptionHandlerConfiguration
     * @return RequestExceptionHandler
     * @see ExceptionHandlerConfiguration
     */
    public final RequestExceptionHandler config(ExceptionHandlerConfiguration cnf) {
        this.logger.info("Configuration is initialize : ".concat(cnf.toString()));
        this.configuration = cnf;
        return this;
    }

    /**
     * Main operation for exception handler
     * @param throwable an Exception
     * @return RequestExceptionHandler
     * @see Throwable
     */
    public RequestExceptionHandler handle(Throwable throwable) {
        if(this.configuration == null){
            this.logger.info("The default configuration will be loaded");
            this.configByDefault();
        }
        return this.handleBaseOnExceptionType(throwable);
    }

    /**
     * Default configuration of exception handler
     * @see ExceptionHandlerConfiguration
     */
    protected void configByDefault() {
        this.configuration = new RequestExceptionHandlerConfiguration()
            .status(HttpStatus.BAD_REQUEST, FailTransactionException.class.getName())
                .instruction("There are bugs which you can find in 'Stack Trace' or 'Stack Frame'", FailTransactionException.class.getName())
            .status(HttpStatus.INTERNAL_SERVER_ERROR, NotImplementException.class.getName())
                .instruction("The called is not yet implemented. Please check in 'Stack Trace' or 'Stack Frame'", NotImplementException.class.getName())
            .status(HttpStatus.NOT_FOUND, ObjectNotFoundException.class.getName())
                .instruction("The requested object is either null or undefined in the runtime", ObjectNotFoundException.class.getName())
            .status(HttpStatus.BAD_REQUEST, RequestNotFoundException.class.getName())
                .instruction("The request body is missing lead to the transaction would be malfunctioned", RequestNotFoundException.class.getName());
        this.logger.info("Load default configuration : ".concat(this.configuration.toString()));
    }

    /**
     * Get response as generic type
     * @param throwable an Exception
     * @return ResponseEntity
     * @see Throwable
     * @see ResponseEntity
     */
    @Override
    public ResponseEntity<?> getResponse(Throwable throwable) {
        return this.handle(throwable).response(this.configuration.getStatus(throwable), ExceptionInfo.getInfo(throwable,
                this.configuration.getStatus(throwable),
                this.configuration.getInstruction(throwable),
                this.handlerResult));
    }

    /**
     * Get response as generic type with http status
     * @param throwable an Exception
     * @param status    HttpStatus
     * @return ResponseEntity
     * @see ResponseEntity
     * @see Throwable
     * @see HttpStatus
     */
    @Override
    public ResponseEntity<?> getResponse(Throwable throwable, HttpStatus status) {
        return this.handle(throwable).response(this.configuration.getStatus(throwable), ExceptionInfo.getInfo(throwable,
                status,
                this.configuration.getInstruction(throwable),
                this.handlerResult));
    }

    /**
     * Get response as generic type with extra message and http status
     * @param throwable an Exception
     * @param status    HttpStatus
     * @param instruction    Extra instruction
     * @return ResponseEntity
     * @see ResponseEntity
     * @see Throwable
     * @see HttpStatus
     */
    @Override
    public ResponseEntity<?> getResponse(Throwable throwable, HttpStatus status, String instruction) {
        return this.handle(throwable).response(this.configuration.getStatus(throwable), ExceptionInfo.getInfo(throwable,
                status,
                Set.of(instruction),
                this.handlerResult));
    }

    /**
     * Get response as generic type with extra message
     * @param throwable an Exception
     * @param instruction    Extra instruction
     * @return ResponseEntity
     * @see ResponseEntity
     * @see Throwable
     */
    @Override
    public ResponseEntity<?> getResponse(Throwable throwable, String instruction) {
        return this.handle(throwable).response(this.configuration.getStatus(throwable), ExceptionInfo.getInfo(throwable,
                this.configuration.getStatus(throwable),
                Set.of(instruction),
                this.handlerResult));
    }

    /**
     * Get raw error response
     * @param throwable an Exception
     * @return ExceptionInfo
     * @see Throwable
     * @see ExceptionInfo
     */
    public ExceptionInfo getRawResponse(Throwable throwable) {
        return this.handle(throwable).response(ExceptionInfo.getInfo(throwable,
                this.configuration.getStatus(throwable),
                this.configuration.getInstruction(throwable),
                this.handlerResult)
        );
    }

    /**
     * Categorized Exception to solve all predefined Exceptions
     * @param throwable an Exception
     * @return RequestExceptionHandler
     * @see RequestExceptionHandler
     * @see Throwable
     */
    protected RequestExceptionHandler handleBaseOnExceptionType(Throwable throwable) {
        if(throwable instanceof FailTransactionException){
            this.handlerResult = this.handleFailTransaction(throwable);
        }else if(throwable instanceof NotImplementException) {
            this.handlerResult = this.handleNotImplement(throwable);
        }else if(throwable instanceof ObjectNotFoundException){
            this.handlerResult = this.handleObjectNotFound(throwable);
        }else if(throwable instanceof RequestNotFoundException){
            this.handlerResult = this.handleRequestNotFound(throwable);
        }else {
            this.handlerResult = this.handleDefault(throwable);
        }
        return this;
    }

    /**
     * Handle request not found (override this method)
     * @param throwable an Exception
     * @return Object
     * @see Throwable
     */
    protected Object handleRequestNotFound(Throwable throwable) {
        // Replace this below line with a specific handle for this exception
        return null;
    }

    /**
     * Handle Object not found (override this method)
     * @param throwable an Exception
     * @return Object
     * @see Throwable
     */
    protected Object handleObjectNotFound(Throwable throwable) {
        // Replace this below line with a specific handle for this exception
        return null;
    }

    /**
     * Handle not implement (override this method)
     * @param throwable an Exception
     * @return Object
     * @see Throwable
     */
    protected Object handleNotImplement(Throwable throwable) {
        // Replace this below line with a specific handle for this exception
        return null;
    }

    /**
     * Handle fail transaction (override this method)
     * @param throwable an Exception
     * @return Object
     * @see Throwable
     */
    protected Object handleFailTransaction(Throwable throwable) {
        // Replace this below line with a specific handle for this exception
        return null;
    }

    /**
     * Handle default (override this method)
     * @param throwable an Exception
     * @return Object
     * @see Throwable
     */
    protected Object handleDefault(Throwable throwable) {
        // Replace this below line with a specific handle for this exception
        return null;
    }
}

