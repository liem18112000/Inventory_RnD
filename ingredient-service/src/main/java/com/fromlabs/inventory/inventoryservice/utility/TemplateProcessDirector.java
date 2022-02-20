/*
 * Copyright (c) 2021. Liem Doan
 */

package com.fromlabs.inventory.inventoryservice.utility;

import com.fromlabs.inventory.inventoryservice.client.supplier.SupplierClient;
import com.fromlabs.inventory.inventoryservice.common.template.*;
import com.fromlabs.inventory.inventoryservice.ingredient.IngredientService;
import com.fromlabs.inventory.inventoryservice.ingredient.beans.request.IngredientPageRequest;
import com.fromlabs.inventory.inventoryservice.ingredient.beans.request.IngredientRequest;
import com.fromlabs.inventory.inventoryservice.ingredient.config.beans.request.IngredientConfigRequest;
import com.fromlabs.inventory.inventoryservice.ingredient.event.IngredientEventService;
import com.fromlabs.inventory.inventoryservice.ingredient.mapper.IngredientMapper;
import com.fromlabs.inventory.inventoryservice.ingredient.track.IngredientHistoryService;
import com.fromlabs.inventory.inventoryservice.ingredient.track.beans.request.IngredientHistoryPageRequest;
import com.fromlabs.inventory.inventoryservice.ingredient.track.mapper.IngredientHistoryMapper;
import com.fromlabs.inventory.inventoryservice.inventory.InventoryService;
import com.fromlabs.inventory.inventoryservice.inventory.beans.request.InventoryPageRequest;
import com.fromlabs.inventory.inventoryservice.inventory.beans.request.InventoryRequest;
import com.fromlabs.inventory.inventoryservice.inventory.mapper.InventoryMapper;
import com.fromlabs.inventory.inventoryservice.item.ItemService;
import com.fromlabs.inventory.inventoryservice.item.beans.request.BatchItemsRequest;
import com.fromlabs.inventory.inventoryservice.item.beans.request.ItemDeleteAllRequest;
import com.fromlabs.inventory.inventoryservice.item.beans.request.ItemPageRequest;
import com.fromlabs.inventory.inventoryservice.item.beans.request.ItemRequest;
import com.fromlabs.inventory.inventoryservice.item.mapper.ItemMapper;
import lombok.experimental.UtilityClass;

