/*
 * Copyright (c) 2021. Liem Doan
 */

package com.fromlabs.inventory.inventoryservice.domains.restaurant.utility;

import com.fromlabs.inventory.inventoryservice.domains.restaurant.services.RestaurantInventoryDomainService;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.springframework.http.ResponseEntity.ok;

@UtilityClass
@Slf4j
public class RestaurantTransactionLogic {

    /**
     * Suggest recipe and quantity of a taxon that could
     * be made by existing ingredient and items
     * @param domainService RestaurantInventoryDomainService
     * @param tenantId      Client ID
     * @return              ResponseEntity
     */
    public ResponseEntity<?> suggestTaxon(
            Long                                tenantId,
            RestaurantInventoryDomainService    domainService
    ) {
        return ok(domainService.suggestTaxon(tenantId));
    }
}
