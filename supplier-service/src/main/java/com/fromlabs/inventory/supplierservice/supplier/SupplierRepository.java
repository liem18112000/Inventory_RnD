/*
 * Copyright (c) 2021. Liem Doan
 */

package com.fromlabs.inventory.supplierservice.supplier;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SupplierRepository
        extends JpaRepository<SupplierEntity, Long>, JpaSpecificationExecutor<SupplierEntity> {

    SupplierEntity findByCode(String code);
    List<SupplierEntity> findByClientIdAndName(Long clientId, String name);
    List<SupplierEntity> findAllByClientId(Long clientId);
    Page<SupplierEntity> findAllByClientId(Long clientId, Pageable pageable);
}
