/*
 * Copyright (c) 2021. Liem Doan
 */

package com.fromlabs.inventory.supplierservice.domains;

import com.fromlabs.inventory.supplierservice.imports.ImportService;
import com.fromlabs.inventory.supplierservice.supplier.SupplierService;

abstract public class AbstractDomainService {

    private final SupplierService   supplierService;
    private final ImportService     importService;

    protected AbstractDomainService(
            SupplierService supplierService,
            ImportService importService
    ) {
        this.supplierService = supplierService;
        this.importService = importService;
    }
}
