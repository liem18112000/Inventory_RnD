/*
 * Copyright (c) 2021. Liem Doan
 */

package com.fromlabs.inventory.inventoryservice.domains.restaurant.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestHeader;

import static com.fromlabs.inventory.inventoryservice.config.AppConfig.TENANT_ID;

/**
 * Interface for Restaurant domain controller
 */
public interface RestaurantDomainController {

    /**
     * Suggest recipe and quantity of a taxon that could
     * be made by existing ingredient and items
     * @param tenantId  Client ID
     * @return          ResponseEntity
     */
    ResponseEntity<?> suggest(
            @RequestHeader(TENANT_ID) Long tenantId
    );
}
