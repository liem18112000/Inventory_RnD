/*
 * Copyright (c) 2021. Liem Doan
 */

package com.fromlabs.inventory.inventoryservice.applications;

import com.fromlabs.inventory.inventoryservice.common.helper.FLStringUtils;
import com.fromlabs.inventory.inventoryservice.common.template.WebStatefulTemplateProcess;
import com.fromlabs.inventory.inventoryservice.common.template.manager.TemplateProcessCacheManger;
import com.fromlabs.inventory.inventoryservice.config.versions.ApiV1;
import com.fromlabs.inventory.inventoryservice.ingredient.IngredientService;
import com.fromlabs.inventory.inventoryservice.ingredient.beans.*;
import com.fromlabs.inventory.inventoryservice.ingredient.config.beans.request.IngredientConfigRequest;
import com.fromlabs.inventory.inventoryservice.inventory.InventoryService;
import com.fromlabs.inventory.inventoryservice.inventory.beans.*;
import com.fromlabs.inventory.inventoryservice.item.ItemService;
import com.fromlabs.inventory.inventoryservice.item.beans.*;
import com.fromlabs.inventory.inventoryservice.utility.TransactionLogic;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.fromlabs.inventory.inventoryservice.config.AppConfig.*;
import static com.fromlabs.inventory.inventoryservice.ingredient.unit.IngredientUnit.*;
import static com.fromlabs.inventory.inventoryservice.utility.RequestBootstrap.*;
import static com.fromlabs.inventory.inventoryservice.utility.TemplateProcessDirector.*;
import static java.util.Objects.nonNull;
import static org.springframework.http.ResponseEntity.*;

/**
 * IngredientController is REST controller which responsible for
 * all C.R.U.D operations of IngredientEntity. InventoryEntity and ItemEntity.
 * @see <a href="https://spring.io/guides/tutorials/rest/">REST Controller example : Building REST services with Spring</a>
 */
@Slf4j
@RestController
@RequestMapping(value = "${application.base-url}/" + ApiV1.URI_API, produces = ApiV1.MIME_API)
public class IngredientController implements ApplicationController {

    // <editor-fold desc="SETUP">

    private final IngredientService             ingredientService;
    private final InventoryService              inventoryService;
    private final ItemService                   itemService;
    private final TemplateProcessCacheManger    processCache;

    public static final String SERVICE_PATH = "/ingredient/";
    protected final String API_VERSION      = ApiV1.VERSION;

    /**
     * Summarize API path with HTTP method
     * @param method    HttpMethod
     * @param subPath   Sub path
     * @return          String
     */
    private String path(HttpMethod method, String subPath) {
        return TransactionLogic.path(method, SERVICE_PATH, subPath, API_VERSION);
    }

    /**
     * Generate process key for process cache manager
     * @param rawProcessKey Raw process key from builder director of template process
     * @param tenantId      Tenant ID
     * @return              String
     */
    private String generateProcessKey(String rawProcessKey, Long tenantId) {
        return rawProcessKey.concat("_").concat(API_VERSION).concat("_").concat(TENANT_ID.concat("@").concat(String.valueOf(tenantId)));
    }

    /**
     * The constructor is initialized with three parameter (see in Parameters)
     * @param ingredientService     The service of IngredientEntity
     * @param inventoryService      The service of InventoryEntity
     * @param itemService           The service of ItemEntity
     * @param processCache          The service of TemplateProcessCache
     */
    public IngredientController(
            IngredientService           ingredientService,
            InventoryService            inventoryService,
            ItemService                 itemService,
            TemplateProcessCacheManger  processCache
    ) {
        this.ingredientService  = ingredientService;
        this.inventoryService   = inventoryService;
        this.itemService        = itemService;
        this.processCache       = processCache;
        trackControllerDependencyInjectionInformation(
                this.ingredientService,
                this.inventoryService,
                this.itemService,
                this.processCache
        );
    }

