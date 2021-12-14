/*
 * Copyright (c) 2021. Liem Doan
 */

package com.fromlabs.inventory.inventoryservice.utility;

import com.fromlabs.inventory.inventoryservice.common.template.*;
import com.fromlabs.inventory.inventoryservice.ingredient.IngredientService;
import com.fromlabs.inventory.inventoryservice.ingredient.beans.*;
import com.fromlabs.inventory.inventoryservice.ingredient.config.beans.request.IngredientConfigRequest;
import com.fromlabs.inventory.inventoryservice.inventory.InventoryService;
import com.fromlabs.inventory.inventoryservice.inventory.beans.*;
import com.fromlabs.inventory.inventoryservice.item.ItemService;
import com.fromlabs.inventory.inventoryservice.item.beans.*;
import lombok.experimental.UtilityClass;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.fromlabs.inventory.inventoryservice.common.template.WebTemplateProcessWithCheckBeforeAfter.*;
import static com.fromlabs.inventory.inventoryservice.utility.ControllerValidation.*;
import static com.fromlabs.inventory.inventoryservice.utility.RequestBootstrap.*;
import static com.fromlabs.inventory.inventoryservice.utility.TransactionConstraint.*;
import static com.fromlabs.inventory.inventoryservice.utility.TransactionLogic.*;
import static org.springframework.http.ResponseEntity.*;

/**
 * <h1>Template process builder director</h1>
 */
@UtilityClass
public class TemplateProcessDirector {

    //<editor-fold desc="buildGetPageIngredientTypeTemplateProcess">

    public final String PROCESS_KEY_GET_PAGE_INGREDIENT_TYPE = "ProcessKey_GetIngredientTypePage";

    /**
     * Build get page ingredient type template process
     * @param tenantId          Tenant ID
     * @param request           IngredientPageRequest
     * @param ingredientService IngredientService
     * @param inventoryService  InventoryService
     * @return                  TemplateProcess
     */
    public TemplateProcess buildGetPageIngredientTypeTemplateProcess(
            Long                    tenantId,
            IngredientPageRequest   request,
            IngredientService       ingredientService,
            InventoryService        inventoryService
    ) {
        return WebStatefulTemplateProcess.statefulWebCheckBuilder()
                .bootstrap( () -> setTenantBoostrap(tenantId, request))
                .validate(  () -> validateTenantAndParentId(request.getClientId(), request.getParentId()))
                .process(   () -> ok(getIngredientPageWithFilter(request, ingredientService, inventoryService)))
                .build();
    }

    //</editor-fold>

    //<editor-fold desc="buildGetPageIngredientCategoryTemplateProcess">

    public final String PROCESS_KEY_GET_PAGE_INGREDIENT_CATE = "ProcessKey_GetIngredientCategoryPage";

    /**
     * Build get page ingredient category template process
     * @param tenantId          Tenant ID
     * @param request           IngredientPageRequest
     * @param ingredientService IngredientService
     * @param inventoryService  InventoryService
     * @return                  WebTemplateProcess
     */
    public WebTemplateProcess buildGetPageIngredientCategoryTemplateProcess(
            Long                    tenantId,
            IngredientPageRequest   request,
            IngredientService       ingredientService,
            InventoryService        inventoryService
    ) {
        return WebStatefulTemplateProcess.statefulWebCheckBuilder()
                .bootstrap( () -> setTenantBoostrap(tenantId, request))
                .validate(  () -> validateTenant(request.getClientId()))
                .process(   () -> ok(getIngredientPageWithFilter(request, ingredientService, inventoryService)))
                .build();
    }

    //</editor-fold>

    //<editor-fold desc="buildGetLabelValueIngredientTypeTemplateProcess">

    public final String PROCESS_KEY_GET_LABEL_VALUE_INGREDIENT_TYPE = "ProcessKey_GetLabelValueIngredientType";

    /**
     * Build get label-value Ingredient type template process
     * @param tenantId          Tenant ID
     * @param ingredientService IngredientService
     * @return                  WebTemplateProcess
     */
    public WebTemplateProcess buildGetLabelValueIngredientTypeTemplateProcess(
            Long                tenantId,
            IngredientService   ingredientService
    ) {
        return WebStatefulTemplateProcess.statefulWebCheckBuilder()
                .bootstrap( () -> tenantId)
                .validate(  () -> validateTenant(tenantId))
                .process(   () -> ok(getSimpleIngredientTypeActiveDto(tenantId, ingredientService)))
                .build();
    }

