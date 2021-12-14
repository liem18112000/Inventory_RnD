/*
 * Copyright (c) 2021. Liem Doan
 */

package com.fromlabs.inventory.inventoryservice.utility;

import com.fromlabs.inventory.inventoryservice.common.transaction.RequestTransactionTemplate;
import com.fromlabs.inventory.inventoryservice.ingredient.*;
import com.fromlabs.inventory.inventoryservice.ingredient.beans.IngredientRequest;
import com.fromlabs.inventory.inventoryservice.ingredient.config.IngredientConfigEntity;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicReference;

import static com.fromlabs.inventory.inventoryservice.utility.TransactionLogic.*;

/**
 * <h1>Transaction layer wrapper</h1>
 *
 * <h2>Brief Information</h2>
 *
 * <p>
 *     This a optional wrapper for Transaction Layer as it must satisfy the ACID properties
 * </p>
 *
 * <p>
 *     Wrapper will apply builder to simply the process of creating a transaction constraint wrapper
 * </p>
 *
 * @see lombok.Builder
 *
 * <h2>Usages</h2>
 *
 * <h3>Component</h3>
 *
 * <p>There are two properties must be provide </p>
 * <ul>
 *     <li>
 *         processStrategy : the main operation for transaction. The exception or any error from this
 *         operation will cause the activation of rollback operation
 *     </li>
 *     <li>
 *         rollBackStrategy : the rollback strategy if there is any exception.
 *         The rollback must not throw any exceptino otherwise system get server eror
 *     </li>
 * </ul>
 *
 * <p>Furthermore, there are some optional properties</p>
 *
 * <ul>
 *     <li>
 *         transactName : name of transaction
 *     </li>
 * </ul>
 *
 * <h3>Example</h3>
 *
 * <pre>
 *     RequestTransactionTemplate.builder()
 *                 .transactName("saveConfigTransaction")
 *                 .processStrategy(   () -&gt; saveConfigTransactionProcess(request, ingredientService, ingredient, config))
 *                 .rollBackStrategy(  () -&gt; saveIngredientConfigTransactionRollback(ingredientService, config))
 *                 .build();
 * </pre>
 *
 * @see RequestTransactionTemplate
 */
@UtilityClass
@Slf4j
public class TransactionWrapper {

    //<editor-fold desc="SAVE INGREDIENT">

    /**
     * Create transaction wrapper for save ingredient config
     * @param request           IngredientRequest
     * @param ingredientService IngredientService
     * @param ingredient        AtomicReference&lt;IngredientEntity&gt;
     * @param config            AtomicReference&lt;IngredientConfigEntity>&gt;
     * @return                  RequestTransactionTemplate
     */
    public RequestTransactionTemplate<Serializable> getSaveConfigTransaction(
            IngredientRequest                       request,
            IngredientService                       ingredientService,
            AtomicReference<IngredientEntity>       ingredient,
            AtomicReference<IngredientConfigEntity> config
    ) {
        log.info("saveIngredientAndConfig: getSaveConfigTransaction");
        return RequestTransactionTemplate.builder()
                .transactName("saveConfigTransaction")
                .processStrategy(   () -> saveConfigTransactionProcess(request, ingredientService, ingredient, config))
                .rollBackStrategy(  () -> saveIngredientConfigTransactionRollback(ingredientService, config))
                .build();
    }

    /**
     * Create transaction wrapper for save ingredient
     * @param request           IngredientRequest
     * @param ingredientService IngredientServiceImpl
     * @param ingredient        AtomicReference&lt;IngredientEntity&gt;
     * @return                  RequestTransactionTemplate
     */
    public RequestTransactionTemplate<Serializable> getSaveIngredientTransaction(
            IngredientRequest                   request,
            IngredientService                   ingredientService,
            AtomicReference<IngredientEntity>   ingredient
    ) {
        log.info("saveIngredientAndConfig: getSaveIngredientTransaction");
        return RequestTransactionTemplate.builder()
                .transactName("saveIngredientTransaction")
                .processStrategy(   () -> saveIngredientTransactionProcess(request, ingredientService, ingredient))
                .rollBackStrategy(  () -> saveIngredientTransactionRollBack(ingredientService, ingredient))
                .build();
    }

    //</editor-fold>
}
