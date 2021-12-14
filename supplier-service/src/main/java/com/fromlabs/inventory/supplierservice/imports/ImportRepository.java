/*
 * Copyright (c) 2021. Liem Doan
 */

package com.fromlabs.inventory.supplierservice.imports;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ImportRepository
        extends JpaRepository<ImportEntity, Long>, JpaSpecificationExecutor<ImportEntity> {
}
