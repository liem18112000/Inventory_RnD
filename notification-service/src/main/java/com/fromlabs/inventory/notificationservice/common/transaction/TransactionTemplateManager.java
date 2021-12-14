/*
 * Copyright (c) 2021. Liem Doan
 */

package com.fromlabs.inventory.notificationservice.common.transaction;

import java.io.Serializable;
import java.util.Stack;

/**
 * <h1>Transaction template manager </h1>
 *
 * <h2>Brief information</h2>
 *
 * <p>
 *     Transaction template manager is applied to handle multiple template transaction.
 *     Thw management mechanism include a stack of rollback transaction to handle rollbacks
 *     when there is any FAILED transaction in the pipeline
 * </p>
 * @param <ID> Template transaction entity ID
 */
public interface TransactionTemplateManager<ID extends Serializable> {

    /**
     * Main transaction with standard workflow
     * @return The response of final transaction
     */
    Object transact();

    /**
     * Summary all transaction pipeline and information
     */
    void summary();

    //<editor-fold desc="TRANSACTION TEMPLATES">

    /**
     * Processing transaction
     * @param trx   RequestTransactionTemplate
     * @return      The response of the transaction
     */
    Object processingTransact(
            RequestTransactionTemplate<ID> trx
    );

    /**
     * Bootstrap transaction
     * @param trx   RequestTransactionTemplate
     * @return      The bootstrap result
     */
    Object bootstrapTransact(
            RequestTransactionTemplate<ID> trx
    );

    /**
     * Committing transaction
     * @param trx       RequestTransactionTemplate
     * @param trxResult Transaction response object
     */
    void commitTransact(
            RequestTransactionTemplate<ID> trx,
            Object trxResult
    );

    /**
     * Rollback all transactions in the stack of rollback
     * @param rollBackStack Roll back stack
     * @param trx           RequestTransactionTemplate
     */
    void rollBack(
            Stack<RequestTransactionTemplate<ID>> rollBackStack,
            RequestTransactionTemplate<ID> trx
    );

    //</editor-fold>
}
