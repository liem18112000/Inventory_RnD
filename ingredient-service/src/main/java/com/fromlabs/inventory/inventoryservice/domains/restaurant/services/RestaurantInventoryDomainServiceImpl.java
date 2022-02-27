/*
 * Copyright (c) 2021. Liem Doan
 */

package com.fromlabs.inventory.inventoryservice.domains.restaurant.services;

import com.fromlabs.inventory.inventoryservice.client.recipe.RecipeClient;
import com.fromlabs.inventory.inventoryservice.client.recipe.beans.RecipeDetailDto;
import com.fromlabs.inventory.inventoryservice.client.recipe.beans.RecipeDto;
import com.fromlabs.inventory.inventoryservice.common.dto.BaseDto;
import com.fromlabs.inventory.inventoryservice.domains.AbstractDomainService;
import com.fromlabs.inventory.inventoryservice.domains.restaurant.beans.ConfirmSuggestion;
import com.fromlabs.inventory.inventoryservice.domains.restaurant.beans.IngredientSuggestion;
import com.fromlabs.inventory.inventoryservice.domains.restaurant.beans.SuggestResponse;
import com.fromlabs.inventory.inventoryservice.ingredient.IngredientService;
import com.fromlabs.inventory.inventoryservice.ingredient.mapper.IngredientMapper;
import com.fromlabs.inventory.inventoryservice.inventory.InventoryEntity;
import com.fromlabs.inventory.inventoryservice.inventory.InventoryService;
import com.fromlabs.inventory.inventoryservice.item.ItemService;
import com.fromlabs.inventory.inventoryservice.utility.TransactionLogic;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.lang.Integer.MAX_VALUE;
import static java.lang.Math.floor;
import static java.util.Objects.nonNull;

