/*
 * Copyright (c) 2021. Liem Doan
 */

package com.fromlabs.inventory.supplierservice.imports;

import com.fromlabs.inventory.supplierservice.supplier.SupplierEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface ImportRepository
        extends JpaRepository<ImportEntity, Long>, JpaSpecificationExecutor<ImportEntity> {

    List<ImportEntity> findAllBySupplier(SupplierEntity supplier);

    ImportEntity findByCode(String code);
}
