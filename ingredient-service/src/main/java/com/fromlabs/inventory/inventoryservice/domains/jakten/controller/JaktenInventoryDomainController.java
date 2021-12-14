/*
 * Copyright (c) 2021. Liem Doan
 */

package com.fromlabs.inventory.inventoryservice.domains.jakten.controller;

import com.fromlabs.inventory.inventoryservice.config.versions.ApiV1;
import com.fromlabs.inventory.inventoryservice.domains.jakten.services.JaktenInventoryDomainService;
import com.fromlabs.inventory.inventoryservice.utility.TransactionLogic;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.fromlabs.inventory.inventoryservice.domains.DomainServiceConfiguration.*;

/**
 * Domain controller for fast
 */
@RestController
@RequestMapping(value = "${application.base-url}/" + ApiV1.URI_API + "/${domain.service-path}", produces = ApiV1.MIME_API)
@ConditionalOnProperty(
        value       = DOMAIN_SERVICE_NAME_CONFIG,
        havingValue = JAKTEN_DOMAIN
)
@Slf4j
public class JaktenInventoryDomainController {

    //<editor-fold desc="SETUP">

    protected final JaktenInventoryDomainService domainService;

    public static final String SERVICE_PATH = "/${application.base-url}/" + ApiV1.URI_API + "/${domain.service-path}";

    /**
     * The constructor is initialized with three parameter (see in Parameters)
     * @param domainService     The service of Restaurant domain
     */
    public JaktenInventoryDomainController(
            @Qualifier(JAKTEN_DOMAIN)
            JaktenInventoryDomainService        domainService
    ) {
        log.info("Domain controller : {}", this.getClass().getName());
        this.domainService = domainService;
    }

    private String path(HttpMethod method, String subPath, String apiVersion) {
        return TransactionLogic.path(method, SERVICE_PATH, subPath, apiVersion);
    }

    //</editor-fold>


}