@Service
@Slf4j
@Transactional(rollbackFor = {Throwable.class, Exception.class})
public class RestaurantInventoryDomainServiceImpl
        extends AbstractDomainService implements RestaurantInventoryDomainService {

    //<editor-fold desc="SETUP">

    protected final RecipeClient recipeClient;

    /**
     * Constructor
     * @param ingredientService IngredientService
     * @param inventoryService  InventoryService
     * @param itemService       ItemService
     */
    public RestaurantInventoryDomainServiceImpl(
            IngredientService ingredientService,
            InventoryService inventoryService,
            ItemService itemService,
            RecipeClient recipeClient) {
        super(ingredientService, inventoryService, itemService);
        this.recipeClient = recipeClient;
    }

    //</editor-fold>

    //<editor-fold desc="CONVERT FROM SYNCED INVENTORY TO MAP">

    /**
     * Sync inventory and return a map of ingredient ID - inventory DTO
     * @param tenantId  Client ID
     * @return          Map&lt;Long, InventoryEntity&gt;
     */
    public Map<Long, InventoryEntity> convertSyncedInventoryToMap(
            Long tenantId
    ) {
        return TransactionLogic.syncAllIngredientInInventory(tenantId, ingredientService, inventoryService, itemService).stream()
                .collect(Collectors.toMap(InventoryEntity::getIngredientId, Function.identity()));
    }

    //</editor-fold>

    //<editor-fold desc="SUGGEST TAXON">

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
    public List<SuggestResponse> suggestTaxon(
            Long tenantId
    ) {
        // Sync ingredient item and return a map of id - inventory DTO.
        final var ingredientsInInventory = convertSyncedInventoryToMap(tenantId);

        // Instantiate a list of suggest response
        var response = new ArrayList<SuggestResponse>();

        // Main operation of suggesting taxon
        checkInventoryForAllRecipeGroups(tenantId, ingredientsInInventory, response);

        // Return response
        return response;
    }

    /**
     * Perform inventory check on all recipe group
     * @param tenantId                  Client ID
     * @param ingredientsInInventory    Map&lt;Long, InventoryEntity&gt;
     * @param response                  List&lt;SuggestResponse&gt;
     */
    public void checkInventoryForAllRecipeGroups(
            Long                        tenantId,
            Map<Long, InventoryEntity>  ingredientsInInventory,
            List<SuggestResponse>       response
    ) {
        // Main loop for check all recipe groups
        recipeClient.getAllGroup(tenantId).forEach(
                group -> checkInventoryForRecipeGroup(tenantId, ingredientsInInventory, response, group)
        );
    }

    /**
     * Perform inventory check on a recipe group
     * @param tenantId                  Client ID
     * @param ingredientsInInventory    Map&lt;Long, InventoryEntity&gt;
     * @param response                  List&lt;SuggestResponse&gt;
     * @param group                     RecipeDto
     */
    public void checkInventoryForRecipeGroup(
            Long                        tenantId,
            Map<Long, InventoryEntity>  ingredientsInInventory,
            List<SuggestResponse>       response,
            RecipeDto                   group
    ) {
        // Ignore inactive or null recipe group and run next iteration
        if(isActive(group)) {

            // Main loop for inventory check for all child recipe
            recipeClient.getAllChild(tenantId, group.getId()).forEach(

                    // Inventory check on all recipe child of a recipe group
                    recipe -> checkInventoryForRecipeChild(tenantId, ingredientsInInventory, response, recipe)
            );
        }
    }

    /**
     * Perform inventory check on a recipe child
     * @param tenantId                  Client ID
     * @param ingredientsInInventory    Map&lt;Long, InventoryEntity&gt;
     * @param response                  List&lt;SuggestResponse&gt;
     * @param recipe                    RecipeDto
     */
    public void checkInventoryForRecipeChild(
            Long                        tenantId,
            Map<Long, InventoryEntity>  ingredientsInInventory,
            List<SuggestResponse>       response,
            RecipeDto                   recipe
    ) {
        if(isActive(recipe)) {

            // Instantiate min quantity ofr recipe suggested
            var minQuantitySuggest = MAX_VALUE;

            // Fetch all recipe detail of a recipe child
            final var details = recipeClient.getDetailAll(tenantId, recipe.getId());

            // Check available quantity is whether satisfy the recipe detail list.
            // If it is satisfied return quantity
            // Keep quantity unchanged if it is not satisfied (2147483647)
            minQuantitySuggest = getMinQuantitySuggest(ingredientsInInventory, minQuantitySuggest, details);

            // Check for a valid suggestion based on the list of detail and quantity
            if(isValidSuggestion(minQuantitySuggest, details))

                // Add suggestion response to result placeholder
                response.add(buildSuggestResponse(recipe, minQuantitySuggest, details));
        }
    }

    /**
     * getMinQuantitySuggest
     * @param ingredientsInInventory    Map&lt;Long, InventoryEntity&gt;
     * @param minQuantitySuggest        int
     * @param details                   List&lt;RecipeDetailDto&gt;
     * @return                          int
     */
    public int getMinQuantitySuggest(
            Map<Long, InventoryEntity>  ingredientsInInventory,
            int                         minQuantitySuggest,
            List<RecipeDetailDto>       details
    ) {
        // Main loop for checking all recipe detail inside a recipe child
        for(var detail : details) {

            // Check activeness and null
            if(isActive(detail)) {

                // Check inventory has enough items
                if(isInventoryHasEnough(ingredientsInInventory, detail))

                    // Update min quantity of recipe can be made by sentinel algorithms
                    // Update value if there is a value is smaller than the smallest value
                    minQuantitySuggest = sentinelMinSuggestionQuantity(ingredientsInInventory, minQuantitySuggest, detail);

                else
                    // Terminate all recipe detail check mif there is a detail is not satisfied
                    break;
            }
        }
        return minQuantitySuggest;
    }

    //</editor-fold>

    //<editor-fold desc="Confirm suggestion">

    /**
     * {@inheritDoc}
     */
    @Transactional
    public ConfirmSuggestion confirmOnSuggestion(
            @NotNull final SuggestResponse request, final int quantity) {

        final int taxonQuantity = request.getTaxonQuantity();
        if (quantity > taxonQuantity) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Taxon quantity is less than confirm quantity");
        }

        final var itemMaps = request
                .getDetails().stream().collect(Collectors.toUnmodifiableMap(
                        detail -> detail.getIngredient().getCode(),
                        detail -> detail.getQuantity() * quantity));
        var remainItemMap = new ArrayList<IngredientSuggestion>();
        var suggestItemMap = new ArrayList<IngredientSuggestion>();
        itemMaps.forEach((ingredientCode, ingredientQuantity) ->
                useItemsBaseOnConfirmTaxon(remainItemMap, suggestItemMap, ingredientCode, ingredientQuantity));

        return ConfirmSuggestion.builder()
                .ingredientRemain(remainItemMap)
                .ingredientSuggestions(suggestItemMap)
                // TODO: Add status when low stock
                .lowStockAlert(false)
                .confirmSuggestion(true)
                .taxonQuantity(taxonQuantity)
                .build();
    }

    @Transactional
    protected void useItemsBaseOnConfirmTaxon(
            @NotNull List<IngredientSuggestion> remainItemMap,
            @NotNull List<IngredientSuggestion> suggestItemMap,
            final @NotNull @NotEmpty String ingredientCode,
            final @NotNull Float ingredientQuantity) {
        final var ingredient = this.ingredientService.getByCode(ingredientCode);
        if (Objects.isNull(ingredient)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    String.format("Ingredient item is not found by code '%s'", ingredientCode));
        }

        final var items = this.itemService.getAllByIngredient(
                ingredient.getClientId(), ingredient);
        final var itemSize = items.size();
        final var suggestionSize = ingredientQuantity.intValue();
        if (itemSize < suggestionSize) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Ingredient item is required more than the inventory contains");
        }
        final var deletedItems = items.subList(0, ingredientQuantity.intValue());
        this.itemService.deleteAll(deletedItems);
        final var ingredientDto = IngredientMapper.toDto(
                this.ingredientService.getByCode(ingredientCode));
        remainItemMap.add(IngredientSuggestion.builder().ingredient(ingredientDto)
                .quantity(itemSize - suggestionSize).build());
        suggestItemMap.add(IngredientSuggestion.builder().ingredient(ingredientDto)
                .quantity(suggestionSize).build());
    }

    //</editor-fold>

    //<editor-fold desc="UTILITIES">

    /**
     * Check activeness and nullable of a dto
     * Return true if object is not null and is active or els false
     * @param obj   BaseDto
     * @return      boolean
     */
    private boolean isActive(BaseDto<?> obj) {
        return nonNull(obj) && obj.isActive();
    }

    /**
     * getQuantitySuggest
     * @param ingredientsInInventory    Map&lt;Long, InventoryEntity&gt;
     * @param minQuantitySuggest        int
     * @param detail                    RecipeDetailDto
     * @return                          int
     */
    private int sentinelMinSuggestionQuantity(
            Map<Long, InventoryEntity>  ingredientsInInventory,
            int                         minQuantitySuggest,
            RecipeDetailDto             detail
    ) {
        // Get minimum value between two value of quantity
        return Math.min(

                // Get the quantity that can be made by the available item compared to the required quantity
                (int) floor(getInventoryQuantity(ingredientsInInventory, detail) / detail.getQuantity()),

                // the current smallest number of quantity
                minQuantitySuggest
        );
    }

    /**
     * Get item quantity from map of inventory based on ingredient id
     * @param ingredientsInInventory    Map&lt;Long, InventoryEntity&gt;
     * @param detail                    RecipeDetailDto
     * @return                          float
     */
    @Min(value = 0, message = "Quantity should be equal or greater than zero")
    private float getInventoryQuantity(
            Map<Long, InventoryEntity>  ingredientsInInventory,
            RecipeDetailDto             detail
    ) {
        return ingredientsInInventory.get(detail.getIngredient().getId()).getQuantity();
    }

    /**
     * Check inventory item is enough to suggest a recipe
     * @param ingredientsInInventory    Map&lt;Long, InventoryEntity&gt;
     * @param detail                    RecipeDetailDto
     * @return                          boolean
     */
    private boolean isInventoryHasEnough(
            Map<Long, InventoryEntity>  ingredientsInInventory,
            RecipeDetailDto             detail
    ) {
        return detail.getQuantity() <= getInventoryQuantity(ingredientsInInventory, detail);
    }

    /**
     * Construct suggest response from information
     * @param recipe                RecipeDto
     * @param minQuantitySuggest    int
     * @param details               List&lt;RecipeDetailDto&gt;
     * @return                      SuggestResponse
     */
    private SuggestResponse buildSuggestResponse(
            RecipeDto                   recipe,
            int                         minQuantitySuggest,
            List<RecipeDetailDto>       details
    ) {
        return SuggestResponse.builder()
                .recipe(recipe)
                .taxonQuantity(minQuantitySuggest)
                .details(details)
                .build();
    }

    /**
     * Check for a valid suggestion
     * @param minQuantitySuggest    int
     * @param details               List&lt;RecipeDetailDto&gt;
     * @return                      boolean
     */
    private boolean isValidSuggestion(
            int                         minQuantitySuggest,
            List<RecipeDetailDto>       details
    ) {
        return

            // Quantity must not MAX_VALUE of int
            minQuantitySuggest != MAX_VALUE &&

            // Quantity must be positive
            minQuantitySuggest > 0 &&

            // detail list of recipe must not be empty
            !details.isEmpty();
    }

    //</editor-fold>
}