    //</editor-fold>

    //<editor-fold desc="buildGetAllIngredientCategoryTemplateProcess">

    /**
     * Build get all ingredient category template process
     * @param tenantId          Tenant ID
     * @param ingredientService IngredientService
     * @return                  WebTemplateProcess
     */
    public WebTemplateProcess buildGetAllIngredientCategoryTemplateProcess(
            Long                tenantId,
            IngredientService   ingredientService
    ) {
        return WebTemplateProcess.builder()
                .validate(  () -> validateTenant(tenantId))
                .process(   () -> ok(IngredientDto.from(ingredientService.getAll(tenantId))))
                .build();
    }

    //</editor-fold>

    //<editor-fold desc="buildGetAllIngredientTypeTemplateProcess">

    /**
     * Build get all ingredient type template process
     * @param tenantId          Tenant ID
     * @param parentId          Parent ID
     * @param ingredientService IngredientService
     * @return                  WebTemplateProcess
     */
    public WebTemplateProcess buildGetAllIngredientTypeTemplateProcess(
            Long                tenantId,
            Long                parentId,
            IngredientService   ingredientService
    ) {
        return WebTemplateProcess.builder()
                .validate(  () -> validateTenantAndParentId(tenantId, parentId))
                .process(   () -> ok(IngredientDto.from(ingredientService.getAll(tenantId, parentId))))
                .build();
    }

    //</editor-fold>

    //<editor-fold desc="buildGetIngredientByCodeTemplateProcess">

    /**
     * Build get ingredient by code template process
     * @param tenantId          Tenant ID
     * @param code              Ingredient code
     * @param ingredientService IngredientService
     * @param inventoryService  InventoryService
     * @return                  WebTemplateProcess
     */
    public WebTemplateProcess buildGetIngredientByCodeTemplateProcess(
            Long                tenantId,
            String              code,
            IngredientService   ingredientService,
            InventoryService    inventoryService
    ) {
        return WebCheckBuilder()
                .validate(  () -> validateTenant(tenantId))
                .before(    () -> Objects.nonNull(ingredientService.get(code)))
                .process(   () -> ok(getIngredientByCodeWithQuantity(code, ingredientService, inventoryService)))
                .after(     () -> Boolean.TRUE).build();
    }

    //</editor-fold>

    //<editor-fold desc="buildGetIngredientByIdTemplateProcess">

    /**
     * Build get ingredient by id template process
     * @param tenantId          Tenant ID
     * @param id                Ingredient Unique ID
     * @param ingredientService IngredientService
     * @param inventoryService  InventoryService
     * @return                  WebTemplateProcess
     */
    public WebTemplateProcess buildGetIngredientByIdTemplateProcess(
            Long                tenantId,
            Long                id,
            IngredientService   ingredientService,
            InventoryService    inventoryService
    ) {
        return WebCheckBuilder()
                .validate(  () -> validateTenantAndId(tenantId, id))
                .before(    () -> Objects.nonNull(ingredientService.get(id)))
                .process(   () -> ok(getIngredientByIdWithQuantity(id, ingredientService, inventoryService)))
                .build();
    }

    //</editor-fold>

    //<editor-fold desc="buildSaveIngredientTemplateProcess">

    /**
     * Build save ingredient template process
     * @param tenantId          Tenant ID
     * @param request           IngredientRequest
     * @param transactFlag      AtomicBoolean
     * @param ingredientService IngredientService
     * @return                  WebTemplateProcess
     */
    public WebTemplateProcess buildSaveIngredientTemplateProcess(
            Long                tenantId,
            IngredientRequest request,
            AtomicBoolean transactFlag,
            IngredientService   ingredientService
    ) {
        return WebCheckBuilder()
                .bootstrap( () -> setTenantBoostrap(tenantId, request))
                .validate(  () -> validateIngredient(request, false))
                .before(    () -> beforeSaveIngredientCheckConstraint(request, ingredientService))
                .process(   () -> saveIngredientAndConfig(request, ingredientService, transactFlag))
                .after(     () -> afterSaveIngredientCheckConstraint(tenantId, request, ingredientService, transactFlag))
                .build();
    }

    //</editor-fold>

    //<editor-fold desc="buildDeleteIngredientByIdTemplateProcess">

