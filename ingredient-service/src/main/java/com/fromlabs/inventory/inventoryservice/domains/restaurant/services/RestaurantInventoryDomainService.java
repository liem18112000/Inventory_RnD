/*
 * Copyright (c) 2021. Liem Doan
 */

package com.fromlabs.inventory.inventoryservice.domains.restaurant.services;

import com.fromlabs.inventory.inventoryservice.client.recipe.beans.RecipeDetailDto;
import com.fromlabs.inventory.inventoryservice.client.recipe.beans.RecipeDto;
import com.fromlabs.inventory.inventoryservice.domains.restaurant.beans.ConfirmSuggestion;
import com.fromlabs.inventory.inventoryservice.domains.restaurant.beans.SendStatisticsResponse;
import com.fromlabs.inventory.inventoryservice.domains.restaurant.beans.SuggestResponse;
import com.fromlabs.inventory.inventoryservice.inventory.InventoryEntity;

import java.util.List;
import java.util.Map;

/**
 * Domain service interface for restaurant domain
 */
public interface RestaurantInventoryDomainService {

    /**
     * Sync inventory and return a map of ingredient ID - inventory DTO
     * @param tenantId  Client ID
     * @return          Map&lt;Long, InventoryEntity&gt;
     */
    Map<Long, InventoryEntity> convertSyncedInventoryToMap(
            Long tenantId
    );

    /**
     * Give a list of recipe with detail information that could be done based on existing items in the inventory
     * There are some point to notice
     * <ul>
     *     <li>convertSyncedInventoryToMap : Sync ingredient item and return a map of id - inventory DTO.
     *     This map will be used to check whether a recipe can be made by existing quantity of all item in the inventory system. </li>
     *     <li>checkInventoryForAllRecipes : Main operation of suggesting taxon:
     *          <ul>
     *              <li>All recipe group, contain many recipe child, based on tenant will be fetch out from recipe client</li>
     *              <li>In each group of recipe, all child recipe will be checked to see it is satisfied the available quantity or not</li>
     *              <li>In each recipe, all recipe detail will be fetched and its quantity will be checked.
     *              All detail must be passed so that a recipe can be suggested</li>
     *              <li>The final response will be conducted and return to controller </li>
     *          </ul>
     *     </li>
     * </ul>
     * @param tenantId Client ID
     * @return List of available recipe
     */
    List<SuggestResponse> suggestTaxon(Long tenantId);

    /**
     * Perform inventory check on all recipe group
     * @param tenantId                  Client ID
     * @param ingredientsInInventory    Map&lt;Long, InventoryEntity&gt;
     * @param response                  List&lt;SuggestResponse&gt;
     */
    void checkInventoryForAllRecipeGroups(
            Long                        tenantId,
            Map<Long, InventoryEntity>  ingredientsInInventory,
            List<SuggestResponse>       response
    );

    /**
     * Perform inventory check on a recipe group
     * @param tenantId                  Client ID
     * @param ingredientsInInventory    Map&lt;Long, InventoryEntity&gt;
     * @param response                  List&lt;SuggestResponse&gt;
     * @param group                     RecipeDto
     */
    void checkInventoryForRecipeGroup(
            Long tenantId,
            Map<Long, InventoryEntity>  ingredientsInInventory,
            List<SuggestResponse>       response,
            RecipeDto                   group
    );

    /**
     * Perform inventory check on a recipe child
     * @param tenantId                  Client ID
     * @param ingredientsInInventory    Map&lt;Long, InventoryEntity&gt;
     * @param response                  List&lt;SuggestResponse&gt;
     * @param recipe                    RecipeDto
     */
    void checkInventoryForRecipeChild(
            Long                        tenantId,
            Map<Long, InventoryEntity>  ingredientsInInventory,
            List<SuggestResponse>       response,
            RecipeDto                   recipe
    );

    /**
     * getMinQuantitySuggest
     * @param ingredientsInInventory    Map&lt;Long, InventoryEntity&gt;
     * @param minQuantitySuggest        int
     * @param details                   List&lt;RecipeDetailDto&gt;
     * @return                          int
     */
    int getMinQuantitySuggest(
            Map<Long, InventoryEntity>  ingredientsInInventory,
            int                         minQuantitySuggest,
            List<RecipeDetailDto>       details
    );

    /**
     * Confirm on suggestion
     * @param request SuggestResponse
     * @param quantity Quantity
     * @return SuggestResponse
     */
    ConfirmSuggestion confirmOnSuggestion(SuggestResponse request, int quantity);

    /**
     * Send inventory statistics
     * @param tenantId Client ID
     * @return SendStatisticsResponse
     */
    SendStatisticsResponse sendInventoryStatistics(Long tenantId);
}
