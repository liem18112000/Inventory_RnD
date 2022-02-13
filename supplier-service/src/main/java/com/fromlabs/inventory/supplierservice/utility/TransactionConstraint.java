/*
 * Copyright (c) 2021. Liem Doan
 */

package com.fromlabs.inventory.supplierservice.utility;

import com.fromlabs.inventory.supplierservice.client.ingredient.IngredientClient;
import com.fromlabs.inventory.supplierservice.common.exception.ConstraintViolateException;
import com.fromlabs.inventory.supplierservice.common.wrapper.ConstraintWrapper;
import com.fromlabs.inventory.supplierservice.imports.ImportService;
import com.fromlabs.inventory.supplierservice.imports.beans.request.ImportRequest;
import com.fromlabs.inventory.supplierservice.imports.details.ImportDetailService;
import com.fromlabs.inventory.supplierservice.imports.details.beans.request.ImportDetailRequest;
import com.fromlabs.inventory.supplierservice.supplier.SupplierService;
import com.fromlabs.inventory.supplierservice.supplier.beans.request.SupplierRequest;
import com.fromlabs.inventory.supplierservice.supplier.providable_material.ProvidableMaterialService;
import com.fromlabs.inventory.supplierservice.supplier.providable_material.beans.request.ProvidableMaterialRequest;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import javax.validation.constraints.NotNull;