    /**
     * Build delete ingredient by id template process
     * @param tenantId          Tenant ID
     * @param id                Ingredient ID
     * @param ingredientService IngredientService
     * @return                  WebTemplateProcess
     */
    public WebTemplateProcess buildDeleteIngredientByIdTemplateProcess(
            Long                tenantId,
            Long                id,
            IngredientService   ingredientService
    ) {
        return WebCheckBuilder()
                .validate(  () -> validateTenantAndId(tenantId, id))
                .before(    () -> isIngredientExistById(id, ingredientService))
                .process(   () -> deleteIngredientEntity(id, ingredientService))
                .after(     () -> !isIngredientExistById(id, ingredientService))
                .build();
    }

    //</editor-fold>

    //<editor-fold desc="buildUpdateIngredientTemplateProcess">

    /**
     * Build update ingredient template process
     * @param tenantId          Tenant ID
     * @param request           IngredientRequest
     * @param ingredientService IngredientService
     * @return                  WebTemplateProcess
     */
    public WebTemplateProcess buildUpdateIngredientTemplateProcess(
            Long                tenantId,
            IngredientRequest   request,
            IngredientService   ingredientService
    ) {
        return WebCheckBuilder()
                .bootstrap( () -> setTenantBoostrap(tenantId, request))
                .validate(  () -> validateIngredient(request, true))
                .before(    () -> updateIngredientCheckConstraint(request, ingredientService))
                .process(   () -> ok(ingredientService.save(ingredientService.get(request.getId()).update(request))))
                .after(     () -> Objects.nonNull(ingredientService.get(request.getCode()))).build();
    }

    //</editor-fold>

    //<editor-fold desc="buildUpdateInventoryByIdTemplateProcess">

    /**
     * Build update inventory template process
     * @param tenantId          Tenant ID
     * @param request           InventoryRequest
     * @param id                Inventory ID
     * @param inventoryService  Inventory ID
     * @return                  WebTemplateProcess
     */
    public WebTemplateProcess buildUpdateInventoryByIdTemplateProcess(
            Long                tenantId,
            InventoryRequest    request,
            Long                id,
            InventoryService    inventoryService
    ) {
        return WebCheckBuilder()
                .bootstrap( () -> setIdAndTenantBootstrap(tenantId, id, request))
                .validate(  () -> validateInventoryRequest(request))
                .before(    () -> updateInventoryCheckConstraint(request, inventoryService))
                .process(   () -> ok(InventoryDto.from(inventoryService.save(inventoryService.get(id).update(request)))))
                .build();
    }

    //</editor-fold>

    //<editor-fold desc="buildGetInventoryByIdTemplateProcess">

    /**
     * Build get inventory by id template process
     * @param tenantId          Tenant ID
     * @param id                Inventory ID
     * @param inventoryService  InventoryService
     * @return                  WebTemplateProcess
     */
    public WebTemplateProcess buildGetInventoryByIdTemplateProcess(
            Long                tenantId,
            Long                id,
            InventoryService    inventoryService
    ) {
        return WebCheckBuilder()
                .validate(  () -> validateTenantAndId(tenantId, id))
                .before(    () -> Objects.nonNull(inventoryService.get(id)))
                .process(   () -> ok(inventoryService.get(id)))
                .after(     () -> Boolean.TRUE)
                .build();
    }

    //</editor-fold>

    //<editor-fold desc="buildSyncInventoryByIngredientTemplateProcess">

    /**
     * Build sync inventory by ingredient template process
     * @param tenantId          Tenant ID
     * @param ingredientId      Ingredient ID
     * @param ingredientService IngredientService
     * @param inventoryService  InventoryService
     * @param itemService       ItemService
     * @return                  WebTemplateProcess
     */
    public WebTemplateProcess buildSyncInventoryByIngredientTemplateProcess(
            Long                tenantId,
            Long                ingredientId,
            IngredientService   ingredientService,
            InventoryService    inventoryService,
            ItemService itemService
    ) {
        return WebCheckBuilder()
                .validate(  () -> validateTenantAndIngredientId(tenantId, ingredientId))
                .before(    () -> checkConstraintBeforeSyncInventoryByIngredient(ingredientId, ingredientService))
                .process(   () -> ok(syncIngredientInInventory(tenantId, ingredientService.get(ingredientId), inventoryService, itemService)))
                .build();
    }

    //</editor-fold>

    //<editor-fold desc="buildSyncInventoryTemplateProcess">

