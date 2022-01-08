/*
 * Copyright (c) 2021. Liem Doan
 */

package com.fromlabs.inventory.inventoryservice.applications;

import com.fromlabs.inventory.inventoryservice.ingredient.beans.request.IngredientPageRequest;
import com.fromlabs.inventory.inventoryservice.ingredient.beans.request.IngredientRequest;
import com.fromlabs.inventory.inventoryservice.ingredient.config.beans.request.IngredientConfigRequest;
import com.fromlabs.inventory.inventoryservice.ingredient.track.beans.request.IngredientHistoryPageRequest;
import com.fromlabs.inventory.inventoryservice.inventory.beans.request.InventoryPageRequest;
import com.fromlabs.inventory.inventoryservice.inventory.beans.request.InventoryRequest;
import com.fromlabs.inventory.inventoryservice.item.beans.request.BatchItemsRequest;
import com.fromlabs.inventory.inventoryservice.item.beans.request.ItemDeleteAllRequest;
import com.fromlabs.inventory.inventoryservice.item.beans.request.ItemPageRequest;
import com.fromlabs.inventory.inventoryservice.item.beans.request.ItemRequest;
import org.springframework.http.ResponseEntity;

/**
 * Application Controller
 * @author Liem
 */
public interface ApplicationController {

    //<editor-fold desc="EVENT">

    ResponseEntity<?> getLabelValueEvent(
            Long clientId
    );

    //</editor-fold>

    //<editor-fold desc="STATUS">

    ResponseEntity<?> getLabelValueStatus();

    //</editor-fold>

    //<editor-fold desc="HISTORY">

    /**
     * Get all history as list by ingredient id
     * @param tenantId      Tenant ID
     * @param ingredientId  Ingredient unique id
     * @return              ResponseEntity
     */
    ResponseEntity<?> getAllHistoryByIngredient(
            Long tenantId,
            Long ingredientId
    );

    /**
     * Get history by id
     * @param id    History unique id
     * @return      ResponseEntity
     */
    ResponseEntity<?> getHistoryById(
            Long id
    );

    /**
     * Get page of history with filter
     * @param tenantId  Tenant ID
     * @param request   IngredientHistoryPageRequest
     * @return          ResponseEntity
     */
    ResponseEntity<?> getPageHistory(
            Long tenantId,
            IngredientHistoryPageRequest request
    );

    //</editor-fold>

    //<editor-fold desc="INGREDIENT">

    //<editor-fold desc="CATEGORY">

    /**
     * Return a ResponseEntity object of all ingredient category (ingredient parent) with pagination
     * @param tenantId  The client id (must be provided)
     * @param request   The request that contain page. size, sort and filter options for ingredient category
     * @return          ResponseEntity
     */
    ResponseEntity<?> getPageIngredientCategory(
            Long tenantId,
            IngredientPageRequest request
    );

    /**
     * Return a ResponseEntity object of all ingredient category (ingredient parent) as a list
     * @param tenantId  The client id (must be provided)
     * @return          ResponseEntity
     */
    ResponseEntity<?> getAllIngredientCategory(
            Long tenantId
    );

    //</editor-fold>

    //<editor-fold desc="TYPE">

    /**
     * Return a ResponseEntity object of all ingredient type (ingredient child) with pagination
     * @param tenantId  The client id (must be provided)
     * @param request   The request that contain page. size, sort and filter options for ingredient type
     * @return          ResponseEntity
     */
    ResponseEntity<?> getPageIngredientType(
            Long tenantId,
            IngredientPageRequest request
    );

    /**
     * Return a ResponseEntity object of all ingredient type (ingredient child) as a list
     * @param tenantId  The client id (must be provided)
     * @param parentId  The id of a parent category ingredient (must be provided)
     * @return          ResponseEntity
     */
    ResponseEntity<?> getAllIngredientType(
            Long tenantId,
            Long parentId
    );

    //</editor-fold>

    //<editor-fold desc="GENERAL">

    /**
     * Return a ResponseEntity object of an ingredient (both parent and child ingredient) with code
     * @param tenantId  The client id (must be provided)
     * @param code      The unique code for identify a ingredient
     * @return          ResponseEntity
     */
    ResponseEntity<?> getIngredient(
            Long tenantId,
            String code
    );

    /**
     * Return a ResponseEntity object of an ingredient (both parent and child ingredient) with ID
     * @param tenantId  The client id (must be provided)
     * @param id        The ID (unique) of an ingredient
     * @return          ResponseEntity
     */
    ResponseEntity<?> getIngredient(
            Long tenantId,
            Long id
    );

    /**
     * Return a ResponseEntity object of an ingredient after save
     * @param tenantId  The client id (must be provided)
     * @param request   The request contain information to create an ingredient
     * @return          ResponseEntity
     */
    ResponseEntity<?> saveIngredient(
            Long tenantId,
            String apiKey,
            IngredientRequest request
    );

    /**
     * Return a ResponseEntity object of an ingredient after save
     * @param tenantId  The client id (must be provided)
     * @param request   The request contain information to update an existing ingredient
     * @return          ResponseEntity
     */
    ResponseEntity<?> updateIngredient(
            Long tenantId,
            String apiKey,
            String principal,
            IngredientRequest request
    );

