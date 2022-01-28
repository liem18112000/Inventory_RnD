/*
 * Copyright (c) 2021. Liem Doan
 */

package com.fromlabs.inventory.supplierservice.supplier;

import com.fromlabs.inventory.supplierservice.common.exception.ObjectNotFoundException;
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

    private final SupplierRepository supplierRepository;

    /**
     * Constructor
     * @param supplierRepository            SupplierRepository
     */
    public SupplierServiceImpl(
            SupplierRepository supplierRepository
    ) {
        this.supplierRepository = supplierRepository;
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
     * Get supplier by id
     *
     * @param id Entity ID
     * @return SupplierEntity
     * @throws ObjectNotFoundException when entity is not found by id
     */
    public SupplierEntity getByIdWithException(Long id) throws ObjectNotFoundException {
        return this.supplierRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException(
                        "Supplier is not found id: ".concat(String.valueOf(id))));
    }

    /**
     * Get supplier by code
     *
     * @param code Entity Code
     * @return SupplierEntity
     */
    public SupplierEntity getByCode(String code) {
        return this.supplierRepository.findByCode(code);
    }

    /**
     * Get entity by name and tenant id
     *
     * @param clientId Client ID
     * @param name     Entity name
     * @return SupplierEntity
     */
    public List<SupplierEntity> getByName(Long clientId, String name) {
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
}
