/*
 * Copyright (c) 2021. Liem Doan
 */

package com.fromlabs.inventory.supplierservice.supplier;

import com.fromlabs.inventory.supplierservice.supplier.providable_material.ProvidableMaterialEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

/**
 * Service interface for SupplierEntity
 */
public interface SupplierService {
    /**
     * Get supplier by id
     * @param id            Entity ID
     * @return              SupplierEntity
     */
    SupplierEntity getById(Long id);

    /**
     * Get supplier by code
     * @param code          Entity Code
     * @return              SupplierEntity
     */
    SupplierEntity get(String code);

    /**
     * Get entity by name and tenant id
     * @param clientId      Client ID
     * @param name          Entity name
     * @return              List&lt;SupplierEntity&gt;
     */
    List<SupplierEntity> get(Long clientId, String name);

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

    /**
     * Get page of supplier with filter
     * @param specification Specification&lt;SupplierEntity&gt;
     * @param pageable      Pageable
     * @return              Page&lt;SupplierEntity&gt;
     */
    Page<SupplierEntity> getPage(Specification<SupplierEntity> specification, Pageable pageable);

    /**
     * Get list of supplier with filter
     * @param specification Specification&lt;SupplierEntity&gt;
     * @return              List&lt;SupplierEntity&gt;
     */
    List<SupplierEntity> getAll(Specification<SupplierEntity> specification);

    /**
     * Save supplier
     * @param entity        SupplierEntity
     * @return              SupplierEntity
     */
    SupplierEntity save(SupplierEntity entity);

    /**
     * Delete supplier
     * @param entity        SupplierEntity
     */
    void delete(SupplierEntity entity);

    /**
     * Get ProvidableMaterial by id
     * @param id    Entity ID
     * @return      ProvidableMaterialEntity
     */
    ProvidableMaterialEntity getProvidableMaterialById(Long id);

    /**
     * Get all Providable Material by client id
     * @param clientId  Client ID
     * @return          List&lt;ProvidableMaterialEntity&gt;
     */
    List<ProvidableMaterialEntity> getAllProvidableMaterial(Long clientId);

    /**
     * Get all Providable Material by name
     * @param clientId  Client ID
     * @param name      Name of entity
     * @return          List&lt;ProvidableMaterialEntity&gt;
     */
    List<ProvidableMaterialEntity> getProvidableMaterialByName(Long clientId, String name);

    /**
     * Get all Providable Material by filter
     * @param specification Specification&lt;ProvidableMaterialEntity&gt;
     * @return              List&lt;ProvidableMaterialEntity&gt;
     */
    List<ProvidableMaterialEntity> getAllProvidableMaterial(Specification<ProvidableMaterialEntity> specification);

    /**
     * Get page Providable Material by filter
     * @param specification Specification&lt;ProvidableMaterialEntity&gt;
     * @param pageable      Pageable
     * @return              Page&lt;ProvidableMaterialEntity&gt;
     */
    Page<ProvidableMaterialEntity> getPageProvidableMaterial(Specification<ProvidableMaterialEntity> specification, Pageable pageable);

    /**
     * Save Providable Material entity
     * @param entity        ProvidableMaterialEntity
     * @return              ProvidableMaterialEntity
     */
    ProvidableMaterialEntity saveProvidableMaterial(ProvidableMaterialEntity entity);

    /**
     * Delete Providable Material entity
     * @param entity    ProvidableMaterialEntity
     */
    void deleteProvidableMaterial(ProvidableMaterialEntity entity);
}
