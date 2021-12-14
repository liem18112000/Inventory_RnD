/*
 * Copyright (c) 2021. Liem Doan
 */

package com.fromlabs.inventory.inventoryservice.domains.restaurant.controller;

import com.fromlabs.inventory.inventoryservice.common.template.WebTemplateProcessWithCheckBeforeAfter;
import com.fromlabs.inventory.inventoryservice.config.versions.ApiV1;
import com.fromlabs.inventory.inventoryservice.domains.restaurant.services.RestaurantInventoryDomainService;
import com.fromlabs.inventory.inventoryservice.domains.restaurant.utility.RestaurantRequestBootstrap;
import com.fromlabs.inventory.inventoryservice.domains.restaurant.utility.RestaurantTransactionConstraint;
import com.fromlabs.inventory.inventoryservice.domains.restaurant.utility.RestaurantTransactionLogic;
import com.fromlabs.inventory.inventoryservice.utility.ControllerValidation;
import com.fromlabs.inventory.inventoryservice.utility.TransactionConstraint;
import com.fromlabs.inventory.inventoryservice.utility.TransactionLogic;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.fromlabs.inventory.inventoryservice.config.AppConfig.TENANT_ID;
import static com.fromlabs.inventory.inventoryservice.domains.DomainServiceConfiguration.DOMAIN_SERVICE_NAME_CONFIG;
import static com.fromlabs.inventory.inventoryservice.domains.DomainServiceConfiguration.RESTAURANT_DOMAIN;

/**
 * Domain controller for restaurant
 */
@RestController
@RequestMapping(
        value       = "${application.base-url}/" + ApiV1.URI_API + "/${domain.service-path}", produces = ApiV1.MIME_API
)
@ConditionalOnProperty(
        value       = DOMAIN_SERVICE_NAME_CONFIG,
        havingValue = RESTAURANT_DOMAIN
)
@Slf4j
public class RestaurantInventoryDomainController implements RestaurantDomainController {

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

    /**
     * Trace out API information
     * @param method    HttpMethod
     * @param subPath   API sub path
     * @return          String
     */
    private String path(HttpMethod method, String subPath) {
        return TransactionLogic.path(method, SERVICE_PATH, subPath, ApiV1.VERSION);
    }

    //</editor-fold>

    /**
     * Suggest recipe and quantity of a taxon that could
     * be made by existing ingredient and items
     * @param tenantId  Client ID
     * @return          ResponseEntity
     */
    @PostMapping("suggest")
    public ResponseEntity<?> suggest(
            @RequestHeader(TENANT_ID) Long tenantId
    ) {
        log.info(path(HttpMethod.POST, "suggest"));
        return (ResponseEntity<?>) WebTemplateProcessWithCheckBeforeAfter.WebCheckBuilder()
                .bootstrap( () -> null)
                .validate(  () -> ControllerValidation.validateTenant(tenantId))
                .before(    () -> RestaurantTransactionConstraint.checkConstraintBeforeSuggest(domainService))
                .process(   () -> RestaurantTransactionLogic.suggestTaxon(tenantId, domainService))
                .after(     () -> RestaurantTransactionConstraint.checkConstraintAfterSuggest(domainService))
                .build().run();
    }
}
