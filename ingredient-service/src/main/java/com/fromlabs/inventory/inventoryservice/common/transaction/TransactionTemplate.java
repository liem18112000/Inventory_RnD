/*
 * Copyright (c) 2021. Liem Doan
 */

package com.fromlabs.inventory.inventoryservice.common.transaction;

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
 *     These changes include database changes, messages, and actions on transducers.</li>
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
 * <h3>Disadvantage</h3>
 * <p>Transaction processing has few disadvantages</p>
 * <ul>
 *     <li>They have relatively expensive setup costs</li>
 *     <li>There is a lack of standard formats</li>
 *     <li>Hardware and software incompatibility</li>
 * </ul>
 * <h2>Programming description</h2>
 * <h3>Template main steps</h3>
 * <ul>
 *     <li>bootstrap : inti all data for transaction (optional)</li>
 *     <li>start : start a specific processing transaction (required)</li>
 *     <li>commit : commit a specific processing (required)</li>
 *     <li>rollBack :rollback transaction when any exception or error occurs (required)</li>
 * </ul>
 */
public interface TransactionTemplate {

    /**
     * First step - optional: Initial commit state and bootstrap some information about transaction
     * @return Object
     */
    Object bootstrap();

    /**
     * Second step - required: Transaction manager start the process.
     * @return  Object
     */
    Object start();

    /**
     * Third step - required: commit state on transaction processing
     * @return  Object
     */
    Object commit();

    /**
     * Forth step - required: if there is any exception or error, the roll back is activated
     * @return  Object
     */
    Object rollBack();
}
