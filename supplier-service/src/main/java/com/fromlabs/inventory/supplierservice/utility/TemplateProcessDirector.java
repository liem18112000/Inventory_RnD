/*
 * Copyright (c) 2021. Liem Doan
 */

package com.fromlabs.inventory.supplierservice.utility;

import com.fromlabs.inventory.supplierservice.client.ingredient.IngredientClient;
import com.fromlabs.inventory.supplierservice.common.template.*;
import com.fromlabs.inventory.supplierservice.supplier.SupplierService;
import com.fromlabs.inventory.supplierservice.supplier.beans.*;
import com.fromlabs.inventory.supplierservice.supplier.providable_material.ProvidableMaterialService;
import com.fromlabs.inventory.supplierservice.supplier.providable_material.beans.ProvidableMaterialPageRequest;
import lombok.experimental.UtilityClass;

import static com.fromlabs.inventory.supplierservice.config.AppConfig.*;
import static com.fromlabs.inventory.supplierservice.utility.ControllerValidation.*;
import static com.fromlabs.inventory.supplierservice.utility.RequestBootstrap.*;
import static com.fromlabs.inventory.supplierservice.utility.TransactionConstraint.*;
import static com.fromlabs.inventory.supplierservice.utility.TransactionLogic.*;
import static org.springframework.http.ResponseEntity.*;

/**
 * <h1>Template process builder director</h1>
 */
@UtilityClass
public class TemplateProcessDirector {

    //<editor-fold desc="buildGetPageSupplierGroupTemplateProcess">

    /**
     * Build get page supplier group template process
     * @param tenantId          Tenant ID
     * @param request           SupplierPageRequest
     * @param supplierService   SupplierService
     * @return                  TemplateProcess
     */
    public TemplateProcess buildGetPageSupplierGroupTemplateProcess(
            Long                tenantId,
            SupplierPageRequest request,
            SupplierService     supplierService
    ) {
        return WebTemplateProcess.builder()
                .bootstrap( () -> bootstrapTenantId(tenantId, request))
                .validate(  () -> validateTenant(request.getTenantId()))
                .process(   () -> getSupplierPage(request, supplierService))
                .build();
    }

    //</editor-fold>

    //<editor-fold desc="buildGetPageSupplierChildTemplateProcess">

    /**
     * Build get page supplier child template process
     * @param tenantId          Tenant ID
     * @param request           SupplierPageRequest
     * @param supplierService   SupplierService
     * @return                  TemplateProcess
     */
    public TemplateProcess buildGetPageSupplierChildTemplateProcess(
            Long                tenantId,
            SupplierPageRequest request,
            SupplierService     supplierService
    ) {
        return WebTemplateProcess.builder()
                .bootstrap( () -> bootstrapTenantId(tenantId, request))
                .validate(  () -> validateTenantAndParentId(request.getTenantId(), request.getParentId()))
                .process(   () -> getSupplierPage(request, supplierService))
                .build();
    }

    //</editor-fold>

    //<editor-fold desc="getListSupplierGroupTemplateProcess">

    /**
     * Get list of supplier group by filter
     * @param tenantId          Tenant ID
     * @param request           SupplierPageRequest
     * @param supplierService   SupplierService
     * @return                  TemplateProcess
     */
    public TemplateProcess getListSupplierGroupTemplateProcess(
            Long                tenantId,
            SupplierPageRequest request,
            SupplierService     supplierService
    ) {
        return WebTemplateProcess.builder()
                .bootstrap( () -> bootstrapTenantId(tenantId, request))
                .validate(  () -> validateTenant(request.getTenantId()))
                .process(   () -> getSupplierList(request, supplierService))
                .build();
    }

    //</editor-fold>

    //<editor-fold desc="getListSupplierChildTemplateProcess">

