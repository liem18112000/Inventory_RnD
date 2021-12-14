/*
 * Copyright (c) 2021. Liem Doan
 */

package com.fromlabs.inventory.supplierservice.domains.restaurant.services;

import com.fromlabs.inventory.supplierservice.domains.AbstractDomainService;
import com.fromlabs.inventory.supplierservice.imports.ImportService;
import com.fromlabs.inventory.supplierservice.supplier.SupplierService;
import org.springframework.stereotype.Service;

@Service
public class RestaurantInventoryDomainServiceImpl
        extends AbstractDomainService
        implements RestaurantInventoryDomainService {

    public RestaurantInventoryDomainServiceImpl(
            SupplierService supplierService,
            ImportService   importService
    ) {
        super(supplierService, importService);
    }
}
