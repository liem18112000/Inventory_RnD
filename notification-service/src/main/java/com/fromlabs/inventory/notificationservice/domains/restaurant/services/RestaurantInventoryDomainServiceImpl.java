/*
 * Copyright (c) 2021. Liem Doan
 */

package com.fromlabs.inventory.notificationservice.domains.restaurant.services;

import com.fromlabs.inventory.notificationservice.domains.AbstractDomainService;
import org.springframework.stereotype.Service;

@Service
public class RestaurantInventoryDomainServiceImpl
        extends AbstractDomainService
        implements RestaurantInventoryDomainService {

    public RestaurantInventoryDomainServiceImpl(
    ) {
        super();
    }
}
