/*
 * Copyright (c) 2021. Liem Doan
 */

package com.fromlabs.inventory.supplierservice.supplier.providable_material;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProvidableMaterialRepository
        extends JpaRepository<ProvidableMaterialEntity, Long>, JpaSpecificationExecutor<ProvidableMaterialEntity> {
    Page<ProvidableMaterialEntity> findAll(Specification<ProvidableMaterialEntity> spec, Pageable pageable);
    List<ProvidableMaterialEntity> findAll(Specification<ProvidableMaterialEntity> spec);
    List<ProvidableMaterialEntity> findAllByClientIdAndName(Long clientId, String name);
    List<ProvidableMaterialEntity> findAllByClientId(Long clientId);
    Page<ProvidableMaterialEntity> findAllByClientId(Long clientId, Pageable pageable);
}
