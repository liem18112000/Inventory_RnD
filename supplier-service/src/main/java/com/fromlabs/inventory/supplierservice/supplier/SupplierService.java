/*
 * Copyright (c) 2021. Liem Doan
 */

package com.fromlabs.inventory.supplierservice.supplier;

import com.fromlabs.inventory.supplierservice.common.exception.ObjectNotFoundException;
import com.fromlabs.inventory.supplierservice.common.service.RestApiService;
import com.fromlabs.inventory.supplierservice.supplier.providable_material.ProvidableMaterialEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

/**
 * Service interface for SupplierEntity
 */
public interface SupplierService extends RestApiService<SupplierEntity, Long> {
    /**
     * Get supplier by id
     * @param id            Entity ID
     * @return              SupplierEntity
     * @throws ObjectNotFoundException when entity is not found by id
     */
    SupplierEntity getByIdWithException(Long id) throws ObjectNotFoundException;

    /**
     * Get supplier by code
     * @param code          Entity Code
     * @return              SupplierEntity
     */
    SupplierEntity getByCode(String code);

    /**
     * Get entity by name and tenant id
     * @param clientId      Client ID
     * @param name          Entity name
     * @return              List&lt;SupplierEntity&gt;
     */
    List<SupplierEntity> getByName(Long clientId, String name);

    /**
     * Get page of entity with client id
     * @param clientId      Client ID
     * @param pageable      Pageable
     * @return              Page&lt;SupplierEntity&gt;
     */
    Page<SupplierEntity> getPage(Long clientId, Pageable pageable);

    /**
     * Get list of entity with client id
     * @param clientId      Client ID
     * @return              List&lt;SupplierEntity&gt;
     */
    List<SupplierEntity> getAll(Long clientId);
}
