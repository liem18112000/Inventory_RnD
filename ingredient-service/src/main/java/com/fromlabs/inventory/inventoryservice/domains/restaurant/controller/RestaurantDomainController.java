/*
 * Copyright (c) 2021. Liem Doan
 */

package com.fromlabs.inventory.inventoryservice.domains.restaurant.controller;

import com.fromlabs.inventory.inventoryservice.domains.restaurant.beans.ConfirmSuggestion;
import com.fromlabs.inventory.inventoryservice.domains.restaurant.beans.SuggestResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
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
            Long tenantId
    );

    /**
     * Confirm suggest recipe and quantity of a taxon that could
     * be made by existing ingredient and items
     * @param confirmSuggestion  CSuggestResponse
     * @param quantity  Confirmed quantity
     * @return          ResponseEntity
     */
    ResponseEntity<?> confirmSuggest(
            SuggestResponse confirmSuggestion, int quantity
    );

    /**
     * Send monthly statistics to pre-defined receipt
     * @param tenantId  Client ID
     * @return          ResponseEntity
     */
    ResponseEntity<?> sendStatistics(
            Long tenantId
    );
}