    /**
     * Track all injected dependencies to Application Controller
     * @param ingredientService     IngredientService
     * @param inventoryService      InventoryService
     * @param itemService           ItemService
     * @param processCache          TemplateProcessCacheManger
     */
    private void trackControllerDependencyInjectionInformation(
            IngredientService           ingredientService,
            InventoryService            inventoryService,
            ItemService                 itemService,
            TemplateProcessCacheManger  processCache
    ) {
        log.info("Ingredient service : {}",             ingredientService.getClass().getName());
        log.info("Inventory service : {}",              inventoryService.getClass().getName());
        log.info("Item service : {}",                   itemService.getClass().getName());
        log.info("Template Process Cache Manager : {}", processCache.getClass().getName());
        log.info("Application controller : {}",         this.getClass().getName());
    }

    /**
     * Check process can be gotten from cache or not.
     * If key is existed and state data is changed return true.
     * Otherwise false
     *
     * @param request       Generic request
     * @param key           String
     * @param processCache  TemplateProcessCacheManger
     * @return              boolean
     */
    private boolean isProcessCanBeGetFromCache(
            Object                      request,
            String                      key,
            TemplateProcessCacheManger  processCache
    ) {

        // Check existence of process manager and request
        assert nonNull(processCache);
        assert nonNull(request);

        // CHeck key must not be null
        assert !FLStringUtils.isNullOrEmpty(key);

        // Check template process is not null
        final var templateProcess = processCache.getTemplateProcessByKey(key);
        assert nonNull(templateProcess);

        // Get main condition
        // Check process exist by key
        return  processCache.checkProcessExistByKey(key) &&
                // Check process template data state is not changed and
                // Template process must be WebStatefulTemplateProcess
                (!(templateProcess instanceof WebStatefulTemplateProcess) ||
                !((WebStatefulTemplateProcess) processCache.getTemplateProcessByKey(key)).isStateChanged(request));
    }

    /**
     * Clear process
     * @return  ResponseEntity
     */
    @PostMapping("clear-processes")
    public ResponseEntity<?> clearProcessCache() {
        log.info(path(HttpMethod.POST, "clear-processes"));
        this.processCache.clearCache();
        return ok().build();
    }

    // </editor-fold>

    // <editor-fold desc="INGREDIENT">

    // <editor-fold desc="CATEGORY">

    /**
     * Return a ResponseEntity object of all ingredient category (ingredient parent) with pagination
     * @param tenantId  The client id (must be provided)
     * @param request   The request that contain page. size, sort and filter options for ingredient category
     * @return          ResponseEntity
     */
    @PostMapping("category/page")
    public ResponseEntity<?> getPageIngredientCategory(
            @RequestHeader(TENANT_ID) Long tenantId,
            @RequestBody IngredientPageRequest request
    ){
        log.info(path(HttpMethod.POST, "category/page"));
        final var key = generateProcessKey(PROCESS_KEY_GET_PAGE_INGREDIENT_CATE, tenantId);
        return (ResponseEntity<?>) (isProcessCanBeGetFromCache(setTenantBoostrap(tenantId, request), key, processCache) ?
                processCache.getTemplateProcessByKeyThenRun(key) : processCache.forceCacheTemplateProcessThenRun(key,
                buildGetPageIngredientCategoryTemplateProcess(tenantId, request, ingredientService, inventoryService)));
    }

    /**
     * Return a ResponseEntity object of all ingredient category (ingredient parent) as a list
     * @param tenantId  The client id (must be provided)
     * @return          ResponseEntity
     */
    @GetMapping("category/all")
    public ResponseEntity<?> getAllIngredientCategory(
            @RequestHeader(TENANT_ID) Long tenantId
    ){
        log.info(path(HttpMethod.GET, "category/all"));
        return (ResponseEntity<?>) buildGetAllIngredientCategoryTemplateProcess(tenantId, ingredientService).run();
    }

    // </editor-fold>

    // <editor-fold desc="TYPE">

