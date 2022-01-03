/*
 * Copyright (c) 2021. Liem Doan
 */

package com.fromlabs.inventory.inventoryservice.domains.restaurant.beans;

import com.fromlabs.inventory.inventoryservice.domains.restaurant.services.RestaurantInventoryDomainService;
import com.fromlabs.inventory.inventoryservice.inventory.InventoryService;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;

import java.util.TimerTask;

@EqualsAndHashCode(callSuper = true)
@Data
@Slf4j
public class ExpiredSuggestTask extends TimerTask {

    private final SuggestResponse                   suggestResponse;
    private final InventoryService                  inventoryService;
    private final RestaurantInventoryDomainService  domainService;
    private boolean deactivate = false;

    /**
     * Construct
     * @param suggestResponse   SuggestResponse
     * @param inventoryService  InventoryService
     * @param domainService     RestaurantInventoryDomainService
     */
    public ExpiredSuggestTask(
            SuggestResponse                     suggestResponse,
            InventoryService                    inventoryService,
            RestaurantInventoryDomainService    domainService
    ) {
        this.suggestResponse    = suggestResponse;
        this.inventoryService   = inventoryService;
        this.domainService      = domainService;
    }


    /**
     * Run on update inventory to release the amount of reserved item if taxon suggestion is not confirmed
     */
    @Override
    public void run() {
        if(!deactivate) {
            log.info("Expire task activate - suggestion response : {}", suggestResponse);
            suggestResponse.getDetails().forEach(detail -> {
                final var ingredient = detail.getIngredient();
                var inventory = inventoryService.getById(ingredient.getId());
                final var releaseQuantity = detail.getQuantity() * suggestResponse.getTaxonQuantity();
                if(releaseQuantity > 0) {
                    log.info("Start release expired suggestion of ingredient : {} - {}", ingredient.getId(), ingredient.getName());
                    final var newReservedQuantity = inventory.getReserved() - releaseQuantity;
                    log.info("Release ingredient item quantity : {}", releaseQuantity);
                    if(newReservedQuantity < 0) {
                        log.error("New reserved quantity is not valid : {}", newReservedQuantity);
                    }else{
                        inventory.setReserved(inventory.getReserved() - releaseQuantity);
                        log.info("New reserved quantity: {}", inventory.getReserved());
                        // TODO: make consider on whether update inventory or not
                        inventory.setQuantity(inventory.getQuantity() + releaseQuantity);
                        log.info("New available quantity: {}", inventory.getQuantity());
                    }
                    inventoryService.save(inventory);
                }else log.error("Release quantity is not valid : {}", releaseQuantity);
            });
        }else {
            log.info("Expire task was deactivated - suggestion response : {}", suggestResponse);
        }

    }
}