    /**
     * Build sync inventory template process
     * @param tenantId          Tenant ID
     * @param ingredientService IngredientService
     * @param inventoryService  InventoryService
     * @param itemService       ItemService
     * @return                  WebTemplateProcess
     */
    public WebTemplateProcess buildSyncInventoryTemplateProcess(
            Long                tenantId,
            IngredientService   ingredientService,
            InventoryService    inventoryService,
            ItemService         itemService
    ) {
        return WebTemplateProcess.builder()
                .validate(  () -> validateTenant(tenantId))
                .process(   () -> ok(syncAllIngredientInInventory(tenantId, ingredientService, inventoryService, itemService)))
                .build();
    }

    //</editor-fold>

    //<editor-fold desc="buildGetAllInventoryTemplateProcess">

    /**
     * Build get all inventory template process
     * @param tenantId          Tenant ID
     * @param inventoryService  InventoryService
     * @return                  WebTemplateProcess
     */
    public WebTemplateProcess buildGetAllInventoryTemplateProcess(
            Long                tenantId,
            InventoryService    inventoryService
    ) {
        return WebTemplateProcess.builder()
                .validate(  () -> validateTenant(tenantId))
                .process(   () -> ok(InventoryDto.from(inventoryService.getAll(tenantId))))
                .build();
    }

    //</editor-fold>

    //<editor-fold desc="buildGetPageInventoryTemplateProcess">

    /**
     * Build get page inventory template process
     * @param tenantId          Tenant ID
     * @param request           InventoryPageRequest
     * @param ingredientService IngredientService
     * @param inventoryService  InventoryService
     * @return                  WebTemplateProcess
     */
    public WebTemplateProcess buildGetPageInventoryTemplateProcess(
            Long                    tenantId,
            InventoryPageRequest request,
            IngredientService       ingredientService,
            InventoryService        inventoryService
    ) {
        return WebTemplateProcess.builder()
                .bootstrap( () -> setTenantBoostrap(tenantId, request))
                .validate(  () -> validateTenant(request.getClientId()))
                .process(   () -> ok(getInventoryPageWithFilter(request, ingredientService, inventoryService)))
                .build();
    }

    //</editor-fold>

    //<editor-fold desc="buildGetPageItemTemplateProcess">

    /**
     * Build get page item template process
     * @param tenantId          Tenant ID
     * @param request           ItemPageRequest
     * @param ingredientService IngredientService
     * @param itemService       ItemService
     * @return                  WebTemplateProcess
     */
    public WebTemplateProcess buildGetPageItemTemplateProcess(
            Long                tenantId,
            ItemPageRequest request,
            IngredientService   ingredientService,
            ItemService         itemService
    ) {
        return WebTemplateProcess.builder()
                .bootstrap( () -> setTenantBoostrap(tenantId, request))
                .validate(  () -> validateTenant(request.getClientId()))
                .process(   () -> ok(getItemPageWithFiler(request, ingredientService, itemService)))
                .build();
    }

    //</editor-fold>

    //<editor-fold desc="buildGetAllItemProcessTemplate">

    /**
     * Build get all item process template
     * @param tenantId      Tenant ID
     * @param itemService   ItemService
     * @return              WebTemplateProcess
     */
    public WebTemplateProcess buildGetAllItemProcessTemplate(
            Long        tenantId,
            ItemService itemService
    ) {
        return WebTemplateProcess.builder()
                .validate(  () -> validateTenant(tenantId))
                .process(   () -> ok(ItemDto.from(itemService.getAll(tenantId))))
                .build();
    }

    //</editor-fold>

    //<editor-fold desc="buildGetItemByIdTemplateProcess">

    /**
     * Build get item by id template process
     * @param id            Item ID
     * @param itemService   ItemService
     * @return              WebTemplateProcess
     */
    public WebTemplateProcess buildGetItemByIdTemplateProcess(
            Long        id,
            ItemService itemService
    ) {
        return WebCheckBuilder()
                .validate(  () -> validateId(id))
                .before(    () -> isItemExistById(id, itemService))
                .process(   () -> ok(ItemDto.from(itemService.getById(id))))
                .build();
    }

    //</editor-fold>

    //<editor-fold desc="buildSaveItemProcessTemplate">

