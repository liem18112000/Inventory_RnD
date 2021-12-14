/*
 * Copyright (c) 2021. Liem Doan
 */

package com.fromlabs.inventory.supplierservice.utility;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;

import static com.fromlabs.inventory.supplierservice.utility.TransactionLogic.*;

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
 */
@UtilityClass
@Slf4j
public class TransactionWrapper {

}