import java.util.Objects;

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
            @NotNull final Long            id,
            @NotNull final SupplierService supplierService
    ) {
        return logWrapper(nonNull(supplierService.getById(id)),
                "checkSupplierExistById: {}");
    }

    /**
     * Check supplier (with both types) exist by code
     * @param code              Supplier code
     * @param supplierService   SupplierService
     * @return                  boolean
     */
    public boolean checkSupplierExistByCode(
            @NotNull final String          code,
            @NotNull final SupplierService supplierService
    ) {
        // Log out constraint result and return it
        return logWrapper(nonNull(supplierService.getByCode(code)),
                "checkSupplierExistByCode: {}");
    }

    /**
     * Check before save supplier
     * @param request           SupplierRequest
     * @param supplierService   SupplierService
     * @return                  boolean
     */
    public boolean checkBeforeSaveSupplier(
            @NotNull final SupplierRequest request,
            @NotNull final SupplierService supplierService
    ) {
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
            @NotNull final SupplierRequest request,
            @NotNull final SupplierService supplierService
    ) {
        // Check pre-conditions
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
            @NotNull final Long            supplierId,
            @NotNull final String          code,
            @NotNull final SupplierService supplierService
    ) {
        // Get supplier by code
        final var supplierWithCode = supplierService.getByCode(code);

        // Build constraint wrapper
        return ConstraintWrapper.builder()
                .name("Check constrain supplier is duplicated by code")
                .check(() -> isNull(supplierWithCode) || supplierWithCode.getId().equals(supplierId))
                .exception(new ConstraintViolateException("Supplier code is duplicated : ".concat(code)))
                .build();
    }

    /**
     * Check before save supplier
     * @param request           ImportRequest
     * @param importService     ImportService
     * @return                  boolean
     */
    public boolean checkBeforeSaveImport(
            @NotNull final ImportRequest request,
            @NotNull final ImportService importService
    ) {
        // Build constrain wrapper and check the constraint
        final boolean result = buildCheckImportDuplicateByCodeConstraintWrapper(
                request.getId(), request.getCode(), importService).constraintCheck();

        // Log out the check result and return it
        return logWrapper(result, "checkBeforeSaveImport: {}");
    }

    /**
     * Check before update supplier
     * @param request           ImportRequest
     * @param importService     ImportService
     * @return                  boolean
     */
    public boolean checkBeforeUpdateImport(
            @NotNull final ImportRequest request,
            @NotNull final ImportService importService
    ) {
        // Check pre-conditions
        assert nonNull(request.getId());

        // Build constrain wrapper and check the constraint
        final boolean result = checkImportExistById(request.getId(), importService) &&
                buildCheckImportDuplicateByCodeConstraintWrapper(
                request.getId(), request.getCode(), importService).constraintCheck();

        // Log out the check result and return it
        return logWrapper(result, "checkBeforeUpdateImport: {}");
    }

    /**
     * Build check import duplicate by code
     * If import id is provided, it will be checked as update mode
     * Otherwise, it will be checked as default mode
     * @param id            Import Unique ID
     * @param code          Import code
     * @param importService ImportService
     * @return ConstraintWrapper
     */
    private ConstraintWrapper buildCheckImportDuplicateByCodeConstraintWrapper(
            @NotNull final Long            id,
            @NotNull final String          code,
            @NotNull final ImportService   importService
    ) {
        // Get import by code
        final var entityWithCode = importService.getByCode(code);

        // Build constraint wrapper
        return ConstraintWrapper.builder()
                .name("Check constrain supplier is duplicated by code")
                .check(() -> isNull(entityWithCode) || entityWithCode.getId().equals(id))
                .exception(new ConstraintViolateException("Import code is duplicated : ".concat(code)))
                .build();
    }

    /**
     * Check entity exist by id
     * @param id ID
     * @param importService ImportService
     * @return boolean
     */
    public boolean checkImportExistById(
            @NotNull final Long id,
            @NotNull final ImportService importService
    ) {
        // Get import by id;
        final var importEntity = importService.getById(id);

        // Build constrain wrapper and check the constraint
        final boolean result = nonNull(importEntity);

        // Log out the check result and return it
        return logWrapper(result, "checkImportExistById: {}");
    }

    /**
     * Check material exist by id
     * @param id ID
     * @param service ProvidableMaterialService
     * @return boolean
     */
    public boolean checkMaterialExistById(
            @NotNull final Long id,
            @NotNull final ProvidableMaterialService service
    ) {
        // Get import by id;
        final var entity = service.getById(id);

        // Build constrain wrapper and check the constraint
        final boolean result = nonNull(entity);

        // Log out the check result and return it
        return logWrapper(result, "checkMaterialExistById: {}");
    }

    public boolean checkImportDetailExistById(
            @NotNull final Long id,
            @NotNull final ImportDetailService service
    ) {
        // Get import detail by id;
        final var entity = service.getById(id);

        // Build constrain wrapper and check the constraint
        final boolean result = nonNull(entity);

        // Log out the check result and return it
        return logWrapper(result, "checkImportDetailExistById: {}");
    }

    /**
     * Check constraints before update or save import detail
     * @param request ImportDetailRequest
     * @param importService ImportService
     * @param service ImportDetailService
     * @param client IngredientClient
     * @param isUpdate Update flag
     * @return boolean
     */
    public boolean checkConstraintsBeforeUpdateOrSaveImportDetail(
            @NotNull final ImportDetailRequest request,
            @NotNull final ImportService importService,
            @NotNull final ImportDetailService service,
            @NotNull final IngredientClient client,
            final boolean isUpdate
    ) {
        final var result = (!isUpdate || checkImportDetailExistById(request.getId(), service)) &&
                request.getQuantity() > 0 &&
                Objects.nonNull(importService.getById(request.getImportId())) &&
                Objects.nonNull(client.getIngredientById(request.getClientId(), request.getIngredientId()));

        // Log out the check result and return it
        return logWrapper(result, "checkConstraintsBeforeUpdateOrSaveImportDetail: {}");
    }

    /**
     * Check constraints before or update material
     * @param request ProvidableMaterialRequest
     * @param supplierService SupplierService
     * @param service ProvidableMaterialService
     * @param client IngredientClient
     * @param isUpdate Update material flag
     * @return boolean
     */
    public boolean checkConstrainsBeforeSaveOrUpdateMaterial(
            @NotNull final ProvidableMaterialRequest request,
            @NotNull final SupplierService supplierService,
            @NotNull final ProvidableMaterialService service,
            @NotNull final IngredientClient client,
            final boolean isUpdate
    ) {
        // Check condition
        final var result = (!isUpdate || checkMaterialExistById(request.getId(), service)) &&
                request.getMaximumQuantity() >= request.getMinimumQuantity() &&
                Objects.nonNull(supplierService.getById(request.getSupplierId())) &&
                Objects.nonNull(client.getIngredientById(request.getTenantId(),
                        request.getIngredientId()));

        // Log out the check result and return it
        return logWrapper(result, "checkConstrainsBeforeSaveMaterial: {}");
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
