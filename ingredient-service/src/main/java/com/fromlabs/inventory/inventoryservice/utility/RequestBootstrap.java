package com.fromlabs.inventory.inventoryservice.utility;

import com.fromlabs.inventory.inventoryservice.ingredient.IngredientService;
import com.fromlabs.inventory.inventoryservice.ingredient.beans.*;
import com.fromlabs.inventory.inventoryservice.ingredient.config.beans.request.IngredientConfigRequest;
import com.fromlabs.inventory.inventoryservice.inventory.InventoryService;
import com.fromlabs.inventory.inventoryservice.inventory.beans.*;
import com.fromlabs.inventory.inventoryservice.item.ItemService;
import com.fromlabs.inventory.inventoryservice.item.beans.*;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

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
    public Object setTenantBoostrap(Long tenantId, IngredientPageRequest request) {
        request.setClientId(tenantId);
        return logWrapper(request, "setTenantBoostrap: {}");
    }

    /**
     * Page bootstrap for get all inventory
     * @param tenantId  Client ID
     * @param request   InventoryPageRequest
     * @return Object
     */
    public Object setTenantBoostrap(Long tenantId, InventoryPageRequest request) {
        request.setClientId(tenantId);
        return logWrapper(request, "setTenantBoostrap: {}");
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
    public Object deleteItemsBootstrap(
            Long                    tenantId,
            ItemDeleteAllRequest    request,
            IngredientService ingredientService,
            InventoryService inventoryService,
            ItemService itemService
    ) {
        final var ingredient = ingredientService.get(Objects.requireNonNull(request.getIngredientId()));
        syncIngredientInInventory(tenantId, ingredient, inventoryService, itemService);
        request.setClientId(tenantId);
        return logWrapper(request, "deleteItemsBootstrap: {}");
    }

    /**
     * Page bootstrap for get all ingredient category and type
     * @param tenantId  Client ID
     * @param request   IngredientRequest
     * @return Object
     */
    public Object setTenantBoostrap(Long tenantId, IngredientRequest request) {
        request.setClientId(tenantId);
        return logWrapper(request, "setTenantBoostrap: {}");
    }

    /**
     * Page bootstrap for get all ingredient category and type
     * @param tenantId  Client ID
     * @param request   IngredientRequest
     * @return Object
     */
    public Object setTenantBoostrap(Long tenantId, ItemRequest request) {
        request.setClientId(tenantId);
        return logWrapper(request, "setTenantBoostrap: {}");
    }

    /**
     * Page bootstrap for get all ingredient category and type
     * @param tenantId  Client ID
     * @param request   IngredientRequest
     * @return Object
     */
    public Object setTenantBoostrap(Long tenantId, ItemPageRequest request) {
        request.setClientId(tenantId);
        return logWrapper(request, "setTenantBoostrap: {}");
    }

    /**
     * Page bootstrap for get all ingredient category and type
     * @param tenantId  Client ID
     * @param id        Entity ID
     * @param request   IngredientConfigRequest
     * @return Object
     */
    public Object setIdAndTenantBoostrap(Long tenantId, Long id, IngredientConfigRequest request) {
        request.setClientId(tenantId);
        request.setId(id);
        return logWrapper(request, "setIdAndTenantBoostrap: {}");
    }

    /**
     * Page bootstrap for get all ingredient category and type
     * @param tenantId  Client ID
     * @param id        Entity ID
     * @param request   IngredientConfigRequest
     * @return Object
     */
    public Object setIdAndTenantBoostrap(Long tenantId, Long id, ItemRequest request) {
        request.setClientId(tenantId);
        request.setId(id);
        return logWrapper(request, "setIdAndTenantBoostrap: {}");
    }

    /**
     * Bootstrap for get all ingredient category and type
     * @param tenantId  Client ID
     * @param id        Entity ID
     * @param request   IngredientConfigRequest
     * @return Object
     */
    public Object setIdAndTenantBootstrap(Long tenantId, Long id, InventoryRequest request) {
        request.setClientId(tenantId);
        request.setId(id);
        return logWrapper(request, "setIdAndTenantBootstrap");
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
