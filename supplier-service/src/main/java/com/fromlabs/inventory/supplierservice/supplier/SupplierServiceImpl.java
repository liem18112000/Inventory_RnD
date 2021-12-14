/*
 * Copyright (c) 2021. Liem Doan
 */

package com.fromlabs.inventory.supplierservice.supplier;

import com.fromlabs.inventory.supplierservice.supplier.providable_material.ProvidableMaterialEntity;
import com.fromlabs.inventory.supplierservice.supplier.providable_material.ProvidableMaterialRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(rollbackFor = {Exception.class, Throwable.class})
@Slf4j
public class SupplierServiceImpl implements SupplierService {

    private final SupplierRepository            supplierRepository;
    private final ProvidableMaterialRepository  materialRepository;

    /**
     * Constructor
     * @param supplierRepository            SupplierRepository
     * @param materialRepository    ProvidableMaterialRepository
     */
    public SupplierServiceImpl(
            SupplierRepository supplierRepository,
            ProvidableMaterialRepository    materialRepository
    ) {
        this.supplierRepository = supplierRepository;
        this.materialRepository = materialRepository;
    }

    /**
     * Get supplier by id
     *
     * @param id Entity ID
     * @return SupplierEntity
     */
    public SupplierEntity getById(Long id) {
        return this.supplierRepository.findById(id).orElse(null);
    }

    /**
     * Get supplier by code
     *
     * @param code Entity Code
     * @return SupplierEntity
     */
    public SupplierEntity get(String code) {
        return this.supplierRepository.findByCode(code);
    }

    /**
     * Get entity by name and tenant id
     *
     * @param clientId Client ID
     * @param name     Entity name
     * @return SupplierEntity
     */
    public List<SupplierEntity> get(Long clientId, String name) {
        return this.supplierRepository.findByClientIdAndName(clientId, name);
    }

    /**
     * Get page of entity with client id
     *
     * @param clientId Client ID
     * @param pageable Pageable
     * @return Page&lt;SupplierEntity&gt;
     */
    public Page<SupplierEntity> getPage(Long clientId, Pageable pageable) {
        return this.supplierRepository.findAllByClientId(clientId, pageable);
    }

    /**
     * Get list of entity with client id
     *
     * @param clientId Client ID
     * @return List&lt;SupplierEntity&gt;
     */
    public List<SupplierEntity> getAll(Long clientId) {
        return this.supplierRepository.findAllByClientId(clientId);
    }

    /**
     * Get page of supplier with filter
     *
     * @param specification Specification&lt;SupplierEntity&gt;
     * @param pageable      Pageable
     * @return Page&lt;SupplierEntity&gt;
     */
    public Page<SupplierEntity> getPage(Specification<SupplierEntity> specification, Pageable pageable) {
        return this.supplierRepository.findAll(specification, pageable);
    }

    /**
     * Get list of supplier with filter
     *
     * @param specification Specification&lt;SupplierEntity&gt;
     * @return List&lt;SupplierEntity&gt;
     */
    public List<SupplierEntity> getAll(Specification<SupplierEntity> specification) {
        return this.supplierRepository.findAll(specification);
    }

    /**
     * Save supplier
     *
     * @param entity SupplierEntity
     * @return SupplierEntity
     */
    public SupplierEntity save(SupplierEntity entity) {
        return this.supplierRepository.save(entity);
    }

    /**
     * Delete supplier
     *
     * @param entity SupplierEntity
     */
    public void delete(SupplierEntity entity) {
        this.supplierRepository.delete(entity);
    }

    /**
     * Get ProvidableMaterial by id
     *
     * @param id Entity ID
     * @return ProvidableMaterialEntity
     */
    public ProvidableMaterialEntity getProvidableMaterialById(Long id) {
        return this.materialRepository.findById(id).orElse(null);
    }

    /**
     * Get all Providable Material by client id
     *
     * @param clientId Client ID
     * @return List&lt;ProvidableMaterialEntity&gt;
     */
    public List<ProvidableMaterialEntity> getAllProvidableMaterial(Long clientId) {
        return this.materialRepository.findAllByClientId(clientId);
    }

    /**
     * Get all Providable Material by name
     *
     * @param clientId Client ID
     * @param name     Name of entity
     * @return List&lt;ProvidableMaterialEntity&gt;
     */
    public List<ProvidableMaterialEntity> getProvidableMaterialByName(Long clientId, String name) {
        return this.materialRepository.findAllByClientIdAndName(clientId, name);
    }

    /**
     * Get all Providable Material by filter
     *
     * @param specification Specification&lt;ProvidableMaterialEntity&gt;
     * @return List&lt;ProvidableMaterialEntity&gt;
     */
    public List<ProvidableMaterialEntity> getAllProvidableMaterial(
            Specification<ProvidableMaterialEntity> specification
    ) {
        return this.materialRepository.findAll(specification);
    }

    /**
     * Get page Providable Material by filter
     *
     * @param specification Specification&lt;ProvidableMaterialEntity&gt;
     * @param pageable      Pageable
     * @return Page&lt;ProvidableMaterialEntity&gt;
     */
    public Page<ProvidableMaterialEntity> getPageProvidableMaterial(
            Specification<ProvidableMaterialEntity> specification,
            Pageable                                pageable
    ) {
        return this.materialRepository.findAll(specification, pageable);
    }

    /**
     * Save Providable Material entity
     *
     * @param entity ProvidableMaterialEntity
     * @return ProvidableMaterialEntity
     */
    public ProvidableMaterialEntity saveProvidableMaterial(
            ProvidableMaterialEntity entity
    ) {
        return this.materialRepository.save(entity);
    }

    /**
     * Delete Providable Material entity
     *
     * @param entity ProvidableMaterialEntity
     */
    public void deleteProvidableMaterial(ProvidableMaterialEntity entity) {
        this.materialRepository.delete(entity);
    }
}
