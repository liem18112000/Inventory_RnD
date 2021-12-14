/*
 * Copyright (c) 2021. Liem Doan
 */

package com.fromlabs.inventory.supplierservice.supplier.providable_material;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

/**
 * Providable material service interface
 */
public interface ProvidableMaterialService {

    /**
     * Get providable material by id
     * @param id    Entity ID
     * @return      ProvidableMaterialEntity
     */
    ProvidableMaterialEntity getById(
            Long id
    );

    /**
     * Get all providable material entity as list by tenant id
     * @param tenantId  Tenant ID
     * @return          List&lt;ProvidableMaterialEntity&gt;
     */
    List<ProvidableMaterialEntity> getAll(
            Long tenantId
    );

    /**
     * Get all providable material entity as list with filter
     * @param specification Specification
     * @return              List&lt;ProvidableMaterialEntity&gt;
     */
    List<ProvidableMaterialEntity> getAll(
            Specification<ProvidableMaterialEntity> specification
    );

    /**
     * Get all providable material entity as list by name
     * @param tenantId  Tenant ID
     * @param name      Providable Material name
     * @return          List&lt;ProvidableMaterialEntity&gt;
     */
    List<ProvidableMaterialEntity> getByName(
            Long tenantId,
            String name
    );

    /**
     * Get page providable material entity with filter
     * @param specification Specification&lt;ProvidableMaterialEntity&gt;
     * @param pageable      Pageable
     * @return              Page&lt;ProvidableMaterialEntity&gt;
     */
    Page<ProvidableMaterialEntity> getPage(
            Specification<ProvidableMaterialEntity> specification,
            Pageable                                pageable
    );

    /**
     * Save providable material entity
     * @param entity    ProvidableMaterialEntity
     * @return          ProvidableMaterialEntity
     */
    ProvidableMaterialEntity save(
            ProvidableMaterialEntity entity
    );

    /**
     * Delete providable material
     * @param entity    ProvidableMaterialEntity
     */
    void delete(
            ProvidableMaterialEntity entity
    );
}
