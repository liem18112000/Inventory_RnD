/*
 * Copyright (c) 2021. Liem Doan
 */

package com.fromlabs.inventory.inventoryservice.domains.fast.controller;

import com.fromlabs.inventory.inventoryservice.config.ApiV1;
import com.fromlabs.inventory.inventoryservice.domains.fast.services.FastInventoryDomainService;
import com.fromlabs.inventory.inventoryservice.utility.TransactionLogic;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.fromlabs.inventory.inventoryservice.domains.DomainServiceConfiguration.*;

/**
 * Domain controller for jakten
 */
@RestController
@RequestMapping(value = "${application.base-url}/" + ApiV1.URI_API + "/${domain.service-path}", produces = ApiV1.MIME_API)
@ConditionalOnProperty(
        value       = DOMAIN_SERVICE_NAME_CONFIG,
        havingValue = FAST_DOMAIN
)
@Slf4j
public class FastInventoryDomainController {

    //<editor-fold desc="SETUP">

    protected final FastInventoryDomainService domainService;

    public static final String SERVICE_PATH = "/${application.base-url}/" + ApiV1.URI_API + "/${domain.service-path}";

    /**
     * The constructor is initialized with three parameter (see in Parameters)
     * @param domainService     The service of Restaurant domain
     */
    public FastInventoryDomainController(
            @Qualifier(FAST_DOMAIN)
            FastInventoryDomainService    domainService
    ) {
        log.info("Domain controller : {}", this.getClass().getName());
        this.domainService = domainService;
    }

    private String path(HttpMethod method, String subPath, String apiVersion) {
        return TransactionLogic.path(method, SERVICE_PATH, subPath, apiVersion);
    }

    //</editor-fold>


}
