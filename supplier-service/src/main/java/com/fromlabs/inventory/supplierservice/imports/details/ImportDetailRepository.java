/*
 * Copyright (c) 2021. Liem Doan
 */

package com.fromlabs.inventory.supplierservice.imports.details;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ImportDetailRepository
        extends JpaRepository<ImportDetailEntity, Long>, JpaSpecificationExecutor<ImportDetailEntity> {

}