import javax.validation.constraints.NotNull;
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

    //<editor-fold desc="buildGetLabelValueStatus">

    // TODO: Using status service
    /**
     * Build get lis of label-value status template process
     * @return TemplateProcess
     */
    public TemplateProcess buildGetLabelValueStatusTemplateProcess() {
        return WebTemplateProcess.builder()
                .process(TransactionLogic::getLabelValueStatus)
                .build();
    }

    //</editor-fold>

    //<editor-fold desc="buildGetLabelValueEventTemplateProcess">

    /**
     * Build get event label-value list template process
     * @param clientId      Client ID
     * @param eventService  IngredientEventService
     * @return TemplateProcess
     */
    public TemplateProcess buildGetLabelValueEventTemplateProcess(
            @NotNull final Long clientId,
            @NotNull final IngredientEventService eventService
    ) {
        return WebTemplateProcess.builder()
                .process(() -> getLabelValueEvent(clientId, eventService))
                .build();
    }

    //</editor-fold>

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
            @NotNull final Long                     tenantId,
            @NotNull final IngredientPageRequest    request,
            @NotNull final IngredientService        ingredientService,
            @NotNull final InventoryService         inventoryService
    ) {
        return WebStatefulTemplateProcess.statefulWebCheckBuilder()
                .bootstrap( () -> bootstrapTenantAndPreprocessIngredientPageRequest(tenantId, request))
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
            @NotNull final Long                     tenantId,
            @NotNull final IngredientPageRequest    request,
            @NotNull final IngredientService        ingredientService,
            @NotNull final InventoryService         inventoryService
    ) {
        return WebStatefulTemplateProcess.statefulWebCheckBuilder()
                .bootstrap( () -> bootstrapTenantAndPreprocessIngredientPageRequest(tenantId, request))
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
            @NotNull final Long                 tenantId,
            @NotNull final IngredientService    ingredientService
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
            @NotNull final Long                 tenantId,
            @NotNull final IngredientService    ingredientService
    ) {
        return WebTemplateProcess.builder()
                .validate(  () -> validateTenant(tenantId))
                .process(   () -> ok(IngredientMapper.toDto(ingredientService.getAll(tenantId))))
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
            @NotNull final Long                 tenantId,
            @NotNull final Long                 parentId,
            @NotNull final IngredientService    ingredientService
    ) {
        return WebTemplateProcess.builder()
                .validate(  () -> validateTenantAndParentId(tenantId, parentId))
                .process(   () -> ok(IngredientMapper.toDto(ingredientService.getAll(tenantId, parentId))))
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
            @NotNull final Long                 tenantId,
            @NotNull final String               code,
            @NotNull final IngredientService    ingredientService,
            @NotNull final InventoryService     inventoryService
    ) {
        return WebCheckBuilder()
                .validate(  () -> validateTenant(tenantId))
                .before(    () -> Objects.nonNull(ingredientService.getByCode(code)))
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
            @NotNull final Long                 tenantId,
            @NotNull final Long                 id,
            @NotNull final IngredientService    ingredientService,
            @NotNull final InventoryService     inventoryService
    ) {
        return WebCheckBuilder()
                .validate(  () -> validateTenantAndId(tenantId, id))
                .before(    () -> Objects.nonNull(ingredientService.getById(id)))
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
     * @param historyService    IngredientHistoryService
     * @param eventService      IngredientEventService
     * @return                  TemplateProcess
     */
    public TemplateProcess buildSaveIngredientTemplateProcess(
            @NotNull final Long tenantId,
            @NotNull final IngredientRequest request,
            @NotNull AtomicBoolean transactFlag,
            @NotNull final IngredientService ingredientService,
            @NotNull final IngredientHistoryService historyService,
            @NotNull final IngredientEventService eventService
    ) {
        return WebCheckBuilder()
                .bootstrap( () -> bootstrapTenantAndPreprocessIngredientRequest(tenantId, request))
                .validate(  () -> validateIngredient(request, false))
                .before(    () -> beforeSaveIngredientCheckConstraint(request, ingredientService, historyService, eventService))
                .process(   () -> saveIngredientAndConfig(request, ingredientService, transactFlag))
                .after(     () -> afterSaveIngredientCheckConstraint(tenantId, request, ingredientService, transactFlag, historyService, eventService))
                .build();
    }

    //</editor-fold>

    //<editor-fold desc="buildDeleteIngredientByIdTemplateProcess">

    /**
     * Build delete ingredient by id template process
     * @param tenantId          Tenant ID
     * @param id                Ingredient ID
     * @param ingredientService IngredientService
     * @return                  TemplateProcess
     */
    public TemplateProcess buildDeleteIngredientByIdTemplateProcess(
            @NotNull final Long                 tenantId,
            @NotNull final Long                 id,
            @NotNull final IngredientService    ingredientService
    ) {
        return WebCheckBuilder()
                .validate(  () -> validateTenantAndId(tenantId, id))
                .before(    () -> isIngredientExistById(id, ingredientService))
                .process(   () -> deleteIngredientEntity(id, ingredientService))
                .after(     () -> !isIngredientExistById(id, ingredientService))
                .build();
    }

    //</editor-fold>

    //<editor-fold desc="buildGetHistoryByIngredientTemplateProcess">

    /**
     * Build get all ingredient history by ingredient id template process
     * @param tenantId          Tenant ID
     * @param ingredientId      Unique Ingredient ID
     * @param ingredientService IngredientService
     * @param historyService    IngredientHistoryService
     * @return                  TemplateProcess
     */
    public TemplateProcess buildGetHistoryByIngredientTemplateProcess(
            @NotNull final Long                     tenantId,
            @NotNull final Long                     ingredientId,
            @NotNull final IngredientService        ingredientService,
            @NotNull final IngredientHistoryService historyService,
            @NotNull final IngredientEventService   eventService
    ) {
        return WebTemplateProcess.builder()
                .validate(  () -> validateTenant(tenantId))
                .process(   () -> getAllHistory(tenantId, ingredientId, ingredientService, historyService, eventService))
                .build();
    }

    //</editor-fold>

    //<editor-fold desc="buildUpdateIngredientTemplateProcess">

    /**
     * Build update ingredient template process
     * @param tenantId          Tenant ID
     * @param request           IngredientRequest
     * @param ingredientService IngredientService
     * @param historyService    IngredientHistoryService
     * @param eventService      IngredientEventService
     * @return                  WebTemplateProcess
     */
    public WebTemplateProcess buildUpdateIngredientTemplateProcess(
            @NotNull final Long tenantId,
            @NotNull final IngredientRequest request,
            @NotNull final IngredientService ingredientService,
            @NotNull final IngredientHistoryService historyService,
            @NotNull final IngredientEventService eventService
    ) {
        return WebCheckBuilder()
                .bootstrap( () -> bootstrapTenantAndPreprocessIngredientRequest(tenantId, request))
                .validate(  () -> validateIngredient(request, true))
                .before(    () -> checkConstraintBeforeUpdateIngredient(request, ingredientService, historyService, eventService))
                .process(   () -> ok(ingredientService.save(ingredientService.getById(request.getId()).update(request))))
                .after(     () -> checkConstraintAfterUpdateIngredient(request, ingredientService, historyService, eventService))
                .build();
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
            @NotNull final Long                 tenantId,
            @NotNull final InventoryRequest request,
            @NotNull final Long                 id,
            @NotNull final InventoryService     inventoryService
    ) {
        return WebCheckBuilder()
                .bootstrap( () -> bootstrapTenantAndIdAndPreprocessInventoryRequest(tenantId, id, request))
                .validate(  () -> validateInventoryRequest(request))
                .before(    () -> updateInventoryCheckConstraint(request, inventoryService))
                .process(   () -> ok(InventoryMapper.toDto(inventoryService.save(inventoryService.getById(id).update(request)))))
                .build();
    }

    //</editor-fold>

    //<editor-fold desc="buildGetHistoryByIdTemplateProcess">

    public TemplateProcess buildGetHistoryByIdTemplateProcess(
            @NotNull final Long                     id,
            @NotNull final IngredientHistoryService historyService,
            @NotNull final IngredientEventService   eventService
    ) {
        return WebTemplateProcess.builder()
                .validate(() -> validateId(id))
                .process(() -> ok(IngredientHistoryMapper.toDto(historyService.getByIdWithException(id), eventService)))
                .build();
    }

    //</editor-fold>

    //<editor-fold desc="buildGetPageHistoryTemplateProcess">

    /**
     * Build get page ingredient history template process
     * @param tenantId          Tenant ID
     * @param request           IngredientHistoryPageRequest
     * @param ingredientService IngredientService
     * @param historyService    IngredientHistoryService
     * @return                  TemplateProcess
     */
    public TemplateProcess buildGetPageHistoryTemplateProcess(
            @NotNull final Long                         tenantId,
            @NotNull final IngredientHistoryPageRequest request,
            @NotNull final IngredientService            ingredientService,
            @NotNull final IngredientHistoryService     historyService,
            @NotNull final IngredientEventService       eventService
    ) {
        return WebTemplateProcess.builder()
                .bootstrap( () -> bootstrapTenantPreprocessIngredientHistoryPageRequest(tenantId, request))
                .validate(  () -> validateTenantAndIngredientId(tenantId, request.getIngredientId()))
                .process(   () -> getPageIngredientHistory(request, ingredientService, historyService, eventService))
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
            @NotNull final Long                 tenantId,
            @NotNull final Long                 id,
            @NotNull final InventoryService     inventoryService
    ) {
        return WebCheckBuilder()
                .validate(  () -> validateTenantAndId(tenantId, id))
                .before(    () -> Objects.nonNull(inventoryService.getById(id)))
                .process(   () -> ok(inventoryService.getById(id)))
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
            @NotNull final Long                 tenantId,
            @NotNull final Long                 ingredientId,
            @NotNull final IngredientService    ingredientService,
            @NotNull final InventoryService     inventoryService,
            @NotNull final ItemService          itemService
    ) {
        return WebCheckBuilder()
                .validate(  () -> validateTenantAndIngredientId(tenantId, ingredientId))
                .before(    () -> checkConstraintBeforeSyncInventoryByIngredient(ingredientId, ingredientService))
                .process(   () -> ok(syncIngredientInInventory(tenantId, ingredientService.getById(ingredientId), inventoryService, itemService)))
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
            @NotNull final Long                tenantId,
            @NotNull final IngredientService   ingredientService,
            @NotNull final InventoryService    inventoryService,
            @NotNull final ItemService         itemService
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
            @NotNull final Long                tenantId,
            @NotNull final InventoryService    inventoryService
    ) {
        return WebTemplateProcess.builder()
                .validate(  () -> validateTenant(tenantId))
                .process(   () -> ok(InventoryMapper.toDto(inventoryService.getAll(tenantId))))
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
            @NotNull final Long                     tenantId,
            @NotNull final InventoryPageRequest request,
            @NotNull final IngredientService        ingredientService,
            @NotNull final InventoryService         inventoryService
    ) {
        return WebTemplateProcess.builder()
                .bootstrap( () -> bootstrapTenantAndPreprocessInventoryPageRequest(tenantId, request))
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
            @NotNull final Long                 tenantId,
            @NotNull final ItemPageRequest request,
            @NotNull final IngredientService    ingredientService,
            @NotNull final ItemService          itemService,
            @NotNull final SupplierClient supplierClient
    ) {
        return WebTemplateProcess.builder()
                .bootstrap( () -> bootstrapTenantAndPreprocessItemPageRequest(tenantId, request))
                .validate(  () -> validateTenant(request.getClientId()))
                .process(   () -> ok(getItemPageWithFiler(request, ingredientService, itemService, supplierClient)))
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
            @NotNull final Long        tenantId,
            @NotNull final ItemService itemService,
            @NotNull final SupplierClient supplierClient
    ) {
        return WebTemplateProcess.builder()
                .validate(  () -> validateTenant(tenantId))
                .process(   () -> ok(ItemMapper.toDto(itemService.getAll(tenantId), supplierClient)))
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
            @NotNull final Long        id,
            @NotNull final ItemService itemService,
            @NotNull final SupplierClient supplierClient
    ) {
        return WebCheckBuilder()
                .validate(  () -> validateId(id))
                .before(    () -> isItemExistById(id, itemService))
                .process(   () -> ok(ItemMapper.toDto(itemService.getById(id), supplierClient)))
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
     * @param historyService    IngredientHistoryService
     * @param eventService      IngredientEventService
     * @return                  WebTemplateProcess
     */
    public WebTemplateProcess buildSaveItemProcessTemplate(
            @NotNull final Long tenantId,
            @NotNull final ItemRequest request,
            @NotNull final IngredientService ingredientService,
            @NotNull final ItemService itemService,
            @NotNull final IngredientHistoryService historyService,
            @NotNull final IngredientEventService eventService,
            @NotNull final SupplierClient supplierClient
    ) {
        return WebCheckBuilder()
                .bootstrap( () -> bootstrapTenantAndPreprocessItemRequest(tenantId, request))
                .validate(  () -> validateSaveItem(request))
                .before(    () -> checkConstraintBeforeSaveItem(request, ingredientService, itemService, historyService, eventService))
                .process(   () -> saveOrUpdateItem(request, ingredientService, itemService, supplierClient))
                .after(     () -> checkConstraintAfterSaveItem(request, itemService, ingredientService, historyService, eventService))
                .build();
    }

    //</editor-fold>

    //<editor-fold desc="buildDeleteIdProcessTemplate">

    /**
     * Build delete item template process
     * @param tenantId          Tenant ID
     * @param id                Item ID
     * @param ingredientService IngredientService
     * @param itemService       ItemService
     * @param historyService    IngredientHistoryService
     * @param eventService      IngredientEventService
     * @return                  WebTemplateProcess
     */
    public WebTemplateProcess buildDeleteIdProcessTemplate(
            @NotNull final Long tenantId,
            @NotNull final Long id,
            @NotNull final IngredientService ingredientService,
            @NotNull final ItemService itemService,
            @NotNull final IngredientHistoryService historyService,
            @NotNull final IngredientEventService eventService
    ) {
        return WebCheckBuilder()
                .validate(  () -> validateTenantAndId(tenantId, id))
                .before(    () -> checkConstraintBeforeDeleteItem(id, itemService, ingredientService, historyService, eventService))
                .process(   () -> deleteItemEntity(id, itemService))
                .after(     () -> checkConstraintAfterDeleteItem(id, itemService, ingredientService, historyService))
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
            @NotNull final Long                     tenantId,
            @NotNull final ItemDeleteAllRequest request,
            @NotNull final IngredientService        ingredientService,
            @NotNull final InventoryService         inventoryService,
            @NotNull final ItemService              itemService,
            @NotNull final SupplierClient           supplierClient
    ) {
        return WebCheckBuilder()
                .bootstrap( () -> bootstrapTenantItemDeleteAllRequest(tenantId, request, ingredientService, inventoryService, itemService))
                .validate(  () -> validateDeleteItems(request))
                .before(    () -> beforeDeleteAllItemsCheckConstraint(request, ingredientService, inventoryService, itemService))
                .process(   () -> deleteAllItems(request, ingredientService, itemService, supplierClient))
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
     * @param historyService    IngredientHistoryService
     * @param eventService      IngredientEventService
     * @return                  WebTemplateProcess
     */
    public WebTemplateProcess buildUpdateItemProcessTemplate(
            @NotNull final Long tenantId,
            @NotNull final Long id,
            @NotNull final ItemRequest request,
            @NotNull final IngredientService ingredientService,
            @NotNull final ItemService itemService,
            @NotNull final IngredientHistoryService historyService,
            @NotNull final IngredientEventService eventService,
            @NotNull final SupplierClient supplierClient
    ) {
        return WebTemplateProcessWithCheckBeforeAfter.WebCheckBuilder()
                .bootstrap( () -> bootstrapTenantAndIdtAndPreprocessItemRequest(tenantId, id, request))
                .validate(  () -> validateUpdateItem(request))
                .before(    () -> checkConstraintBeforeUpdateItem(request, itemService, ingredientService, historyService, eventService))
                .process(   () -> saveOrUpdateItem(request, ingredientService, itemService, supplierClient))
                .after(     () -> checkConstraintAfterUpdateItem(request, itemService, ingredientService, historyService, eventService))
                .build();
    }

    //</editor-fold>

    //<editor-fold desc="buildSaveItemsProcessTemplate">

    /**
     * Build save items process template
     * @param tenantId          Tenant ID
     * @param request           BatchItemsRequest
     * @param ingredientService IngredientService
     * @param itemService       ItemService
     * @param historyService    IngredientHistoryService
     * @param eventService      IngredientEventService
     * @return                  TemplateProcess
     */
    public TemplateProcess buildSaveItemsProcessTemplate(
            @NotNull final Long tenantId,
            @NotNull final BatchItemsRequest request,
            @NotNull final IngredientService ingredientService,
            @NotNull final ItemService itemService,
            @NotNull final IngredientHistoryService historyService,
            @NotNull final IngredientEventService eventService,
            @NotNull final SupplierClient supplierClient
    ) {
        return WebTemplateProcessWithCheckBeforeAfter.WebCheckBuilder()
                .bootstrap( () -> bootstrapTenantAndPreprocessBatchItemRequest(tenantId, request))
                .validate(  () -> validateSaveItems(request))
                .before(    () -> checkConstraintBeforeSaveItems(request, ingredientService, itemService, historyService, eventService))
                .process(   () -> saveItems(request, ingredientService, itemService, supplierClient))
                .after(     () -> checkConstraintAfterSaveItems(request, ingredientService, itemService, historyService, eventService))
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
            @NotNull final Long                     tenantId,
            @NotNull final Long                     id,
            @NotNull final IngredientConfigRequest  request,
            @NotNull final IngredientService        ingredientService
    ) {
        return WebTemplateProcessWithCheckBeforeAfter.WebCheckBuilder()
                .bootstrap( () -> bootstrapTenantAndIdIngredientConfigRequest(tenantId, id, request))
                .validate(  () -> validateTenantAndId(request.getClientId(), request.getId()))
                .before(    () -> Objects.nonNull(ingredientService.getConfig(request.getId())))
                .process(   () -> ok(ingredientService.saveConfig(ingredientService.getConfig(request.getId()).update(request))))
                .after(     () -> Objects.nonNull(ingredientService.getConfig(request.getId())))
                .build();
    }

    //</editor-fold>

}
