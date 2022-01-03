package com.fromlabs.inventory.inventoryservice.utility;

import com.fromlabs.inventory.inventoryservice.ingredient.IngredientService;
import com.fromlabs.inventory.inventoryservice.ingredient.beans.request.IngredientPageRequest;
import com.fromlabs.inventory.inventoryservice.ingredient.beans.request.IngredientRequest;
import com.fromlabs.inventory.inventoryservice.ingredient.config.beans.request.IngredientConfigRequest;
import com.fromlabs.inventory.inventoryservice.ingredient.track.beans.request.IngredientHistoryPageRequest;
import com.fromlabs.inventory.inventoryservice.inventory.InventoryService;
import com.fromlabs.inventory.inventoryservice.inventory.beans.request.InventoryPageRequest;
import com.fromlabs.inventory.inventoryservice.inventory.beans.request.InventoryRequest;
import com.fromlabs.inventory.inventoryservice.item.ItemService;
import com.fromlabs.inventory.inventoryservice.item.beans.request.BatchItemsRequest;
import com.fromlabs.inventory.inventoryservice.item.beans.request.ItemDeleteAllRequest;
import com.fromlabs.inventory.inventoryservice.item.beans.request.ItemPageRequest;
import com.fromlabs.inventory.inventoryservice.item.beans.request.ItemRequest;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import javax.validation.constraints.NotNull;
import java.util.Objects;

import static com.fromlabs.inventory.inventoryservice.utility.TransactionLogic.*;

/**
 * <h1>Bootstrap layer</h1>
 *
 * <h2>Brief Information</h2>
 *
 * <p>
 *     This the first layer in the segment of non-infrastructure service layer.
 *     In detail, it play as the point where define the request state
 * </p>
 *
 * <p>
 *     It is a optional layer as there may be some transaction which the boostrap
 *     of request state is not necessary
 * </p>
 *
 * <h2>Usage</h2>
 *
 * <p>
 *     The logic for bootstrapping is as simple as a piece of cake.
 *     It will carry out exactly how developer want the state of request to be
 * </p>
 */
@Slf4j
@UtilityClass
public class RequestBootstrap {

    /**
     * Page bootstrap for get all ingredient category and type
     * @param tenantId  Client ID
     * @param request   IngredientPageRequest
     * @return Object
     */
    public Object bootstrapTenantAndPreprocessIngredientPageRequest(
            @NotNull final Long            tenantId,
            @NotNull IngredientPageRequest request
    ) {
        request.setClientId(tenantId);
        request.setCode(request.getCode().strip());
        request.setName(request.getName().strip());
        request.setUnit(request.getUnit().strip());
        request.setUnitType(request.getUnitType().strip());
        request.setDescription(request.getDescription().strip());
        request.setCreateAt(request.getCreateAt().strip());
        request.setUpdateAt(request.getUpdateAt().strip());
        return logWrapper(request, "setTenantBoostrap: {}");
    }

    /**
     * Page bootstrap for get all ingredient category and type
     * @param tenantId  Client ID
     * @param request   IngredientPageRequest
     * @return Object
     */
    public Object bootstrapTenantPreprocessIngredientHistoryPageRequest(
            @NotNull final Long tenantId,
            @NotNull IngredientHistoryPageRequest request
    ) {
        request.setClientId(tenantId);
        request.setName(request.getName().strip());
        request.setDescription(request.getDescription().strip());
        request.setUpdateAt(request.getUpdateAt().strip());
        request.setActorName(request.getActorName().strip());
        request.setActorRole(request.getActorRole().strip());
        return logWrapper(request, "IngredientHistoryPageRequest: {}");
    }

    /**
     * Page bootstrap for get all inventory
     * @param tenantId  Client ID
     * @param request   InventoryPageRequest
     * @return Object
     */
    public Object bootstrapTenantAndPreprocessInventoryPageRequest(
            @NotNull final Long tenantId,
            @NotNull InventoryPageRequest request
    ) {
        request.setClientId(tenantId);
        request.setName(request.getName().strip());
        request.setUnit(request.getUnit().strip());
        request.setUnitType(request.getUnitType().strip());
        request.setDescription(request.getDescription().strip());
        request.setUpdateAt(request.getUpdateAt().strip());
        return logWrapper(request, "InventoryPageRequest: {}");
    }

    /**
     * Delete items request bootstrap
     * @param tenantId          Tenant ID
     * @param request           ItemDeleteAllRequest
     * @param ingredientService IngredientService
     * @param inventoryService  InventoryService
     * @param itemService       ItemService
     * @return                  Object
     */
    public Object bootstrapTenantItemDeleteAllRequest(
            @NotNull final Long                    tenantId,
            @NotNull ItemDeleteAllRequest          request,
            @NotNull final IngredientService       ingredientService,
            @NotNull final InventoryService        inventoryService,
            @NotNull final ItemService             itemService
    ) {
        final var ingredient = ingredientService.getById(Objects.requireNonNull(request.getIngredientId()));
        syncIngredientInInventory(tenantId, ingredient, inventoryService, itemService);
        request.setClientId(tenantId);
        return logWrapper(request, "ItemDeleteAllRequest: {}");
    }