    /**
     * Return a ResponseEntity object of all ingredient type (ingredient child) with pagination
     * @param tenantId  The client id (must be provided)
     * @param request   The request that contain page. size, sort and filter options for ingredient type
     * @return          ResponseEntity
     */
    @PostMapping("type/page")
    public ResponseEntity<?> getPageIngredientType(
            @RequestHeader(TENANT_ID) Long tenantId,
            @RequestBody IngredientPageRequest request
    ){
        log.info(path(HttpMethod.POST, "type/page"));
        final var key = generateProcessKey(PROCESS_KEY_GET_PAGE_INGREDIENT_TYPE, tenantId);
        return (ResponseEntity<?>) (isProcessCanBeGetFromCache(setTenantBoostrap(tenantId, request), key, processCache) ?
                processCache.getTemplateProcessByKeyThenRun(key) : processCache.forceCacheTemplateProcessThenRun(key,
                buildGetPageIngredientTypeTemplateProcess(tenantId, request, ingredientService, inventoryService)));
    }

    /**
     * Return a ResponseEntity object of all ingredient type (ingredient child) as a list
     * @param tenantId  The client id (must be provided)
     * @param parentId  The id of a parent category ingredient (must be provided)
     * @return          ResponseEntity
     */
    @GetMapping("type/all")
    public ResponseEntity<?> getAllIngredientType(
            @RequestHeader(TENANT_ID) Long tenantId,
            @RequestParam(PARENT_ID) Long parentId
    ){
        log.info(path(HttpMethod.GET, "type/all"));
        return (ResponseEntity<?>) buildGetAllIngredientTypeTemplateProcess(tenantId, parentId, ingredientService).run();
    }

    /**
     * Get label value ingredient ID
     * @param tenantId  Tenant ID
     * @return          ResponseEntity
     */
    @GetMapping("type/simple")
    public ResponseEntity<?> getLabelValueIngredientType(
            @RequestHeader(TENANT_ID) Long tenantId
    ) {
        log.info(path(HttpMethod.GET, "type/simple"));
        final var key = generateProcessKey(PROCESS_KEY_GET_LABEL_VALUE_INGREDIENT_TYPE, tenantId);
        return (ResponseEntity<?>) (isProcessCanBeGetFromCache(tenantId, key, processCache) ?
                processCache.getTemplateProcessByKeyThenRun(key) : processCache.forceCacheTemplateProcessThenRun(key,
                buildGetLabelValueIngredientTypeTemplateProcess(tenantId, ingredientService)));
    }

    // </editor-fold>

    // <editor-fold desc="GENERAL">

    /**
     * Return a ResponseEntity object of an ingredient (both parent and child ingredient) with code
     * @param tenantId  The client id (must be provided)
     * @param code      The unique code for identify a ingredient
     * @return          ResponseEntity
     */
    @GetMapping("code")
    public ResponseEntity<?> getIngredient(
            @RequestHeader(TENANT_ID) Long tenantId,
            @RequestParam(CODE) String code
    ){
        log.info(path(HttpMethod.GET, "code"));
        return (ResponseEntity<?>) buildGetIngredientByCodeTemplateProcess(tenantId, code, ingredientService, inventoryService).run();
    }

    /**
     * Return a ResponseEntity object of an ingredient (both parent and child ingredient) with ID
     * @param tenantId  The client id (must be provided)
     * @param id        The ID (unique) of an ingredient
     * @return          ResponseEntity
     */
    @GetMapping("{id:\\d+}")
    public ResponseEntity<?> getIngredient(
            @RequestHeader(TENANT_ID) Long tenantId,
            @PathVariable(ID) Long id
    ){
        log.info(path(HttpMethod.GET, String.valueOf(id)));
        return (ResponseEntity<?>) buildGetIngredientByIdTemplateProcess(tenantId, id, ingredientService, inventoryService).run();
    }

