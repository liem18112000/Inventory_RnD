/*
 * Copyright (c) 2021. Liem Doan
 */

package com.fromlabs.inventory.supplierservice.utility;

import com.fromlabs.inventory.supplierservice.common.exception.ConstraintViolateException;
import com.fromlabs.inventory.supplierservice.common.wrapper.ConstraintWrapper;
import com.fromlabs.inventory.supplierservice.supplier.SupplierService;
import com.fromlabs.inventory.supplierservice.supplier.beans.SupplierRequest;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

/**
 * <h1>Transaction Constraints layer</h1>
 *
 * <h2>Brief Information</h2>
 *
 * <p>
 *     This the third layer in the segment of non-infrastructure service layer.
 *     In detail, it play as the constraint guard for the pre-condition
 *     and post-condition
 * </p>
 *
 * <p>
 *     It is a mandatory layer as it must be implemented to keep the
 *     consistence of the domain service layer before and after executing transactions
 * </p>
 *
 * <h2>Usages</h2>
 *
 * <p>
 *     The infrastructure service such as the IngredientService, InventoryService or ItemService
 *     will be injected to perform checking the constraints
 * </p>
 *
 * <p>Despite that it is not easy to implement such constrain checking, it is straight-forward to
 * implement them with the regular style of programming</p>
 */
@UtilityClass
@Slf4j
public class TransactionConstraint {

    /**
     * Check supplier (with both types) exist by ID
     * @param id                Supplier ID
     * @param supplierService   SupplierService
     * @return                  boolean
     */
    public boolean checkSupplierExistById(
            Long            id,
            SupplierService supplierService
    ) {
        // Check pre-conditions
        assert nonNull(supplierService);

        return logWrapper(nonNull(supplierService.getById(id)), "checkSupplierExistById: {}");
    }

    /**
     * Check supplier (with both types) exist by code
     * @param code              Supplier code
     * @param supplierService   SupplierService
     * @return                  boolean
     */
    public boolean checkSupplierExistByCode(
            String          code,
            SupplierService supplierService
    ) {
        // Check pre-conditions
        assert nonNull(supplierService);

        // Log out constraint result and return it
        return logWrapper(nonNull(supplierService.get(code)), "checkSupplierExistByCode: {}");
    }

    /**
     * Check before save supplier
     * @param request           SupplierRequest
     * @param supplierService   SupplierService
     * @return                  boolean
     */
    public boolean checkBeforeSaveSupplier(
            SupplierRequest request,
            SupplierService supplierService
    ) {
        // Check pre-conditions
        assert nonNull(request);
        assert nonNull(supplierService);

        // Build constrain wrapper and check the constraint
        final boolean result = buildCheckSupplierDuplicateByCodeConstraintWrapper(
                request.getId(), request.getCode(), supplierService).constraintCheck();

        // Log out the check result and return it
        return logWrapper(result, "checkBeforeSaveSupplier: {}");
    }

    /**
     * Check before save supplier
     * @param request           SupplierRequest
     * @param supplierService   SupplierService
     * @return                  boolean
     */
    public boolean checkBeforeUpdateSupplier(
            SupplierRequest request,
            SupplierService supplierService
    ) {
        // Check pre-conditions
        assert nonNull(request);
        assert nonNull(supplierService);
        assert nonNull(request.getId());

        // Build constrain wrapper and check the constraint
        final boolean result = checkSupplierExistById(request.getId(), supplierService) &&
                buildCheckSupplierDuplicateByCodeConstraintWrapper(
                request.getId(), request.getCode(), supplierService).constraintCheck();

        // Log out the check result and return it
        return logWrapper(result, "checkBeforeUpdateSupplier: {}");
    }

    /**
     * Build check supplier duplicate by code
     * If supplier id is provided, it will be checked as update mode
     * Otherwise, it will be checked as default mode
     * @param supplierId        Supplier Unique ID
     * @param code              Supplier code
     * @param supplierService   SupplierService
     * @return                  ConstraintWrapper
     */
    private ConstraintWrapper buildCheckSupplierDuplicateByCodeConstraintWrapper(
            Long            supplierId,
            String          code,
            SupplierService supplierService
    ) {
        // Check pre-conditions
        assert nonNull(supplierService);

        // Get supplier by code
        final var supplierWithCode = supplierService.get(code);

        // Build constraint wrapper
        return ConstraintWrapper.builder()
                .name("Check constrain supplier is duplicated by code")
                .check(() -> (isNull(supplierId) && isNull(supplierWithCode)) || (nonNull(supplierId) && supplierWithCode.getId().equals(supplierId)))
                .exception(new ConstraintViolateException("Supplier code is duplicated : ".concat(code)))
                .build();
    }

    /**
     * Log wrapper
     * @param result    boolean
     * @param message   Log message
     * @return          boolean
     */
    private boolean logWrapper(boolean result, String message) {
        log.info(message, result);
        return result;
    }
}
