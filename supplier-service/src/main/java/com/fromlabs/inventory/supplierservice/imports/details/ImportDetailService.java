/*
 * Copyright (c) 2021. Liem Doan
 */

package com.fromlabs.inventory.supplierservice.imports.details;

import com.fromlabs.inventory.supplierservice.common.exception.ObjectNotFoundException;
import com.fromlabs.inventory.supplierservice.common.service.RestApiService;

/**
 * Import detail service
 * @author Liem
 */
public interface ImportDetailService extends RestApiService<ImportDetailEntity, Long> {

    /**
     * Get import entity by id
     * @param id Entity Unique ID
     * @return ImportDetailEntity
     * @throws ObjectNotFoundException throw if entity is not found
     */
    ImportDetailEntity getByIdWithException(Long id) throws ObjectNotFoundException;
}