    /**
     * Return a ResponseEntity object of an ingredient after save
     * @param tenantId  The client id (must be provided)
     * @param request   The request contain information to create an ingredient
     * @return          ResponseEntity
     */
    @PostMapping
    public ResponseEntity<?> saveIngredient(
            @RequestHeader(TENANT_ID) Long tenantId,
            @RequestBody IngredientRequest request
    ){
        log.info(path(HttpMethod.POST, ""));
        var transactFlag = new AtomicBoolean(Boolean.TRUE);
        return (ResponseEntity<?>) buildSaveIngredientTemplateProcess(tenantId, request, transactFlag, ingredientService).run();
    }

    /**
     * Return a ResponseEntity object of an ingredient after save
     * @param tenantId  The client id (must be provided)
     * @param request   The request contain information to update an existing ingredient
     * @return          ResponseEntity
     */
    @PutMapping
    public ResponseEntity<?> updateIngredient(
            @RequestHeader(TENANT_ID) Long tenantId,
            @RequestBody IngredientRequest request
    ){
        log.info(path(HttpMethod.PUT, ""));
        return (ResponseEntity<?>) buildUpdateIngredientTemplateProcess(tenantId, request, ingredientService).run();
    }

    /**
     * Return a ResponseEntity object of empty after delete
     * @param tenantId  The client id (must be provided)
     * @param id        The ID (unique) of an ingredient
     * @return          ResponseEntity
     */
    @DeleteMapping("{id:\\d+}")
    public ResponseEntity<?> deleteIngredient(
            @RequestHeader(TENANT_ID) Long tenantId,
            @PathVariable(ID) Long id
    ){
        log.info(path(HttpMethod.DELETE, String.valueOf(id)));
        return (ResponseEntity<?>) buildDeleteIngredientByIdTemplateProcess(tenantId, id, ingredientService).run();
    }

    // </editor-fold>

    // <editor-fold desc="CONFIGURATION">

    /**
     * Update ingredient config
     * @param tenantId  Client ID
     * @param id        Config id
     * @param request   IngredientConfigRequest
     * @return          ResponseEntity
     */
    @PutMapping("/config/{id:\\d+}")
    public ResponseEntity<?> updateConfig(
            @RequestHeader(TENANT_ID) Long tenantId,
            @PathVariable(ID) Long id,
            @RequestBody IngredientConfigRequest request
    ){
        log.info(path(HttpMethod.PUT, "config/".concat(String.valueOf(id))));
        return (ResponseEntity<?>) buildUpdateConfigTemplateProcess(tenantId, id, request, ingredientService).run();
    }

    // </editor-fold>

    //<editor-fold desc="UNIT">

    /**
     * Get all unit inside a unit type
     * @param unit  Ingredient unit type
     * @return      ResponseEntity
     */
    @GetMapping("/unit")
    public ResponseEntity<?> getIngredientUnit(
            @RequestParam(UNIT) String unit
    ){
        log.info(path(HttpMethod.GET, "unit"));
        switch (unit) {
            case WHOLE:     return ok(WholeUnit.values());
            case AREA:      return ok(AreaUnit.values());
            case LENGTH:    return ok(LengthUnit.values());
            case VOLUME:    return ok(VolumeUnit.values());
            case WEIGHT:    return ok(WeightUnit.values());
        }
        return ok(new ArrayList<>());
    }

    /**
     * Get all unit type of ingredient
     * @return  ResponseEntity
     */
    @GetMapping("/unit-type")
    public ResponseEntity<?> getIngredientUnit() {
        log.info(path(HttpMethod.GET, "unit-type"));
        return ok(unitTypes());
    }

    //</editor-fold>

    // </editor-fold>

    // <editor-fold desc="INVENTORY">