    /**
     * Build save item process template
     * @param tenantId          Tenant ID
     * @param request           ItemRequest
     * @param ingredientService IngredientService
     * @param itemService       ItemService
     * @return                  WebTemplateProcess
     */
    public WebTemplateProcess buildSaveItemProcessTemplate(
            Long                tenantId,
            ItemRequest request,
            IngredientService   ingredientService,
            ItemService         itemService
    ) {
        return WebCheckBuilder()
                .bootstrap( () -> setTenantBoostrap(tenantId, request))
                .validate(  () -> validateSaveItem(request))
                .before(    () -> saveItemCheckConstraint(request, ingredientService, itemService))
                .process(   () -> saveOrUpdateItem(request, ingredientService, itemService))
                .build();
    }

    //</editor-fold>

    //<editor-fold desc="buildDeleteIdProcessTemplate">

    /**
     * Build delete item template process
     * @param tenantId      Tenant ID
     * @param id            Item ID
     * @param itemService   ItemService
     * @return              WebTemplateProcess
     */
    public WebTemplateProcess buildDeleteIdProcessTemplate(
            Long tenantId,
            Long id,
            ItemService itemService
    ) {
        return WebCheckBuilder()
                .validate(  () -> validateTenantAndId(tenantId, id))
                .before(    () -> isItemExistById(id, itemService))
                .process(   () -> deleteItemEntity(id, itemService))
                .after(     () -> !isItemExistById(id, itemService))
                .build();
    }

    //</editor-fold>

    //<editor-fold desc="buildDeleteAllItemAfterConfirmTemplateProcess">

    /**
     * Build delete all item after confirm template process
     * @param tenantId          Tenant ID
     * @param request           ItemDeleteAllRequest
     * @param ingredientService IngredientService
     * @param inventoryService  InventoryService
     * @param itemService       ItemService
     * @return                  WebTemplateProcess
     */
    public WebTemplateProcess buildDeleteAllItemAfterConfirmTemplateProcess(
            Long                    tenantId,
            ItemDeleteAllRequest    request,
            IngredientService       ingredientService,
            InventoryService        inventoryService,
            ItemService             itemService
    ) {
        return WebCheckBuilder()
                .bootstrap( () -> deleteItemsBootstrap(tenantId, request, ingredientService, inventoryService, itemService))
                .validate(  () -> validateDeleteItems(request))
                .before(    () -> beforeDeleteAllItemsCheckConstraint(request, ingredientService, inventoryService, itemService))
                .process(   () -> deleteAllItems(request, ingredientService, itemService))
                .build();
    }

    //</editor-fold>

    //<editor-fold desc="buildUpdateItemProcessTemplate">

    /**
     * Build update item process template
     * @param tenantId          Tenant ID
     * @param id                Item ID
     * @param request           ItemRequest
     * @param ingredientService IngredientService
     * @param itemService       ItemService
     * @return                  WebTemplateProcess
     */
    public WebTemplateProcess buildUpdateItemProcessTemplate(
            Long                tenantId,
            Long                id,
            ItemRequest         request,
            IngredientService   ingredientService,
            ItemService         itemService
    ) {
        return WebTemplateProcessWithCheckBeforeAfter.WebCheckBuilder()
                .bootstrap( () -> setIdAndTenantBoostrap(tenantId, id, request))
                .validate(  () -> validateUpdateItem(request))
                .before(    () -> updateItemCheckConstraint(request, itemService))
                .process(   () -> saveOrUpdateItem(request, ingredientService, itemService))
                .build();
    }

    //</editor-fold>

    //<editor-fold desc="buildUpdateConfigTemplateProcess">

    /**
     * Build Update config template process
     * @param tenantId          Tenant ID
     * @param id                Config ID
     * @param request           IngredientConfigRequest
     * @param ingredientService IngredientService
     * @return                  WebTemplateProcess
     */
    public WebTemplateProcess buildUpdateConfigTemplateProcess(
            Long                    tenantId,
            Long                    id,
            IngredientConfigRequest request,
            IngredientService       ingredientService
    ) {
        return WebTemplateProcessWithCheckBeforeAfter.WebCheckBuilder()
                .bootstrap( () -> setIdAndTenantBoostrap(tenantId, id, request))
                .validate(  () -> validateTenantAndId(request.getClientId(), request.getId()))
                .before(    () -> Objects.nonNull(ingredientService.getConfig(request.getId())))
                .process(   () -> ok(ingredientService.saveConfig(ingredientService.getConfig(request.getId()).update(request))))
                .after(     () -> Objects.nonNull(ingredientService.getConfig(request.getId())))
                .build();
    }

    //</editor-fold>

}
