/*
 * Copyright (c) 2021. Liem Doan
 */

package com.fromlabs.inventory.inventoryservice.domains.restaurant.utility;

import com.fromlabs.inventory.inventoryservice.domains.restaurant.services.RestaurantInventoryDomainService;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

@UtilityClass
@Slf4j
public class RestaurantTransactionConstraint {

    /**
     * checkConstraintAfterSuggest
     * @param domainService RestaurantInventoryDomainService
     * @return              boolean
     */
    public boolean checkConstraintAfterSuggest(
            RestaurantInventoryDomainService domainService
    ) {
        return Boolean.TRUE;
    }

    /**
     * checkConstraintBeforeSuggest
     * @param domainService RestaurantInventoryDomainService
     * @return              boolean
     */
    public boolean checkConstraintBeforeSuggest(
            RestaurantInventoryDomainService domainService
    ) {
        return Boolean.TRUE;
    }
}
