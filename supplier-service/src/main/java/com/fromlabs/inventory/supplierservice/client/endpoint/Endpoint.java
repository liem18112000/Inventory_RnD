/*
 * Copyright (c) 2021. Liem Doan
 */

package com.fromlabs.inventory.supplierservice.client.endpoint;

import com.fromlabs.inventory.supplierservice.supplier.SupplierEntity;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

import static com.fromlabs.inventory.supplierservice.config.AppConfig.ID;

/**
 * Supplier endpoint interface for internally expose API to other service
 */
public interface Endpoint {

    //<editor-fold desc="Supplier">

    /**
     * Get supplier by id
     * @param id    Supplier ID
     * @return      SupplierEntity
     */
    SupplierEntity getSupplierById(
            Long id
    );

    /**
     * Get supplier by code
     * @param code  Supplier Code
     * @return      SupplierEntity
     */
    SupplierEntity getSupplierByCode(
            String code
    );

    /**
     * Get all supplier as list by name and tenant id
     * @param tenantId  Tenant ID
     * @param name      Supplier name
     * @return          List&lt;SupplierEntity&gt;
     */
    List<SupplierEntity> getAllSupplierByName(
            Long    tenantId,
            String  name
    );

    /**
     * Get all supplier group with label-value form
     * @param tenantId  Tenant ID
     * @return          List&lt;SupplierEntity&gt;
     */
    List<SupplierEntity> getAllSupplierGroupLabelValue(
            Long tenantId
    );

    /**
     * Get all supplier child with label-value form
     * @param tenantId  Tenant ID
     * @param parentId  Supplier Group ID
     * @return          List&lt;SupplierEntity&gt;
     */
    List<SupplierEntity> getAllSupplierChildLabelValue(
            Long tenantId,
            Long parentId
    );

    //</editor-fold>


}
