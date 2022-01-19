/*
 * Copyright (c) 2021. Liem Doan
 */

package com.fromlabs.inventory.supplierservice.imports;

import com.fromlabs.inventory.supplierservice.common.exception.ObjectNotFoundException;
import com.fromlabs.inventory.supplierservice.common.service.RestApiService;
import com.fromlabs.inventory.supplierservice.supplier.SupplierEntity;

import java.util.List;

/**
 * Import service
 * @author Liem
 */
public interface ImportService extends RestApiService<ImportEntity, Long> {

    /**
     * Get import entity by id
     * @param id Entity Unique ID
     * @return ImportEntity
     * @throws ObjectNotFoundException throw if entity is not found
     */
    ImportEntity getByIdWithException(Long id) throws ObjectNotFoundException;

    /**
     * Get import entity by code
     * @param code Entity Code
     * @return ImportEntity
     */
    ImportEntity getByCode(String code);
}