    /**
     * Get list of supplier child by filter
     * @param tenantId          Tenant ID
     * @param request           SupplierPageRequest
     * @param supplierService   SupplierService
     * @return                  TemplateProcess
     */
    public TemplateProcess getListSupplierChildTemplateProcess(
            Long                tenantId,
            SupplierPageRequest request,
            SupplierService     supplierService
    ) {
        return WebTemplateProcess.builder()
                .bootstrap( () -> bootstrapTenantId(tenantId, request))
                .validate(  () -> validateTenantAndParentId(request.getTenantId(), request.getParentId()))
                .process(   () -> getSupplierList(request, supplierService))
                .build();
    }

    //</editor-fold>

    //<editor-fold desc="buildGetSupplierByIdTemplateProcess">

    /**
     * Build get Supplier by ID template process
     * @param id                Supplier
     * @param supplierService   SupplierService
     * @return                  TemplateProcess
     */
    public TemplateProcess buildGetSupplierByIdTemplateProcess(
            Long            id,
            SupplierService supplierService
    ) {
        return WebTemplateProcessWithCheckBeforeAfter.WebCheckBuilder()
                .validate(  () -> validateId(id))
                .before(    () -> checkSupplierExistById(id, supplierService))
                .process(   () -> getSupplierById(id, supplierService))
                .build();
    }

    //</editor-fold>

    //<editor-fold desc="buildGetSupplierByCodeTemplateProcess">

    /**
     * Build get Supplier by code template process
     * @param code              Supplier code
     * @param supplierService   SupplierService
     * @return                  TemplateProcess
     */
    public TemplateProcess buildGetSupplierByCodeTemplateProcess(
            String          code,
            SupplierService supplierService
    ) {
        return WebTemplateProcess.builder()
                .validate(  () -> validateRequired(CODE, code))
                .process(   () -> getSupplierByCode(code, supplierService))
                .build();
    }

    //</editor-fold>

    //<editor-fold desc="buildGetSupplierByNameTemplateProcess">

    /**
     * Build get supplier by name template process
     * @param tenantId          Tenant ID
     * @param name              Supplier name
     * @param supplierService   SupplierService
     * @return                  TemplateProcess
     */
    public TemplateProcess buildGetSupplierByNameTemplateProcess(
            Long            tenantId,
            String          name,
            SupplierService supplierService
    ) {
        return WebTemplateProcess.builder()
                .validate(  () -> validateTenantIdAndName(tenantId, name))
                .process(   () -> getSupplierByName(tenantId, name, supplierService))
                .build();
    }

    //</editor-fold>

    //<editor-fold desc="buildGetSupplierChildWithLabelValueTemplateProcess">

    /**
     * Build get supplier child with label-value
     * @param tenantId          Tenant ID
     * @param parentId          Supplier Group Id
     * @param supplierService   SupplierService
     * @return                  TemplateProcess
     */
    public TemplateProcess buildGetSupplierChildWithLabelValueTemplateProcess(
            Long            tenantId,
            Long            parentId,
            SupplierService supplierService
    ) {
        return WebTemplateProcess.builder()
                .validate(  () -> validateTenantAndParentId(tenantId, parentId))
                .process(   () -> getSupplierChildWithLabelValue(tenantId, parentId, supplierService))
                .build();
    }

    //</editor-fold>

    /**
     * Build get supplier group with label-value
     * @param tenantId          Tenant ID
     * @param supplierService   SupplierService
     * @return                  TemplateProcess
     */
    public TemplateProcess buildGetSupplierGroupWithLabelValueTemplateProcess(
            Long            tenantId,
            SupplierService supplierService
    ) {
        return WebTemplateProcess.builder()
                .validate(  () -> validateTenant(tenantId))
                .process(   () -> getSupplierGroupWithLabelValue(tenantId, supplierService))
                .build();
    }

    //<editor-fold desc="buildSaveSupplierTemplateProcess">

