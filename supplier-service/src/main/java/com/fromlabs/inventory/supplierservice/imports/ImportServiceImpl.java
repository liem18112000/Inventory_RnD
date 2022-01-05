/*
 * Copyright (c) 2021. Liem Doan
 */

package com.fromlabs.inventory.supplierservice.imports;

import com.fromlabs.inventory.supplierservice.common.exception.ObjectNotFoundException;
import com.fromlabs.inventory.supplierservice.supplier.SupplierEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * {@inheritDoc}
 */
@Service
@Transactional(rollbackFor = {Exception.class, Throwable.class})
public class ImportServiceImpl implements ImportService {

    protected final ImportRepository repository;

    public ImportServiceImpl(ImportRepository repository) {
        this.repository = repository;
    }

    /**
     * {@inheritDoc}
     */
    public ImportEntity getById(@NotNull final Long id) {
        return this.repository.findById(id).orElse(null);
    }

    /**
     * {@inheritDoc}
     */
    public ImportEntity save(@NotNull final ImportEntity entity) {
        return this.repository.save(entity);
    }

    /**
     * {@inheritDoc}
     */
    public void delete(@NotNull final ImportEntity entity) {
        this.repository.delete(entity);
    }

    /**
     * {@inheritDoc}
     */
    public Page<ImportEntity> getPage(
            @NotNull final Specification<ImportEntity> specification,
            @NotNull final Pageable pageable) {
        return this.repository.findAll(specification, pageable);
    }

    /**
     * {@inheritDoc}
     */
    public List<ImportEntity> getAll(Specification<ImportEntity> specification) {
        return this.repository.findAll(specification);
    }

    /**
     * {@inheritDoc}
     */
    public List<ImportEntity> getAllBySupplier(SupplierEntity supplier) {
        return this.repository.findAllBySupplier(supplier);
    }

    /**
     * {@inheritDoc}
     */
    public ImportEntity getByIdWithException(@NotNull final Long id)
            throws ObjectNotFoundException {
        return this.repository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException(
                        "Import is not found with id : ".concat(String.valueOf(id))));
    }
}
