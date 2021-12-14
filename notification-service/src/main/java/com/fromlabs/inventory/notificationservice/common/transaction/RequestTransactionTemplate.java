/*
 * Copyright (c) 2021. Liem Doan
 */

package com.fromlabs.inventory.notificationservice.common.transaction;

import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.util.UUID;
import java.util.concurrent.Callable;

import static com.fromlabs.inventory.notificationservice.common.transaction.TransactionStatus.*;
import static java.util.Objects.isNull;

/**
 * <h1>Transaction</h1>
 * <h2>Definition</h2>
 * <p>
 *     Transaction processing is designed to maintain a system's Integrity
 *     (typically a database or some modern filesystems) in a known, consistent state, by ensuring that
 *     interdependent operations on the system are either all completed successfully or all canceled successfully.
 * </p>
 * <p>
 *     Transaction processing links multiple individual operations in a single, indivisible transaction,
 *     and ensures that either all operations in a transaction are completed without error,
 *     or none of them are. If some of the operations are completed but errors occur when the others are attempted,
 *     the transaction-processing system "rolls back" all of the operations of the transaction
 *     (including the successful ones), thereby erasing all traces of the transaction and restoring
 *     the system to the consistent, known state that it was in before processing of the transaction began.
 *     If all operations of a transaction are completed successfully, the transaction is committed by the system,
 *     and all changes to the database are made permanent; the transaction cannot be rolled back once this is done.
 * </p>
 * <h2>ACID criteria</h2>
 * <ul>
 *     <li>Atomicity - transaction's changes to the state are atomic: either all happen or none happen.
 *     These changes include database changes, message_queue, and actions on transducers.</li>
 *     <li>Consistency - transaction is a correct transformation of the state.
 *     The actions taken as a group do not violate any of the integrity constraints associated with the state.</li>
 *     <li>Isolation - Even though transactions execute concurrently, it appears to each transaction T,
 *     that others executed either before T or after T, but not both.</li>
 *     <li>Durability - Once a transaction completes successfully (commits), its changes to the
 *     database survive failures and retain its changes.</li>
 * </ul>
 * <h2>Pros and Cons</h2>
 * <h3>Advantages</h3>
 * <p>Transaction processing has these benefits:</p>
 * <ul>
 *     <li>It allows sharing of computer resources among many users</li>
 *     <li>It shifts the time of job processing to when the computing resources are less busy</li>
 *     <li>It avoids idling the computing resources without minute-by-minute human interaction and supervision</li>
 *     <li>It is used on expensive classes of computers to help amortize the cost by
 *     keeping high rates of utilization of those expensive resources</li>
 * </ul>
 *
 * <h3>Disadvantage</h3>
 * <p>Transaction processing has few disadvantages</p>
 * <ul>
 *     <li>They have relatively expensive setup costs</li>
 *     <li>There is a lack of standard formats</li>
 *     <li>Hardware and software incompatibility</li>
 * </ul>
 *
 * <h2>Programming description</h2>
 * <h3>Template main steps</h3>
 * <ul>
 *     <li>bootstrap : inti all data for transaction (optional)</li>
 *     <li>start : start a specific processing transaction (required)</li>
 *     <li>commit : commit a specific processing (required)</li>
 *     <li>rollBack :rollback transaction when any exception or error occurs (required)</li>
 * </ul>
 *
 * <h3>Strategy pattern</h3>
 * <p>The patten is applied to provide a high customization for the mechanism of
 * handle a single transaction. In detail, each step of transaction could be injected into
 * the properties of the transaction wrapper</p>
 *
 * <h3>Properties</h3>
 * <h4>bootstrapStrategy</h4>
 * <p> This property is applied as an Callable in Java Programming. It is used to inject the function
 * for the initial stage of the whole transaction. This property is optional as guard will skipped it
 * if it is not defined</p>
 *
 * <p>The exception in the bootstrap strategy activate the rollback mechanism</p>
 *
 * <h4>processStrategy</h4>
 * <p> This property is applied as an Callable in Java Programming. It is used to inject the function
 * for the main process transact. It is mandatory property so the lack of process strategy will cause
 * transaction status assigned VIOLATED and, hence, fail the transaction</p>
 *
 * <p>The exception in the process strategy activate the rollback mechanism</p>
 *
 * <h4>commitStrategy</h4>
 *
 * <p> This property is applied as an Callable in Java Programming. It is used to inject the function
 * for the committing the success transaction . It is mandatory property so the lack of process strategy
 * will cause transaction status assigned VIOLATED and, hence, fail the transaction</p>
 *
 * <p> This commit strategy must not be failed as it cause a severe damage (data inconsistency).
 * The error caused by commit strategy may be logged down to storage for later consideration</p>
 *
 * <h4>rollbackStrategy</h4>
 * <p> This property is applied as an Callable in Java Programming. It is used to inject the function
 * for the rollback mechanism when rollback flag is activated and transaction status is FAILED.
 * It is mandatory property so the lack of process strategy will cause
 * transaction status assigned VIOLATED and, hence, fail the transaction</p>
 *
 * <p> This rollback strategy must not be failed as it cause a severe damage (data inconsistency).
 * The error caused by rollback strategy may be logged down to storage for later consideration</p>
 */
