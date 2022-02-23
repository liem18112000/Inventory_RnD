/*
 * Copyright (c) 2021. Liem Doan
 */

package com.fromlabs.inventory.supplierservice.utility;

import com.fromlabs.inventory.supplierservice.client.ingredient.IngredientClient;
import com.fromlabs.inventory.supplierservice.common.template.*;
import com.fromlabs.inventory.supplierservice.imports.ImportService;
import com.fromlabs.inventory.supplierservice.imports.beans.request.ImportPageRequest;
import com.fromlabs.inventory.supplierservice.imports.beans.request.ImportRequest;
import com.fromlabs.inventory.supplierservice.imports.details.ImportDetailService;
import com.fromlabs.inventory.supplierservice.imports.details.beans.request.ImportDetailPageRequest;
import com.fromlabs.inventory.supplierservice.imports.details.beans.request.ImportDetailRequest;
import com.fromlabs.inventory.supplierservice.supplier.SupplierService;
import com.fromlabs.inventory.supplierservice.supplier.beans.request.SupplierPageRequest;
import com.fromlabs.inventory.supplierservice.supplier.beans.request.SupplierRequest;
import com.fromlabs.inventory.supplierservice.supplier.providable_material.ProvidableMaterialService;
import com.fromlabs.inventory.supplierservice.supplier.providable_material.beans.request.ProvidableMaterialPageRequest;
import com.fromlabs.inventory.supplierservice.supplier.providable_material.beans.request.ProvidableMaterialRequest;
import lombok.experimental.UtilityClass;

import javax.validation.constraints.NotNull;

