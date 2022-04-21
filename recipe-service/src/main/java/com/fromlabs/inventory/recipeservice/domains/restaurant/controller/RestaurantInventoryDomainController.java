/*
 * Copyright (c) 2021. Liem Doan
 */

package com.fromlabs.inventory.recipeservice.domains.restaurant.controller;

import com.fromlabs.inventory.recipeservice.config.ApiV1;
import com.fromlabs.inventory.recipeservice.domains.restaurant.services.RestaurantInventoryDomainService;
import io.sentry.spring.tracing.SentryTransaction;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.fromlabs.inventory.recipeservice.domains.DomainServiceConfiguration.DOMAIN_SERVICE_NAME_CONFIG;
import static com.fromlabs.inventory.recipeservice.domains.DomainServiceConfiguration.RESTAURANT_DOMAIN;

/**
 * Domain controller for restaurant
 */
@RestController
@RequestMapping(value = "${application.base-url}/" + ApiV1.URI_API + "/${domain.service-path}", produces = ApiV1.MIME_API)
@ConditionalOnProperty(
        value       = DOMAIN_SERVICE_NAME_CONFIG,
        havingValue = RESTAURANT_DOMAIN
)
@Slf4j
@SentryTransaction(operation = "restaurant-recipe-service")
public class RestaurantInventoryDomainController {

    //<editor-fold desc="SETUP">

    protected final RestaurantInventoryDomainService domainService;

    public static final String SERVICE_PATH = "/${application.base-url}/" + ApiV1.URI_API + "/${domain.service-path}";

    /**
     * The constructor is initialized with three parameter (see in Parameters)
     * @param domainService     The service of Restaurant domain
     */
    public RestaurantInventoryDomainController(
            @Qualifier(RESTAURANT_DOMAIN)
            RestaurantInventoryDomainService    domainService
    ) {
        log.info("Domain controller : {}", this.getClass().getName());
        this.domainService = domainService;
    }

    //</editor-fold>

}
