/*
 * Copyright (c) 2021. Liem Doan
 */

package com.fromlabs.inventory.supplierservice.supplier.providable_material;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Providable material service implementation
 */
@Service
@Transactional(rollbackFor = {Exception.class, Throwable.class})
public class ProvidableMaterialServiceImpl implements ProvidableMaterialService {

    private final ProvidableMaterialRepository repository;

    /**
     * Constructor
     * @param repository    ProvidableMaterialRepository
     */
    public ProvidableMaterialServiceImpl(
            ProvidableMaterialRepository repository
    ) {
        this.repository = repository;
    }

    /**
     * Get providable material by id
     *
     * @param id        Entity ID
     * @return          ProvidableMaterialEntity
     */
    public ProvidableMaterialEntity getById(
            Long id
    ) {
        return this.repository.findById(id).orElse(null);
    }

    /**
     * Get all providable material entity as list by tenant id
     *
     * @param tenantId  Tenant ID
     * @return          List&lt;ProvidableMaterialEntity&gt;
     */
    public List<ProvidableMaterialEntity> getAll(
            Long tenantId
    ) {
        return this.repository.findAllByClientId(tenantId);
    }

    /**
     * Get all providable material entity as list with filter
     *
     * @param specification Specification
     * @return List&lt;ProvidableMaterialEntity&gt;
     */
    public List<ProvidableMaterialEntity> getAll(Specification<ProvidableMaterialEntity> specification) {
        return this.repository.findAll(specification);
    }

    /**
     * Get all providable material entity as list by name
     *
     * @param tenantId  Tenant ID
     * @param name      Providable Material name
     * @return          List&lt;ProvidableMaterialEntity&gt;
     */
    public List<ProvidableMaterialEntity> getByName(
            Long    tenantId,
            String  name
    ) {
        return this.repository.findAllByClientIdAndName(tenantId, name);
    }

    /**
     * Get page providable material entity with filter
     *
     * @param specification Specification&lt;ProvidableMaterialEntity&gt;
     * @param pageable      Pageable
     * @return              Page&lt;ProvidableMaterialEntity&gt;
     */
    public Page<ProvidableMaterialEntity> getPage(
            Specification<ProvidableMaterialEntity> specification,
            Pageable                                pageable
    ) {
        return this.repository.findAll(specification, pageable);
    }

    /**
     * Save providable material entity
     *
     * @param entity    ProvidableMaterialEntity
     * @return          ProvidableMaterialEntity
     */
    public ProvidableMaterialEntity save(
            ProvidableMaterialEntity entity
    ) {
        return this.repository.save(entity);
    }

    /**
     * Delete providable material
     *
     * @param entity ProvidableMaterialEntity
     */
    public void delete(
            ProvidableMaterialEntity entity
    ) {
        this.repository.delete(entity);
    }
}
