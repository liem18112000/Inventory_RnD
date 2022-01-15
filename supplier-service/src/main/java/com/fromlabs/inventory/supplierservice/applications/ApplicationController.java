/*
 * Copyright (c) 2021. Liem Doan
 */

package com.fromlabs.inventory.supplierservice.applications;

import com.fromlabs.inventory.supplierservice.imports.beans.request.ImportPageRequest;
import com.fromlabs.inventory.supplierservice.imports.beans.request.ImportRequest;
import com.fromlabs.inventory.supplierservice.imports.details.beans.request.ImportDetailPageRequest;
import com.fromlabs.inventory.supplierservice.supplier.beans.request.SupplierPageRequest;
import com.fromlabs.inventory.supplierservice.supplier.beans.request.SupplierRequest;
import com.fromlabs.inventory.supplierservice.supplier.providable_material.beans.request.ProvidableMaterialPageRequest;
import com.fromlabs.inventory.supplierservice.supplier.providable_material.beans.request.ProvidableMaterialRequest;
import org.springframework.http.ResponseEntity;

public interface ApplicationController {

    //<editor-fold desc="SUPPLIER">

    /**
     * Get page supplier group with filter
     * @param tenantId  Tenant ID
     * @param request   SupplierPageRequest
     * @return          ResponseEntity
     */
    ResponseEntity<?> getPageSupplierGroup(
            Long                tenantId,
            SupplierPageRequest request
    );

    /**
     * Get all supplier group as list with filter
     * @param tenantId  Tenant ID
     * @param request   SupplierPageRequest
     * @return          ResponseEntity
     */
    ResponseEntity<?> getAllSupplierGroup(
            Long                tenantId,
            SupplierPageRequest request
    );

    /**
     * Get page supplier child with filter
     * @param tenantId  Tenant ID
     * @param request   SupplierPageRequest
     * @return          ResponseEntity
     */
    ResponseEntity<?> getPageSupplierChild(
            Long                tenantId,
            SupplierPageRequest request
    );

    /**
     * Get all supplier child as list with filter
     * @param tenantId  Tenant ID
     * @param request   SupplierPageRequest
     * @return          ResponseEntity
     */
    ResponseEntity<?> getAllSupplierChild(
            Long                tenantId,
            SupplierPageRequest request
    );

    /**
     * Get all supplier (with both types) by name as list
     * @param tenantId  Tenant ID
     * @param name      Suppler name
     * @return          ResponseEntity
     */
    ResponseEntity<?> getAllSupplierByName(
            Long                tenantId,
            String              name
    );

    /**
     * Get supplier (ith both types) by name
     * @param code  Supplier Code
     * @return      ResponseEntity
     */
    ResponseEntity<?> getSupplierByCode(
            String code
    );

    /**
     * Get supplier (with both types) with ID
     * @param id        Unique Entity ID
     * @return          ResponseEntity
     */
    ResponseEntity<?> getSupplierById(
            Long id
    );

    /**
     * Save supplier (with both types)
     * @param tenantId  Tenant ID
     * @param request   SupplierRequest
     * @return          ResponseEntity
     */
    ResponseEntity<?> saveSupplier(
            Long            tenantId,
            SupplierRequest request
    );

    /**
     * Update supplier (with both types)
     * @param tenantId  Tenant ID
     * @param request   SupplierRequest
     * @return          ResponseEntity
     */
    ResponseEntity<?> updateSupplier(
            Long tenantId,
            SupplierRequest request
    );

    /**
     * Delete supplier (with both types) by id
     * @param id    Unique Entity ID
     * @return      ResponseEntity
     */
    ResponseEntity<?> deleteSupplier(
            Long id
    );

    //</editor-fold>

    //<editor-fold desc="PROVIDABLE MATERIAL">

    /**
     * Get providable material by id
     * @param id    Unique Entity ID
     * @return      ResponseEntity
     */
    ResponseEntity<?> getProvidableMaterialById(
            Long id
    );

    /**
     * Get all providable material as list b tenant id
     * @param tenantId  Tenant ID
     * @return          ResponseEntity
     */
    ResponseEntity<?> getAllProvidableMaterialByTenantId(
            Long tenantId
    );

    /**
     * Get page providable material with filter
     * @param tenantId  Tenant ID
     * @param request   ProvidableMaterialPageRequest
     * @return          ResponseEntity
     */
    ResponseEntity<?> getPageProvidableMaterialWithFilter(
            Long tenantId,
            ProvidableMaterialPageRequest request
    );

    /**
     * Save material with request
     * @param tenantId  Tenant ID
     * @param request   ProvidableMaterialRequest
     * @return ResponseEntity
     */
    ResponseEntity<?> saveProvidableMaterial(
            Long tenantId,
            ProvidableMaterialRequest request
    );

    /**
     * Update material with request
     * @param tenantId  Tenant ID
     * @param request   ProvidableMaterialRequest
     * @return ResponseEntity
     */
    ResponseEntity<?> updateProvidableMaterial(
            Long tenantId,
            ProvidableMaterialRequest request
    );

    //</editor-fold>

    //<editor-fold desc="IMPORT">

    /**
     * Get page import with filter
     * @param tenantId  Tenant ID
     * @param request   ImportPageRequest
     * @return          ResponseEntity
     */
    ResponseEntity<?> getPageImport(
            Long                tenantId,
            ImportPageRequest   request
    );

    /**
     * Get all import as list with filter
     * @param tenantId  Tenant ID
     * @param request   SupplierPageRequest
     * @return          ResponseEntity
     */
    ResponseEntity<?> getAllImport(
            Long                tenantId,
            ImportPageRequest   request
    );

    /**
     * Get import with ID
     * @param id        Unique Entity ID
     * @return          ResponseEntity
     */
    ResponseEntity<?> getImportById(
            Long id
    );

    /**
     * Save import by request
     * @param tenantId  Tenant ID
     * @param request   ImportRequest
     * @return          ResponseEntity
     */
    ResponseEntity<?> saveImport(
            Long            tenantId,
            ImportRequest   request
    );

    /**
     * Update import by request
     * @param tenantId  Tenant ID
     * @param request   SupplierRequest
     * @return          ResponseEntity
     */
    ResponseEntity<?> updateImport(
            Long tenantId,
            SupplierRequest request
    );

    //</editor-fold>
}