@Slf4j
public class RequestTransactionTemplate<ID extends Serializable> implements TransactionTemplate {

    protected final Callable<?>         bootstrapStrategy;
    protected final Callable<?>         processStrategy;
    protected final Callable<?>         rollBackStrategy;
    protected final Callable<?>         commitStrategy;

    @Getter protected TransactionStatus status;
    @Getter protected String            transactId;
    @Getter protected String            transactName    = "Transact";

    public static final String          VIOLATE_MSG     = "Start process or rollback process is not initialized";
    public static final String          STATUS_MSG      = "Transaction ( step : {} , status : {})";

    /**
     * Trace out the current status of transaction template
     * @param step      Template step
     * @param status    Transaction status
     */
    protected void trace(String step, TransactionStatus status) {
        log.info(STATUS_MSG, step, status.name());
    }

    @Builder
    public RequestTransactionTemplate(
            String transactName,
            Callable<?> bootstrapStrategy,
            Callable<?> processStrategy,
            Callable<?> rollBackStrategy,
            Callable<?> commitStrategy)
    {
        // Assign strategies for all template process functions
        this.transactName       = transactName;
        this.bootstrapStrategy  = bootstrapStrategy;
        this.processStrategy    = processStrategy;
        this.rollBackStrategy   = rollBackStrategy;
        this.commitStrategy     = commitStrategy;

        // Assign initial values for transaction information
        this.status             = INITIAL;
        this.transactId         = String.valueOf(UUID.randomUUID());
    }

    /**
     * First step - optional: Initial commit state and bootstrap some information about transaction
     * @return Object
     */
    @Override
    public Object bootstrap() {
        try{

            // Set up the first state for transaction and log it out
            this.status = INITIAL;
            this.trace("boostrap", this.status);

            // The bootstrap stage will be skipped if the strategy for them is not initialized
            if(isNull(this.bootstrapStrategy)){
                log.warn("Bootstrap skipped");
                return null;
            }

            // Activate the bootstrap if the strategy is initialized
            return this.bootstrapStrategy.call();
        } catch (Exception exception) {

            // Turn on failed status for any exception and log out the exception
            this.status = FAILED;
            this.trace("boostrap", this.status);
            exception.printStackTrace();

            // Return null result as a sign for fail boostrap
            return null;
        }
    }

    /**
     * Second step - required: Transaction manager start the process.
     * @return Object
     */
    @Override
    public Object start(){
        try {

            // Check whether violation may exist
            if(isTransactionViolated()){

                // Log out error message and set VIOLATED status for transaction
                log.error(VIOLATE_MSG);
                this.status = VIOLATED;

                // Return null as a sign for violation in transaction
                return null;
            }

            // Process start if there is no violation and log them out
            this.status = PROCESSING;
            this.trace("start", this.status);

            // Running transaction process and return response
            return this.processStrategy.call();

        } catch (Exception exception) {

            // Turn on failed status for any exception and log out the exception
            this.status = FAILED;
            this.trace("start", this.status);
            exception.printStackTrace();

            // Return null result as a sign for fail process
            return null;
        }
    }

    /**
     * Third step - required: commit state on transaction processing
     * @return Object
     */
    @Override
    public Object commit() {
        try{

            // Activate COMMITTING status for transaction and log them out
            this.status = COMMITTING;
            trace("commit", this.status);

            // If custom strategy is not initialized, system run default commit process
            if(isNull(this.commitStrategy)) return null;

            // Return response from custom commit process
            return this.commitStrategy.call();

        } catch (Exception exception) {

            // Turn on failed status for any exception and log out the exception
            this.status = FAILED;
            this.trace("commit", this.status);
            exception.printStackTrace();

            // Return null result as a sign for fail commit
            return null;
        }

    }

    /**
     * Forth step - required: if there is any exception or error, the roll back is activated
     * @return Object
     */
    @Override
    public Object rollBack() {

        // Check whether violation may exist
        if(isTransactionViolated()){

            // Log out error message and set VIOLATED status for transaction
            log.error("Start process or rollback process is not initialized");
            this.status = VIOLATED;

            // Return null as a sign for violation in transaction
            return null;
        }
        try {

            // Rollback is activated due to FAILED transaction status
            log.error("Rollback activate");

            // Activate rollback mechanism with strategy
            return this.rollBackStrategy.call();

        } catch (Exception exception) {

            // Turn on failed status for any exception and log out the exception
            this.status = FAILED;
            this.trace("rollback", this.status);
            log.error("System is suffered from server damage - log failed rollback transaction : {} - {}",
                    this.getTransactId(), this.transactName);
            exception.printStackTrace();

            // Return null result as a sign for fail rollback
            return null;
        }
    }

    /**
     * Check whether the transaction is violated or not
     * @return Boolean flag for transaction violation (return true if it is violated)
     */
    protected boolean isTransactionViolated() {
        return isNull(this.processStrategy) || isNull(this.rollBackStrategy);
    }
}