    /**
     * Return a ResponseEntity object of all inventory with pagination
     * @param tenantId  The client id (must be provided)
     * @param request   The request that contain page. size, sort and filter options for inventory type
     * @return          ResponseEntity
     */
    @PostMapping("inventory/page")
    public ResponseEntity<?> getPageInventory(
            @RequestHeader(TENANT_ID) Long tenantId,
            @RequestBody InventoryPageRequest request
    ){
        log.info(path(HttpMethod.POST, "inventory/page"));
        return (ResponseEntity<?>) buildGetPageInventoryTemplateProcess(tenantId, request, ingredientService, inventoryService).run();
    }

    /**
     * Return a ResponseEntity object of all inventory as list
     * @param tenantId  The client id (must be provided)
     * @return          ResponseEntity
     */
    @GetMapping("inventory/all")
    public ResponseEntity<?> getAllInventory(
            @RequestHeader(TENANT_ID) Long tenantId
    ){
        log.info(path(HttpMethod.GET, "inventory/all"));
        return (ResponseEntity<?>) buildGetAllInventoryTemplateProcess(tenantId, inventoryService).run();
    }

    /**
     * Return a ResponseEntity object of boolean result after sync
     * <p>
     * Sync means that the information (quantity) of the items which has the same ingredient will be summarized
     * and create (if that figure is not exist) or update (if that figure is already exist).
     * @param tenantId  The client id (must be provided)
     * @return          ResponseEntity
     */
    @PostMapping("inventory/sync")
    public ResponseEntity<?> syncInventory(
            @RequestHeader(TENANT_ID) Long tenantId
    ) {
        log.info(path(HttpMethod.POST, "inventory/sync"));
        return (ResponseEntity<?>) buildSyncInventoryTemplateProcess(tenantId, ingredientService, inventoryService, itemService).run();
    }

    /**
     * Return a ResponseEntity object of boolean result after sync
     * <p>
     * Sync means that the information (quantity) of the items which has the same ingredient will be summarized
     * and create (if that figure is not exist) or update (if that figure is already exist).
     * @param tenantId      The client id (must be provided)
     * @param ingredientId  Ingredient ID
     * @return              ResponseEntity
     */
    @PostMapping("inventory/sync/{ingredientId}")
    public ResponseEntity<?> syncInventoryByIngredient(
            @RequestHeader(TENANT_ID) Long tenantId,
            @PathVariable("ingredientId") Long ingredientId
    ) {
        log.info(path(HttpMethod.POST, "inventory/sync/".concat(String.valueOf(ingredientId))));
        return (ResponseEntity<?>) buildSyncInventoryByIngredientTemplateProcess(tenantId, ingredientId, ingredientService, inventoryService, itemService).run();
    }

    /**
     * Update inventory by id
     * @param tenantId  client id
     * @param request   InventoryRequest
     * @param id        ingredient id
     * @return          ResponseEntity
     */
    @PutMapping("inventory/{id:\\d+}")
    public ResponseEntity<?> updateInventory(
            @RequestHeader(TENANT_ID) Long tenantId,
            @RequestBody InventoryRequest request,
            @PathVariable(ID) Long id
    ){
        log.info(path(HttpMethod.PUT, "inventory/".concat(String.valueOf(id))));
        return (ResponseEntity<?>) buildUpdateInventoryByIdTemplateProcess(tenantId, request, id, inventoryService).run();
    }

    /**
     * Get an ingredient by id
     * @param tenantId  Client id
     * @param id        Ingredient id
     * @return          ResponseEntity
     */
    @GetMapping("inventory/{id:\\d+}")
    public ResponseEntity<?> getInventoryById(
            @RequestHeader(TENANT_ID) Long tenantId,
            @PathVariable(ID) Long id
    ){
        log.info(path(HttpMethod.GET, "inventory/".concat(String.valueOf(id))));
        return (ResponseEntity<?>) buildGetInventoryByIdTemplateProcess(tenantId, id, inventoryService).run();
    }

    // </editor-fold>

    // <editor-fold desc="ITEM">

