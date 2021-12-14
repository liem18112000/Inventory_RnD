/*
 * Copyright (c) 2021. Liem Doan
 */

package com.fromlabs.inventory.inventoryservice.common.transaction;

import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import static com.fromlabs.inventory.inventoryservice.common.transaction.TransactionStatus.*;
import static java.util.Objects.nonNull;

@Data
@Slf4j
public class TransactionGuard<ID extends Serializable> implements TransactionTemplateManager<ID> {

    //<editor-fold desc="SETUP">

    protected List<RequestTransactionTemplate<ID>> transactions = new ArrayList<>();
    protected boolean transactSuccess = true;

    public static final String SUCCESS_TRANSACT_MSG = "Transactions are success and committed";
    public static final String FAILED_TRANSACT_MSG  = "System was roll backed due to a failed transaction";
    public static final String START_TRANSACT_MSG   = "Transaction Guard mechanism activated ...";
    public static final String END_TRANSACT_MSG     = "Transaction Guard mechanism terminated : {}";

    public TransactionGuard() {}

    @Builder
    public TransactionGuard(List<RequestTransactionTemplate<ID>> transactions) {
        this.transactions = transactions;
    }

    //</editor-fold>

    /**
     * Main transaction
     * @return Object
     */
    public Object transact() {
        log.info(START_TRANSACT_MSG);
        Stack<RequestTransactionTemplate<ID>> rollBackStack = new Stack<>();
        Object result = null;
        for (var trx : this.transactions){

            final var trxBootstrap = bootstrapTransact(trx);
            if (isBootstrapFailed(rollBackStack, trx)) return result;

            final var trxResult = processingTransact(trx);
            if (isTransactFailed(rollBackStack, trx)) return result;

            commitTransact(trx, trxResult);
            if (isCommitFailed(rollBackStack, trx)) return result;

            saveRollbackTransact(rollBackStack, trx);
            result = getFinalResultOnCompleteTransaction(rollBackStack, result, trxResult);
        }
        final var totalResultMessage = getTransactionResult(result);
        log.info("Transaction Guard mechanism terminated : {}", totalResultMessage);
        return totalResultMessage;
    }

    private RequestTransactionResult getTransactionResult(Object result) {
        return RequestTransactionResult.builder()
                .finalResult(result)
                .success(transactSuccess)
                .message(transactSuccess ? SUCCESS_TRANSACT_MSG : FAILED_TRANSACT_MSG)
                .build();
    }

    /**
     * Summary all transaction pipeline and information
     */
    public void summary() {
        if(!this.transactions.isEmpty()) {
            log.info("TRANSACTION SUMMARY");
            this.transactions.forEach(trx -> {
                log.info("Transaction : {} - {} : {}", trx.getTransactId(), trx.getTransactName(), trx);
            });
        }
    }

    //<editor-fold desc="TRANSACTION">

    public Object processingTransact(
            RequestTransactionTemplate<ID> trx
    ) {
        final var obj = trx.start();
        log.info("{}:{} - {} - output: {}", trx.getTransactId(), trx.getTransactName(), trx.getStatus(), obj);
        return obj;
    }

    public Object bootstrapTransact(
            RequestTransactionTemplate<ID> trx
    ) {
        final var obj = trx.bootstrap();
        log.info("{}:{} - {} - output: {}", trx.getTransactId(), trx.getTransactName(), trx.getStatus(), obj);
        return obj;
    }

    public void commitTransact(
            RequestTransactionTemplate<ID> trx,
            Object trxResult
    ) {
        if(nonNull(trxResult)) trx.commit();
        log.info("{}:{} - {}", trx.getTransactId(), trx.getTransactName(), trx.getStatus());
    }

    public void rollBack(
            Stack<RequestTransactionTemplate<ID>> rollBackStack,
            RequestTransactionTemplate<ID> trx
    ) {
        log.error("{}:{} - {}", trx.getTransactId(), trx.getTransactName(), trx.getStatus());
        while (!rollBackStack.isEmpty()) {
            var obj = rollBackStack.pop();
            log.error("Rollback: {} - {}", obj.getTransactId(), obj.getTransactName());
            obj.rollBack();
        }
    }

    //</editor-fold>

    //<editor-fold desc="TRANSACTION CHECK">

    protected boolean isBootstrapFailed(
            Stack<RequestTransactionTemplate<ID>> rollBackStack,
            RequestTransactionTemplate<ID> trx
    ) {
        return resolveIfTransactIsFailedOrViolated(rollBackStack, trx);
    }

    protected boolean isTransactFailed(
            Stack<RequestTransactionTemplate<ID>> rollBackStack,
            RequestTransactionTemplate<ID> trx
    ) {
        return resolveIfTransactIsFailedOrViolated(rollBackStack, trx);
    }

    protected boolean isCommitFailed(
            Stack<RequestTransactionTemplate<ID>> rollBackStack,
            RequestTransactionTemplate<ID> trx
    ) {
        return resolveIfTransactIsFailedOrViolated(rollBackStack, trx);
    }

    //</editor-fold>

    //<editor-fold desc="TRANSACTION FLOW">

    protected boolean resolveIfTransactIsFailedOrViolated(
            Stack<RequestTransactionTemplate<ID>> rollBackStack,
            RequestTransactionTemplate<ID> trx
    ) {
        if (trx.getStatus().equals(VIOLATED) || trx.getStatus().equals(FAILED)) {
            rollBack(rollBackStack, trx);
            this.transactSuccess = false;
            return true;
        }
        return false;
    }

    protected Object getFinalResultOnCompleteTransaction(
            Stack<RequestTransactionTemplate<ID>> rollBackStack,
            Object result, Object trxResult
    ) {
        if(rollBackStack.size() == this.transactions.size())
            result = trxResult;
        return result;
    }

    protected void saveRollbackTransact(
            Stack<RequestTransactionTemplate<ID>> rollBackStack,
            RequestTransactionTemplate<ID> trx
    ) {
        rollBackStack.push(trx);
    }

    //</editor-fold>

}