    /**
     * Build save supplier template process
     * @param tenantId          Tenant ID
     * @param request           SupplierRequest
     * @param supplierService   SupplierService
     * @return                  TemplateProcess
     */
    public TemplateProcess buildSaveSupplierTemplateProcess(
            Long            tenantId,
            SupplierRequest request,
            SupplierService supplierService
    ) {
        return WebTemplateProcessWithCheckBeforeAfter.WebCheckBuilder()
                .bootstrap( () -> bootstrapTenantId(tenantId, request))
                .validate(  () -> validateSupplierRequest(request, false))
                .before(    () -> checkBeforeSaveSupplier(request, supplierService))
                .process(   () -> saveSupplier(request, supplierService))
                .build();
    }

    //</editor-fold>

    //<editor-fold desc="buildUpdateSupplierTemplateProcess">

    /**
     * Build update supplier template process
     * @param tenantId          Tenant ID
     * @param request           SupplierRequest
     * @param supplierService   SupplierService
     * @return                  TemplateProcess
     */
    public TemplateProcess buildUpdateSupplierTemplateProcess(
            Long            tenantId,
            SupplierRequest request,
            SupplierService supplierService
    ) {
        return WebTemplateProcessWithCheckBeforeAfter.WebCheckBuilder()
                .bootstrap( () -> bootstrapTenantId(tenantId, request))
                .validate(  () -> validateSupplierRequest(request, true))
                .before(    () -> checkBeforeUpdateSupplier(request, supplierService))
                .process(   () -> saveSupplier(request, supplierService))
                .build();
    }

    //</editor-fold>

    //<editor-fold desc="buildDeleteSupplierTemplateProcess">

    /**
     * Build delete supplier template process
     * @param id                Supplier ID
     * @param supplierService   SupplierService
     * @return                  TemplateProcess
     */
    public TemplateProcess buildDeleteSupplierTemplateProcess(
            Long            id,
            SupplierService supplierService
    ) {
        return WebTemplateProcessWithCheckBeforeAfter.WebCheckBuilder()
                .validate(  () -> validateId(id))
                .before(    () -> checkSupplierExistById(id, supplierService))
                .process(   () -> deleteSupplier(id, supplierService))
                .after(     () -> !checkSupplierExistById(id, supplierService))
                .build();
    }

    //</editor-fold>

    //<editor-fold desc="Build get providable material by id template process">

    /**
     * Build get providable material by id template process
     * @param id                        Unique Entity ID
     * @param providableMaterialService ProvidableMaterialService
     * @param ingredientClient          IngredientClient
     * @return                          TemplateProcess
     */
    public TemplateProcess buildGetProvidableMaterialById(
            Long                        id,
            ProvidableMaterialService   providableMaterialService,
            IngredientClient            ingredientClient
    ) {
        return WebTemplateProcess.builder()
                .validate(  () -> validateId(id))
                .process(   () -> ok(getProvidableMaterialById(id, providableMaterialService, ingredientClient)))
                .build();
    }

    //</editor-fold>

    //<editor-fold desc="Build get all providable material by tenant id template process">

    /**
     * Build get all providable material by tenant id template process
     * @param tenantId                  Tenant ID
     * @param providableMaterialService ProvidableMaterialService
     * @param ingredientClient          IngredientClient
     * @return                          TemplateProcess
     */
    public TemplateProcess buildGetAllProvidableMaterialByTenantId(
            Long                        tenantId,
            ProvidableMaterialService   providableMaterialService,
            IngredientClient            ingredientClient
    ) {
        return WebTemplateProcess.builder()
                .validate(  () -> validateTenant(tenantId))
                .process(   () -> ok(getAllProvidableMaterialByTenantId(tenantId, providableMaterialService, ingredientClient)))
                .build();
    }

    //</editor-fold>

    public TemplateProcess buildGetPageProvidable(
            Long tenantId,
            ProvidableMaterialPageRequest request
    ) {
        return WebTemplateProcessWithCheckBeforeAfter.WebCheckBuilder()
//                .bootstrap( () -> bootstrapTenantId(tenantId, ))
                .build();
    }
}