    /**
     * Return a ResponseEntity object of empty after delete
     * @param tenantId  The client id (must be provided)
     * @param id        The ID (unique) of an ingredient
     * @return          ResponseEntity
     */
    ResponseEntity<?> deleteIngredient(
            Long tenantId,
            String apiKey,
            String principal,
            Long id
    );

    //</editor-fold>

    //<editor-fold desc="CONFIGURATION">

    /**
     * Update ingredient config
     * @param tenantId  Client ID
     * @param id        Config id
     * @param request   IngredientConfigRequest
     * @return          ResponseEntity
     */
    ResponseEntity<?> updateConfig(
            Long tenantId,
            Long id,
            IngredientConfigRequest request
    );

    //</editor-fold>

    //<editor-fold desc="UNIT">

    /**
     * Get all unit inside a unit type
     * @param unit  Ingredient unit type
     * @return      ResponseEntity
     */
    ResponseEntity<?> getIngredientUnit(
            String unit
    );

    /**
     * Get all unit type of ingredient
     * @return  ResponseEntity
     */
    ResponseEntity<?> getIngredientUnit();

    //</editor-fold>

    //</editor-fold>

    //<editor-fold desc="INVENTORY">

    /**
     * Return a ResponseEntity object of all inventory with pagination
     * @param tenantId  The client id (must be provided)
     * @param request   The request that contain page. size, sort and filter options for inventory type
     * @return          ResponseEntity
     */
    ResponseEntity<?> getPageInventory(
            Long tenantId,
            InventoryPageRequest request
    );

    /**
     * Return a ResponseEntity object of all inventory as list
     * @param tenantId  The client id (must be provided)
     * @return          ResponseEntity
     */
    ResponseEntity<?> getAllInventory(
            Long tenantId
    );

    /**
     * Return a ResponseEntity object of boolean result after sync
     * <p>
     * Sync means that the information (quantity) of the items which has the same ingredient will be summarized
     * and create (if that figure is not exist) or update (if that figure is already exist).
     * @param tenantId  The client id (must be provided)
     * @return          ResponseEntity
     */
    ResponseEntity<?> syncInventory(
            Long tenantId
    );

    /**
     * Update inventory by id
     * @param tenantId  client id
     * @param request   InventoryRequest
     * @param id        ingredient id
     * @return          ResponseEntity
     */
    ResponseEntity<?> updateInventory(
            Long tenantId,
            InventoryRequest request,
            Long id
    );

    /**
     * Get an ingredient by id
     * @param tenantId  Client id
     * @param id        Ingredient id
     * @return          ResponseEntity
     */
    ResponseEntity<?> getInventoryById(
            Long tenantId,
            Long id
    );

    /**
     * Return a ResponseEntity object of all item with pagination
     * @param tenantId  The client id (must be provided)
     * @param request   The request that contain page. size, sort and filter options for inventory type
     * @return          ResponseEntity
     */
    ResponseEntity<?> getPageItem(
            Long tenantId,
            ItemPageRequest request
    );

    /**
     * Return a ResponseEntity object of all item as list
     * @param tenantId  The client id (must be provided)
     * @return          ResponseEntity
     */
    ResponseEntity<?> getAllItem(
            Long tenantId
    );

    //</editor-fold>

    //<editor-fold desc="ITEM">

    /**
     * Return a ResponseEntity object of item by id
     * @param id        ID of item (must be provided)
     * @return          ResponseEntity
     */
    ResponseEntity<?> getItemById(
            Long id
    );

    /**
     * Save item
     * @param tenantId  The client id (must be provided)
     * @param request   ItemRequest
     * @return          ResponseEntity
     */
    ResponseEntity<?> saveItem(
            Long tenantId,
            ItemRequest request
    );

    /**
     * Save batch of item by request
     * @param tenantId  Tenant ID
     * @param request   BatchItemsRequest
     * @return          ResponseEntity
     */
    ResponseEntity<?> saveBatchItems(
            Long tenantId,
            BatchItemsRequest request
    );

    /**
     * Update item entity by ID
     * @param tenantId  The client id (must be provided)
     * @param id        ID of item
     * @param request   ItemRequest
     * @return          ResponseEntity
     */
    ResponseEntity<?> updateItem(
            Long tenantId,
            Long id,
            ItemRequest request
    );

    /**
     * Delete item entity by id
     * @param tenantId  The client id (must be provided)
     * @param id        ID of item
     * @return          ResponseEntity
     */
    ResponseEntity<?> deleteItem(
            Long tenantId,
            Long id
    );

    /**
     * Return an ResponseEntity object. Delete multiple item entities.
     * @param tenantId  The client id (must be provided)
     * @param request   ItemDeleteAllRequest
     * @return          ResponseEntity
     */
    ResponseEntity<?> deleteAllItemsAfterConfirm(
            Long tenantId,
            ItemDeleteAllRequest request
    );

    //</editor-fold>
}