import static com.fromlabs.inventory.supplierservice.config.AppConfig.*;
import static com.fromlabs.inventory.supplierservice.utility.ControllerValidation.*;
import static com.fromlabs.inventory.supplierservice.utility.RequestBootstrap.*;
import static com.fromlabs.inventory.supplierservice.utility.TransactionConstraint.*;
import static com.fromlabs.inventory.supplierservice.utility.TransactionLogic.*;

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

    //<editor-fold desc="buildGetSupplierGroupWithLabelValueTemplateProcess">

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

    //</editor-fold>

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
                .process(   () -> updateSupplier(request, supplierService))
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
                .process(   () -> getProvidableMaterialById(id, providableMaterialService, ingredientClient))
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
                .process(   () -> getAllProvidableMaterialByTenantId(tenantId, providableMaterialService, ingredientClient))
                .build();
    }

    //</editor-fold>

    //<editor-fold desc="buildGetPageProvidableTemplateProcess">

    public TemplateProcess buildGetPageProvidableTemplateProcess(
            @NotNull final Long tenantId,
            @NotNull final ProvidableMaterialPageRequest request,
            @NotNull final SupplierService supplierService,
            @NotNull final ProvidableMaterialService providableMaterialService,
            @NotNull final IngredientClient ingredientClient
    ) {
        return WebTemplateProcess.builder()
                .bootstrap( () -> bootstrapTenantId(tenantId, request))
                .validate(  () -> validateTenant(tenantId))
                .process(   () -> getPageProvidableMaterial(request, supplierService, providableMaterialService, ingredientClient))
                .build();
    }

    //</editor-fold>

    //<editor-fold desc="buildSaveProvidableMaterialTemplateProcess">

    /**
     * Build save material template process
     * @param tenantId                  Tenant ID
     * @param request                   ProvidableMaterialRequest
     * @param supplierService           SupplierService
     * @param providableMaterialService ProvidableMaterialService
     * @param ingredientClient          IngredientClient
     * @return TemplateProcess
     */
    public TemplateProcess buildSaveProvidableMaterialTemplateProcess(
            @NotNull final Long tenantId,
            @NotNull final ProvidableMaterialRequest request,
            @NotNull final SupplierService supplierService,
            @NotNull final ProvidableMaterialService providableMaterialService,
            @NotNull final IngredientClient ingredientClient
    ) {
        final boolean isUpdate = false;
        return WebTemplateProcessWithCheckBeforeAfter.WebCheckBuilder()
                .bootstrap( () -> bootstrapTenantId(tenantId, request))
                .validate(  () -> validateMaterialRequest(request, isUpdate))
                .before(    () -> checkConstrainsBeforeSaveOrUpdateMaterial(request, supplierService, providableMaterialService, ingredientClient, isUpdate))
                .process(   () -> saveProvidableMaterial(request, supplierService, providableMaterialService, ingredientClient))
                .build();
    }

    //</editor-fold>

    //<editor-fold desc="buildUpdateProvidableMaterialTemplateProcess">

    /**
     * Build update material template process
     * @param tenantId                  Tenant ID
     * @param request                   ProvidableMaterialRequest
     * @param supplierService           SupplierService
     * @param providableMaterialService ProvidableMaterialService
     * @param ingredientClient          IngredientClient
     * @return TemplateProcess
     */
    public TemplateProcess buildUpdateProvidableMaterialTemplateProcess(
            @NotNull final Long tenantId,
            @NotNull final ProvidableMaterialRequest request,
            @NotNull final SupplierService supplierService,
            @NotNull final ProvidableMaterialService providableMaterialService,
            @NotNull final IngredientClient ingredientClient
    ) {
        final boolean isUpdate = true;
        return WebTemplateProcessWithCheckBeforeAfter.WebCheckBuilder()
                .bootstrap( () -> bootstrapTenantId(tenantId, request))
                .validate(  () -> validateMaterialRequest(request, isUpdate))
                .before(    () -> checkConstrainsBeforeSaveOrUpdateMaterial(request, supplierService, providableMaterialService, ingredientClient, isUpdate))
                .process(   () -> updateProvidableMaterial(request, providableMaterialService, ingredientClient))
                .build();
    }

    //</editor-fold>

    //<editor-fold desc="buildDeleteProvidableMaterialTemplateProcess">

    /**
     * Build providable material by id template process
     * @param id Entity ID
     * @param providableMaterialService ProvidableMaterialService
     * @return TemplateProcess
     */
    public TemplateProcess buildDeleteProvidableMaterialTemplateProcess(
            @NotNull final Long id,
            @NotNull final ProvidableMaterialService providableMaterialService
    ) {
        return WebTemplateProcessWithCheckBeforeAfter.WebCheckBuilder()
                .validate(() -> validateId(id))
                .before(() -> checkMaterialExistById(id, providableMaterialService))
                .process(() -> deleteProvidableMaterial(id, providableMaterialService))
                .after(() -> !checkMaterialExistById(id, providableMaterialService))
                .build();
    }

    //</editor-fold>

    //<editor-fold desc="buildGetImportByIdTemplateProcess">

    /**
     * Build get import by id template process
     * @param id            Import Unique ID
     * @param importService importService
     * @return              TemplateProcess
     */
    public TemplateProcess buildGetImportByIdTemplateProcess(
            @NotNull final Long id,
            @NotNull final ImportService importService
    ) {
        return WebTemplateProcess.builder()
                .validate(  () -> validateId(id))
                .process(   () -> getImportById(id, importService))
                .build();
    }

    //</editor-fold>

    //<editor-fold desc="buildGetPageImportTemplateProcess">

    /**
     * Build get page import template process
     * @param tenantId          Tenant ID
     * @param request           ImportPageRequest
     * @param supplierService   SupplierService
     * @param importService     ImportService
     * @return                  TemplateProcess
     */
    public TemplateProcess buildGetPageImportTemplateProcess(
            @NotNull final Long tenantId,
            @NotNull final ImportPageRequest request,
            @NotNull final SupplierService supplierService,
            @NotNull final ImportService importService
    ) {
        return WebTemplateProcess.builder()
                .bootstrap( () -> bootstrapTenantId(tenantId, request))
                .validate(  () -> validateTenant(tenantId))
                .process(   () -> getPageImport(request, supplierService, importService))
                .build();
    }

    //</editor-fold>

    //<editor-fold desc="buildSaveImportTemplateProcess">

    /**
     * Build save import template process
     * @param tenantId Tenant ID
     * @param request   ImportRequest
     * @param importService ImportService
     * @param supplierService SupplierService
     * @return TemplateProcess
     */
    public TemplateProcess buildSaveImportTemplateProcess(
            @NotNull final Long tenantId,
            @NotNull final ImportRequest request,
            @NotNull final ImportService importService,
            @NotNull final SupplierService supplierService
    ) {
        return WebTemplateProcessWithCheckBeforeAfter.WebCheckBuilder()
                .bootstrap( () -> bootstrapTenantId(tenantId, request))
                .validate(  () -> validateImportRequest(request, false))
                .before(    () -> checkBeforeSaveImport(request, importService))
                .process(   () -> saveImport(request, supplierService, importService))
                .build();
    }

    //</editor-fold>

    //<editor-fold desc="buildUpdateImportTemplateProcess">

    /**
     * Build update import template process
     * @param tenantId Tenant ID
     * @param request   ImportRequest
     * @param importService ImportService
     * @return TemplateProcess
     */
    public TemplateProcess buildUpdateImportTemplateProcess(
            @NotNull final Long tenantId,
            @NotNull final ImportRequest request,
            @NotNull final ImportService importService
    ) {
        return WebTemplateProcessWithCheckBeforeAfter.WebCheckBuilder()
                .bootstrap( () -> bootstrapTenantId(tenantId, request))
                .validate(  () -> validateImportRequest(request, false))
                .before(    () -> checkBeforeUpdateImport(request, importService))
                .process(   () -> updateImport(request, importService))
                .build();
    }

    //</editor-fold>

    //<editor-fold desc="buildGetImportDetailByIdTemplateProcess">

    public TemplateProcess buildGetImportDetailByIdTemplateProcess (
            @NotNull final Long id,
            @NotNull final ImportDetailService importDetailService,
            @NotNull final IngredientClient ingredientClient
    ) {
        return WebTemplateProcess.builder()
                .validate(() -> validateId(id))
                .process(() -> getImportDetailById(id, importDetailService, ingredientClient))
                .build();
    }

    //</editor-fold>

    //<editor-fold desc="buildGetImportDetailPageTemplateProcess">

    /**
     * Build get import detail page template process
     * @param tenantId Tenant ID
     * @param request   ImportDetailPageRequest
     * @param importService ImportService
     * @param importDetailService ImportDetailService
     * @param ingredientClient IngredientClient
     * @return TemplateProcess
     */
    public TemplateProcess buildGetImportDetailPageTemplateProcess (
            @NotNull final Long tenantId,
            @NotNull final ImportDetailPageRequest request,
            @NotNull final ImportService importService,
            @NotNull final ImportDetailService importDetailService,
            @NotNull final IngredientClient ingredientClient
    ) {
        return WebTemplateProcess.builder()
                .validate(() -> validateTenant(tenantId))
                .bootstrap(() -> bootstrapTenantId(tenantId, request))
                .process(() -> getImportDetailPage(request, importService, importDetailService, ingredientClient))
                .build();
    }

    //</editor-fold>

    //<editor-fold desc="buildUpdateImportDetailTemplateProcess">

    /**
     * Build update import detail template process
     * @param request   ImportDetailRequest
     * @param importDetailService ImportDetailService
     * @param ingredientClient IngredientClient
     * @return TemplateProcess
     */
    public TemplateProcess buildUpdateImportDetailTemplateProcess(
            @NotNull final Long id,
            @NotNull final ImportDetailRequest request,
            @NotNull final ImportDetailService importDetailService,
            @NotNull final IngredientClient ingredientClient
    ) {
        return WebTemplateProcessWithCheckBeforeAfter.WebCheckBuilder()
                .bootstrap( () -> bootstrapId(id, request))
                .validate(  () -> validateImportDetailRequest(request))
                .before(    () -> checkConstraintsBeforeUpdateBasicInformationImportDetail(request, importDetailService))
                .process(   () -> updateImportDetail(request, importDetailService, ingredientClient))
                .build();
    }

    //</editor-fold>

    //<editor-fold desc="buildDeleteImportDetailByIdTemplateProcess">

    /**
     * Build delete import detail by id template process
     * @param id Import detail id
     * @param importDetailService ImportDetailService
     * @return TemplateProcess
     */
    public TemplateProcess buildDeleteImportDetailByIdTemplateProcess(
            @NotNull final Long id,
            @NotNull final ImportDetailService importDetailService
    ) {
        return WebTemplateProcessWithCheckBeforeAfter.WebCheckBuilder()
                .validate(  () -> validateId(id))
                .before(    () -> checkImportDetailExistById(id, importDetailService))
                .process(   () -> deleteImportDetailById(id, importDetailService))
                .after(     () -> !checkImportDetailExistById(id, importDetailService))
                .build();
    }

    //</editor-fold>
}