    /**
     * Return a ResponseEntity object of all item with pagination
     * @param tenantId  The client id (must be provided)
     * @param request   The request that contain page. size, sort and filter options for inventory type
     * @return          ResponseEntity
     */
    @PostMapping("item/page")
    public ResponseEntity<?> getPageItem(
            @RequestHeader(TENANT_ID) Long tenantId,
            @RequestBody ItemPageRequest request
    ){
        log.info(path(HttpMethod.POST, "item/page"));
        return (ResponseEntity<?>) buildGetPageItemTemplateProcess(tenantId, request, ingredientService, itemService).run();
    }

    /**
     * Return a ResponseEntity object of all item as list
     * @param tenantId  The client id (must be provided)
     * @return          ResponseEntity
     */
    @GetMapping("item/all")
    public ResponseEntity<?> getAllItem(
            @RequestHeader(TENANT_ID) Long tenantId
    ){
        log.info(path(HttpMethod.GET, "item/all"));
        return (ResponseEntity<?>) buildGetAllItemProcessTemplate(tenantId, itemService).run();
    }

    /**
     * Return a ResponseEntity object of item by id
     * @param id        ID of item (must be provided)
     * @return          ResponseEntity
     */
    @GetMapping("item/{id:\\d+}")
    public ResponseEntity<?> getItemById(
            @PathVariable(ID) Long id
    ){
        log.info(path(HttpMethod.GET, "item/".concat(String.valueOf(id))));
        return (ResponseEntity<?>) buildGetItemByIdTemplateProcess(id, itemService).run();
    }

    /**
     * Save item
     * @param tenantId  The client id (must be provided)
     * @param request   ItemRequest
     * @return          ResponseEntity
     */
    @PostMapping("item")
    public ResponseEntity<?> saveItem(
            @RequestHeader(TENANT_ID) Long tenantId,
            @RequestBody ItemRequest request
    ){
        log.info(path(HttpMethod.POST, "item"));
        return (ResponseEntity<?>) buildSaveItemProcessTemplate(tenantId, request, ingredientService, itemService).run();
    }

    /**
     * Update item entity by ID
     * @param tenantId  The client id (must be provided)
     * @param id        ID of item
     * @param request   ItemRequest
     * @return          ResponseEntity
     */
    @PutMapping(value = "item/{id:\\d+}")
    public ResponseEntity<?> updateItem(
            @RequestHeader(TENANT_ID) Long tenantId,
            @PathVariable(ID) Long id,
            @RequestBody ItemRequest request
    ){
        log.info(path(HttpMethod.PUT, "item/".concat(String.valueOf(id))));
        return (ResponseEntity<?>) buildUpdateItemProcessTemplate(tenantId, id, request, ingredientService, itemService).run();
    }

    /**
     * Delete item entity by id
     * @param tenantId  The client id (must be provided)
     * @param id        ID of item
     * @return          ResponseEntity
     */
    @DeleteMapping("item/{id:\\d+}")
    public ResponseEntity<?> deleteItem(
            @RequestHeader(TENANT_ID) Long tenantId,
            @PathVariable(ID) Long id
    ){
        log.info(path(HttpMethod.DELETE, "item/".concat(String.valueOf(id))));
        return (ResponseEntity<?>) buildDeleteIdProcessTemplate(tenantId, id, itemService).run();
    }

    /**
     * Return an ResponseEntity object. Delete multiple item entities.
     * @param tenantId  The client id (must be provided)
     * @param request   ItemDeleteAllRequest
     * @return          ResponseEntity
     */
    @DeleteMapping("item/all")
    public ResponseEntity<?> deleteAllItemsAfterConfirm(
            @RequestHeader(TENANT_ID) Long      tenantId,
            @RequestBody ItemDeleteAllRequest   request
    ) {
        log.info(path(HttpMethod.DELETE, "item/all"));
        return (ResponseEntity<?>) buildDeleteAllItemAfterConfirmTemplateProcess(tenantId, request,
                ingredientService, inventoryService, itemService).run();
    }

    // </editor-fold>

}