    /**
     * Page bootstrap for get all ingredient category and type
     * @param tenantId  Client ID
     * @param request   IngredientRequest
     * @return Object
     */
    public Object bootstrapTenantAndPreprocessIngredientRequest(
            @NotNull final Long        tenantId,
            @NotNull IngredientRequest request
    ) {
        request.setClientId(tenantId);
        request.setCode(request.getCode().strip());
        request.setName(request.getName().strip());
        request.setActorName(request.getActorName().strip());
        request.setActorRole(request.getActorRole().strip());
        request.setUnit(request.getUnit().strip());
        request.setUnitType(request.getUnitType().strip());
        request.setDescription(request.getDescription().strip());
        return logWrapper(request, "IngredientRequest: {}");
    }

    /**
     * Page bootstrap for get all ingredient category and type
     * @param tenantId  Client ID
     * @param request   IngredientRequest
     * @return Object
     */
    public Object bootstrapTenantAndPreprocessItemRequest(
            @NotNull final Long  tenantId,
            @NotNull ItemRequest request
    ) {
        request.setClientId(tenantId);
        request.setCode(request.getCode().strip());
        request.setName(request.getName().strip());
        request.setActorName(request.getActorName().strip());
        request.setActorRole(request.getActorRole().strip());
        request.setUnit(request.getUnit().strip());
        request.setUnitType(request.getUnitType().strip());
        request.setDescription(request.getDescription().strip());
        return logWrapper(request, "ItemRequest: {}");
    }

    /**
     * Page bootstrap for get all ingredient category and type
     * @param tenantId  Client ID
     * @param request   IngredientRequest
     * @return Object
     */
    public Object bootstrapTenantAndPreprocessItemPageRequest(
            @NotNull final Long      tenantId,
            @NotNull ItemPageRequest request
    ) {
        request.setClientId(tenantId);
        request.setCode(request.getCode().strip());
        request.setName(request.getName().strip());
        request.setUnit(request.getUnit().strip());
        request.setUnitType(request.getUnitType().strip());
        request.setDescription(request.getDescription().strip());
        request.setUpdateAt(request.getUpdateAt().strip());
        return logWrapper(request, "setTenantBoostrap: {}");
    }

    /**
     * Page bootstrap for get all ingredient category and type
     * @param tenantId  Client ID
     * @param id        Entity ID
     * @param request   IngredientConfigRequest
     * @return Object
     */
    public Object bootstrapTenantAndIdIngredientConfigRequest(
            @NotNull final Long tenantId,
            @NotNull final Long id,
            @NotNull IngredientConfigRequest request
    ) {
        request.setClientId(tenantId);
        request.setId(id);
        return logWrapper(request, "IngredientConfigRequest: {}");
    }

    /**
     * Page bootstrap for get all ingredient category and type
     * @param tenantId  Client ID
     * @param id        Entity ID
     * @param request   IngredientConfigRequest
     * @return Object
     */
    public Object bootstrapTenantAndIdtAndPreprocessItemRequest(
            @NotNull final Long  tenantId,
            @NotNull final Long  id,
            @NotNull ItemRequest request
    ) {
        request.setClientId(tenantId);
        request.setId(id);
        request.setCode(request.getCode().strip());
        request.setName(request.getName().strip());
        request.setActorName(request.getActorName().strip());
        request.setActorRole(request.getActorRole().strip());
        request.setUnit(request.getUnit().strip());
        request.setUnitType(request.getUnitType().strip());
        request.setDescription(request.getDescription().strip());
        return logWrapper(request, "ItemRequest: {}");
    }

    /**
     * Bootstrap for get all ingredient category and type
     * @param tenantId  Client ID
     * @param id        Entity ID
     * @param request   IngredientConfigRequest
     * @return Object
     */
    public Object bootstrapTenantAndIdAndPreprocessInventoryRequest(
            @NotNull final Long         tenantId,
            @NotNull final Long         id,
            @NotNull InventoryRequest request
    ) {
        request.setClientId(tenantId);
        request.setId(id);
        request.setName(request.getName().strip());
        request.setDescription(request.getDescription().strip());
        return logWrapper(request, "InventoryRequest: {}");
    }

    /**
     * Bootstrap tenant id for add all items
     * @param tenantId  Tenant ID
     * @param request   BatchItemsRequest
     * @return          Object
     */
    public Object bootstrapTenantAndPreprocessBatchItemRequest(
            @NotNull final Long tenantId,
            @NotNull BatchItemsRequest request
    ) {
        request.setClientId(tenantId);
        request.setCode(request.getCode().strip());
        request.setName(request.getName().strip());
        request.setActorName(request.getActorName().strip());
        request.setActorRole(request.getActorRole().strip());
        request.setUnit(request.getUnit().strip());
        request.setUnitType(request.getUnitType().strip());
        request.setDescription(request.getDescription().strip());
        return logWrapper(request, "BatchItemsRequest: {}");
    }

    /**
     * Log wrapper
     * @param result    Object
     * @param message   Log message
     * @return  boolean
     */
    private Object logWrapper(Object result, String message) {
        log.info(message, result);
        return result;
    }
}
