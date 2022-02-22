/*
 * Copyright (c) 2021. Liem Doan
 */

package com.fromlabs.inventory.inventoryservice.utility;

import com.fromlabs.inventory.inventoryservice.client.supplier.SupplierClient;
import com.fromlabs.inventory.inventoryservice.common.exception.ConstraintViolateException;
import com.fromlabs.inventory.inventoryservice.common.wrapper.ConstraintWrapper;
import com.fromlabs.inventory.inventoryservice.ingredient.*;
import com.fromlabs.inventory.inventoryservice.ingredient.beans.request.IngredientRequest;
import com.fromlabs.inventory.inventoryservice.ingredient.event.IngredientEventService;
import com.fromlabs.inventory.inventoryservice.ingredient.track.IngredientHistoryService;
import com.fromlabs.inventory.inventoryservice.inventory.InventoryService;
import com.fromlabs.inventory.inventoryservice.inventory.beans.request.InventoryRequest;
import com.fromlabs.inventory.inventoryservice.item.ItemService;
import com.fromlabs.inventory.inventoryservice.item.beans.request.BatchItemsRequest;
import com.fromlabs.inventory.inventoryservice.item.beans.request.ItemDeleteAllRequest;
import com.fromlabs.inventory.inventoryservice.item.beans.request.ItemRequest;
import com.fromlabs.inventory.inventoryservice.item.mapper.ItemMapper;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.fromlabs.inventory.inventoryservice.ingredient.specification.IngredientSpecification.*;
import static com.fromlabs.inventory.inventoryservice.ingredient.event.beans.enums.IngredientEvent.*;
import static com.fromlabs.inventory.inventoryservice.ingredient.event.status.IngredientEventStatus.*;
import static com.fromlabs.inventory.inventoryservice.ingredient.beans.unit.IngredientUnit.GENERIC;
import static java.util.Objects.*;
import static org.springframework.data.jpa.domain.Specification.*;


/**
 * <h1>Transaction Constraints layer</h1>
 *
 * <h2>Brief Information</h2>
 *
 * <p>
 *     This the third layer in the segment of non-infrastructure service layer.
 *     In detail, it play as the constraint guard for the pre-condition
 *     and post-condition
 * </p>
 *
 * <p>
 *     It is a mandatory layer as it must be implemented to keep the
 *     consistence of the domain service layer before and after executing transactions
 * </p>
 *
 * <h2>Usages</h2>
 *
 * <p>
 *     The infrastructure service such as the IngredientService, InventoryService or ItemService
 *     will be injected to perform checking the constraints
 * </p>
 *
 * <p>Despite that it is not easy to implement such constrain checking, it is straight-forward to
 * implement them with the regular style of programming</p>
 */

@UtilityClass
@Slf4j
public class TransactionConstraint {

    //<editor-fold desc="beforeSaveIngredientCheckConstraint">

    /**
     * Check ingredient is not exist before save . And,
     * if it has parents, this parents must exist
     * @param request           IngredientRequest
     * @param ingredientService IngredientService
     * @param historyService    IngredientHistoryService
     * @param eventService      IngredientEventService
     * @return boolean
     */
    public boolean beforeSaveIngredientCheckConstraint(
            @NotNull final IngredientRequest request,
            @NotNull final IngredientService ingredientService,
            @NotNull final IngredientHistoryService historyService,
            @NotNull final IngredientEventService eventService
    ) {
        final var isPassed = checkIngredientNameDuplicate(request.getName(), ingredientService)
                             && checkIngredientCodeDuplicate(request.getCode(), ingredientService)
                             && checkIngredientParentExist(request.getParentId(), ingredientService)
                             && checkIngredientTypeUnitAndUnitType(request, ingredientService)
                ;
        if(!isPassed && nonNull(request.getId())) {
            TransactionLogic.trackEvent(request, INGREDIENT_MODIFY, FAILED,
                    ingredientService, historyService, eventService);
        }

        return logWrapper(isPassed,"beforeSaveIngredientCheckConstraint : {}");
    }

    /**
     * Check ingredient type is match unit and unit type with it parent
     * @param request           IngredientRequest
     * @param ingredientService IngredientService
     * @return                  Boolean
     */
    public Boolean checkIngredientTypeUnitAndUnitType(
            @NotNull final IngredientRequest request,
            @NotNull final IngredientService ingredientService
    ) {
        final var parent = nonNull(request.getParentId()) ? ingredientService.getById(request.getParentId()) : null;
        return ConstraintWrapper.builder()
                .name("Check constrain unit and unit type")
                .check(() -> isNull(parent) || (parent.getUnit().equals(GENERIC) && parent.getUnitType().equals(GENERIC)) ||
                        ( parent.getUnit().equals(request.getUnit()) && parent.getUnitType().equals(request.getUnitType())) )
                .exception(new ConstraintViolateException("Ingredient type unit and unit type are mismatch"))
                .build().constraintCheck();
    }

    /**
     * checkIngredientNameDuplicate
     * @param name              Ingredient name
     * @param ingredientService IngredientService
     * @return                  Boolean
     */
    public Boolean checkIngredientNameDuplicate(
            String name,
            IngredientService ingredientService
    ) {
        return ConstraintWrapper.builder()
                .name("Check name duplicate")
                .check(() -> ingredientService.getAll(where(hasName(name))).isEmpty())
                .exception(new ConstraintViolateException("Ingredient name duplicated : ".concat(name)))
                .build().constraintCheck();
    }

    /**
     * checkIngredientCodeDuplicate
     * @param code              Ingredient unique code
     * @param ingredientService IngredientService
     * @return                  Boolean
     */
    public Boolean checkIngredientCodeDuplicate(
            String code,
            IngredientService ingredientService
    ) {
        return ConstraintWrapper.builder()
                .name("Check name duplicate")
                .check(() -> ingredientService.getAll(where(hasCode(code))).isEmpty())
                .exception(new ConstraintViolateException("Ingredient code duplicated : ".concat(code)))
                .build().constraintCheck();
    }

    /**
     * checkIngredientParentExist
     * @param parentId          Parent ingredient ID
     * @param ingredientService IngredientService
     * @return                  Boolean
     */
    public Boolean checkIngredientParentExist(
            Long                parentId,
            IngredientService   ingredientService
    ) {
        return ConstraintWrapper.builder()
                .name("Check parent exist if has id")
                .check(() -> isNull(parentId) || nonNull(ingredientService.getById(parentId)))
                .exception(new ConstraintViolateException("Parent is not exist with id : ".concat(String.valueOf(parentId))))
                .build().constraintCheck();
    }

    //</editor-fold>

    /**
     * Check ingredient is existed and its config is existed after save
     * @param tenantId          Client ID
     * @param request           IngredientRequest
     * @param ingredientService IngredientService
     * @param transactFlag      Transaction boolean flag
     * @param historyService    IngredientHistoryService
     * @param eventService      IngredientEventService
     * @return                  boolean
     */
    public boolean afterSaveIngredientCheckConstraint(
            @NotNull final Long tenantId,
            @NotNull final IngredientRequest request,
            @NotNull final IngredientService ingredientService,
            @NotNull final AtomicBoolean transactFlag,
            @NotNull final IngredientHistoryService historyService,
            @NotNull final IngredientEventService eventService
    ) {
        if(transactFlag.get()) {
            final var ingredient = ingredientService.getByCode(request.getCode());
            final var isPassed = nonNull(ingredient) &&
                    (isNull(request.getParentId()) || nonNull(ingredientService.getConfig(tenantId, ingredient)));

            request.setId(ingredient.getId());
            TransactionLogic.trackEvent(
                    request, INGREDIENT_CREATE, isPassed ? SUCCESS : FAILED,
                    ingredientService, historyService, eventService);
            return logWrapper(isPassed, "afterSaveIngredientCheckConstraint: {}");
        }
        TransactionLogic.trackEvent(request, INGREDIENT_CREATE, FAILED,
                ingredientService, historyService, eventService);
        return logWrapper(true, "Check constraints after was skipped due to failed transaction");
    }

    //<editor-fold desc="beforeDeleteAllItemsCheckConstraint">

    /**
     * Check constraint before delete multiple item. Firstly,
     * Check the quantity of deleted is less or equal to remaining quantity.
     * Check all deleted items are existed
     * @param request           ItemDeleteAllRequest
     * @param ingredientService IngredientService
     * @param inventoryService  InventoryService
     * @param itemService       ItemServiceImpl
     * @return                  boolean
     */
    public boolean beforeDeleteAllItemsCheckConstraint(
            ItemDeleteAllRequest request,
            IngredientService       ingredientService,
            InventoryService        inventoryService,
            ItemService             itemService
    ) {
        final var ingredient = ingredientService.getById(request.getIngredientId());
        final var isPassed = checkIngredientIsChild(ingredient, request.getIngredientId()) &&
                             checkItemQuantityBeforeDelete(request.getQuantity(), ingredient, inventoryService);
        return logWrapper(isPassed, "beforeDeleteAllItemsCheckConstraint: {}");
    }

    /**
     * Check ingredient is child ingredient
     * @param ingredient    IngredientEntity
     * @param ingredientId  Ingredient ID
     * @return              Boolean
     */
    private Boolean checkIngredientIsChild(
            IngredientEntity    ingredient,
            Long                ingredientId
    ) {
        return ConstraintWrapper.builder()
                .name("Check ingredient is child ingredient")
                .check(() -> nonNull(ingredient) && nonNull(ingredient.getParent()))
                .exception(new ConstraintViolateException("Ingredient is not a child ingredient with id : ".concat(String.valueOf(ingredientId))))
                .build().constraintCheck();
    }

    /**
     * Check item quantity is enough
     * @param deleteQuantity    Quantity of item need to be deleted
     * @param ingredient        IngredientEntity
     * @param inventoryService  InventoryService
     * @return                  Boolean
     */
    public Boolean checkItemQuantityBeforeDelete(
            float deleteQuantity,
            IngredientEntity ingredient,
            InventoryService inventoryService
    ) {

        // Check pre-condition
        assert nonNull(inventoryService);

        // Build constrain wrapper
        final var inventory = inventoryService.getByIngredient(ingredient);
        return ConstraintWrapper.builder()
                .name("Check item quantity is enough")
                .check(() -> nonNull(inventory) && inventory.getQuantity() - inventory.getReserved() - deleteQuantity >= 0 )
                .exception(new ConstraintViolateException("Item quantity is not enough"))
                .build().constraintCheck();
    }

    //</editor-fold>

    /**
     * Check ingredient must be existed before update.
     * And, if it is an ingredient type, it must not have children.
     * Finally, if it is an ingredient category, it must not have parents
     * @param request           IngredientRequest
     * @param ingredientService IngredientService
     * @param historyService    IngredientHistoryService
     * @param eventService      Event service
     * @return                  boolean
     */
    public boolean checkConstraintBeforeUpdateIngredient(
            @NotNull final IngredientRequest request,
            @NotNull final IngredientService ingredientService,
            @NotNull final IngredientHistoryService historyService,
            @NotNull final IngredientEventService eventService
    ) {
        final var ingredient = ingredientService.getById(request.getId());

        // Check constraint before update ingredient
        final var isPassed = nonNull(ingredientService.getById(request.getId())) &&
                !(nonNull(request.getParentId()) && !ingredient.getChildren().isEmpty()) &&
                !(!request.getChildrenIds().isEmpty() && nonNull(ingredient.getParent())) &&
                checkIngredientNotDuplicateByCode(request, ingredientService);

        // Track update failed event when update ingredient
        if(!isPassed) {
            TransactionLogic.trackEvent(request, INGREDIENT_MODIFY, FAILED,
                    ingredientService, historyService, eventService);
        }

        return logWrapper(isPassed, "updateIngredientCheckConstraint: {}");
    }

    /**
     * Check ingredient is not duplicated by code
     * @param request           IngredientRequest
     * @param ingredientService IngredientService
     * @return                  boolean
     */
    private boolean checkIngredientNotDuplicateByCode(
            @NotNull final IngredientRequest request,
            @NotNull final IngredientService ingredientService
    ) {
        final var ingredient = ingredientService.getByCode(request.getCode());
        return ConstraintWrapper.builder()
                .name("Check constrain ingredient is not duplicated by code")
                .check(() -> isNull(ingredient) || ingredient.getId().equals(request.getId()))
                .exception(new ConstraintViolateException("ingredient is not duplicated by code : ".concat(request.getCode())))
                .build().constraintCheck();
    }

    /**
     * Check update code is existed
     * @param request           IngredientRequest
     * @param ingredientService IngredientService
     * @param historyService    IngredientHistoryService
     * @param eventService      IngredientEventService
     * @return                  boolean
     */
    public boolean checkConstraintAfterUpdateIngredient(
            @NotNull final IngredientRequest request,
            @NotNull final IngredientService ingredientService,
            @NotNull final IngredientHistoryService historyService,
            @NotNull final IngredientEventService eventService
    ) {
        // Check constraint before update ingredient
        final var isPassed = Objects.nonNull(ingredientService.getByCode(request.getCode()));

        // Track update event when update ingredient
        TransactionLogic.trackEvent(
                request, INGREDIENT_MODIFY, isPassed ? SUCCESS : FAILED,
                ingredientService, historyService, eventService
        );

        return logWrapper(isPassed, "checkConstraintAfterUpdateIngredient: {}");
    }

    //<editor-fold desc="checkConstraintBeforeSaveItem">

    /**
     * Check item must not be existed before create (no duplicate code).
     * Check item's ingredient exist. And, that ingredient must not be category.
     * Finally, the expired date must be in the future.
     * @param request           ItemRequest
     * @param ingredientService IngredientService
     * @param itemService       ItemService
     * @param historyService    IngredientHistoryService
     * @param eventService      IngredientEventService
     * @return                  boolean
     */
    public boolean checkConstraintBeforeSaveItem(
            @NotNull final ItemRequest request,
            @NotNull final IngredientService ingredientService,
            @NotNull final ItemService itemService,
            @NotNull final IngredientHistoryService historyService,
            @NotNull final IngredientEventService eventService
    ) {
        final var isPassed =    checkItemNotDuplicateByCode(request, itemService) &&
                                checkSaveItemRequestIngredientIsChild(request, ingredientService) &&
                                checkItemExpiration(request) &&
                                checkItemTypeUnitAndUnitType(request,ingredientService);
        if(!isPassed){
            TransactionLogic.trackEvent(request, INGREDIENT_ITEM_ADD, FAILED,
                    ingredientService, historyService, eventService);
        }
        return logWrapper(isPassed,"checkConstraintBeforeSaveItem: {}");
    }

    /**
     * Check save ingredient of item is child ingredient item
     * @param request           ItemRequest
     * @param ingredientService IngredientService
     * @return                  Boolean
     */
    public Boolean checkSaveItemRequestIngredientIsChild(
            ItemRequest request,
            IngredientService ingredientService
    ) {
        final var ingredient = ingredientService.getById(requireNonNull(requireNonNull(request).getIngredientId()));
        return checkIngredientIsChild(ingredient, request.getIngredientId());
    }

    /**
     * Check item unit type and uint match ingredient
     * @param request           ItemRequest
     * @param ingredientService IngredientService
     * @return                  boolean
     */
    public Boolean checkItemTypeUnitAndUnitType(
            @NotNull final ItemRequest request,
            @NotNull final IngredientService ingredientService
    ) {
        final var ingredient = nonNull(request.getIngredientId()) ? ingredientService.getById(request.getIngredientId()) : null;
        return ConstraintWrapper.builder()
                .name("Check constrain unit and unit type in item")
                .check(() -> isNull(ingredient) || ( ingredient.getUnit().equals(request.getUnit()) && ingredient.getUnitType().equals(request.getUnitType())) )
                .exception(new ConstraintViolateException("Item type unit and unit type are mismatch"))
                .build().constraintCheck();
    }

    //</editor-fold>

    //<editor-fold desc="Check constraint after save item">

    /**
     * Check constraint after save item
     * @param request           ItemRequest
     * @param itemService       ItemService
     * @param ingredientService IngredientService
     * @param historyService    IngredientHistoryService
     * @param eventService      IngredientEventService
     * @return                  boolean
     */
    public boolean checkConstraintAfterSaveItem(
            @NotNull final ItemRequest request,
            @NotNull final ItemService itemService,
            @NotNull final IngredientService ingredientService,
            @NotNull final IngredientHistoryService historyService,
            @NotNull final IngredientEventService eventService
    ) {
        final var isPassed = checkItemExistByCode(request, itemService);
        TransactionLogic.trackEvent(
                request, INGREDIENT_ITEM_ADD, isPassed ? SUCCESS : FAILED,
                ingredientService, historyService, eventService
        );
        return logWrapper(isPassed, "checkConstraintAfterSaveItem : {}");
    }

    /**
     * Check constrain item is existed by code
     * @param request       ItemRequest
     * @param itemService   ItemService
     * @return              boolean
     */
    private static boolean checkItemExistByCode(
            @NotNull final ItemRequest request,
            @NotNull final ItemService itemService
    ) {
        return ConstraintWrapper.builder()
                .name("Check constrain item is existed by code")
                .check(() -> nonNull(itemService.getByCode(request.getClientId(), request.getCode())))
                .exception(new ConstraintViolateException("Item is not existed by code : ".concat(request.getCode())))
                .build().constraintCheck();
    }

    //</editor-fold>

    //<editor-fold desc="checkConstraintBeforeUpdateItem">

    /**
     * Check item must be existed before update.
     * Check item have duplicated code.
     * Finally, the expired date must be in the future.
     * During the checking the event of update item wil be tracked.
     * @param request           ItemRequest
     * @param itemService       ItemService
     * @param ingredientService ingredientService
     * @param historyService    IngredientHistoryService
     * @param eventService      IngredientEventService
     * @return                  boolean
     */
    public boolean checkConstraintBeforeUpdateItem(
            @NotNull final ItemRequest request,
            @NotNull final ItemService itemService,
            @NotNull final IngredientService ingredientService,
            @NotNull final IngredientHistoryService historyService,
            @NotNull final IngredientEventService eventService
    ) {
        // Constraint checking
        final var isPassed = checkItemExistById(request, itemService) &&
                             checkItemNotDuplicateByCode(request, itemService) &&
                             checkItemExpiration(request);

        // Track update item history
        if(!isPassed) {
            TransactionLogic.trackEvent(request, INGREDIENT_ITEM_MODIFY, FAILED,
                    ingredientService, historyService, eventService);
        }

        return logWrapper(isPassed, "checkConstraintAfterUpdateItem: {}");
    }

    /**
     * Check item exist by id
     * @param request       ItemRequest
     * @param itemService   ItemService
     * @return              boolean
     */
    public Boolean checkItemExistById(
            @NotNull final ItemRequest request,
            @NotNull final ItemService itemService
    ) {
        return ConstraintWrapper.builder()
                .name("Check constrain item is existed by id")
                .check(() -> nonNull(request.getId()) && nonNull(itemService.getById(request.getId())))
                .exception(new ConstraintViolateException("Item is not existed by id : ".concat(String.valueOf(request.getId()))))
                .build().constraintCheck();
    }

    /**
     * Check constrain item is existed by code
     * @param request       ItemRequest
     * @param itemService   ItemService
     * @return              Boolean
     */
    public Boolean checkItemNotDuplicateByCode(
            ItemRequest request,
            ItemService itemService
    ) {
        return ConstraintWrapper.builder()
                .name("Check constrain item is not duplicate by code")
                .check(() -> isNull(itemService.getByCode(request.getClientId(), request.getCode())) ||
                        itemService.getByCode(request.getClientId(), request.getCode()).getId().equals(request.getId()))
                .exception(new ConstraintViolateException("Item is duplicate by code : ".concat(request.getCode())))
                .build().constraintCheck();
    }

    /**
     * Check constrain item is not expired
     * @param request   ItemRequest
     * @return          Boolean
     */
    public Boolean checkItemExpiration(
            ItemRequest request
    ) {
        return ConstraintWrapper.builder()
                .name("Check constrain item is not expired")
                .check(() -> !LocalDate.now().isAfter(LocalDate.parse(requireNonNull(request.getExpiredAt()))))
                .exception(new ConstraintViolateException("Item is expired"))
                .build().constraintCheck();
    }

    //</editor-fold>

    /**
     * Check item must be existed after update.
     * Check item have  code.
     * During the checking the event of update item wil be tracked.
     * @param request           ItemRequest
     * @param itemService       ItemService
     * @param ingredientService ingredientService
     * @param historyService    IngredientHistoryService
     * @param eventService      IngredientEventService
     * @return                  boolean
     */
    public boolean checkConstraintAfterUpdateItem(
            @NotNull final ItemRequest request,
            @NotNull final ItemService itemService,
            @NotNull final IngredientService ingredientService,
            @NotNull final IngredientHistoryService historyService,
            @NotNull final IngredientEventService eventService
    ) {
        // Constraint checking
        final var isPassed = nonNull(request.getId()) &&
                itemService.getById(request.getId()).getCode().equals(request.getCode());

        // Track update item history
        TransactionLogic.trackEvent(
                request, INGREDIENT_ITEM_MODIFY, isPassed ? SUCCESS : FAILED,
                ingredientService, historyService, eventService
        );

        return logWrapper(isPassed, "checkConstraintAfterUpdateItem: {}");
    }

    /**
     * Check constraint when updating inventory
     * @param request           InventoryRequest
     * @param inventoryService  InventoryService
     * @return                  boolean
     */
    public boolean updateInventoryCheckConstraint(
            InventoryRequest request,
            InventoryService inventoryService
    ) {
        final var ingredient = inventoryService.getByName(request.getClientId(), request.getName());
        return logWrapper(nonNull(inventoryService.getById(request.getId())) &&
                (isNull(ingredient) || ingredient.getId().equals(request.getId())),
                "updateInventoryCheckConstraint: {}");
    }

    /**
     * Check constraint before delete item by id.
     * During the checking,track item remove event
     * @param id                Item unique id
     * @param itemService       ItemService
     * @param ingredientService IngredientService
     * @param historyService    IngredientHistoryService
     * @param eventService      IngredientEventService
     * @return                  boolean
     */
    public boolean checkConstraintBeforeDeleteItem(
            @NotNull final Long id,
            @NotNull final ItemService itemService,
            @NotNull final IngredientService ingredientService,
            @NotNull final IngredientHistoryService historyService,
            @NotNull final IngredientEventService eventService
    ) {
        // Check constraint before delete item by id
        final var isPassed = isItemExistById(id, itemService);

        // Track item remove event
        if(isPassed)
            TransactionLogic.trackEvent(
                    ItemMapper.toRequest(itemService.getById(id)),
                    INGREDIENT_ITEM_REMOVE, SUCCESS,
                    ingredientService, historyService, eventService
            );

        return logWrapper(isPassed, "checkConstraintBeforeDeleteItem: {}");
    }

    /**
     * Check constraint before delete item by id.
     * During the checking,track item remove event
     * @param id                Item unique id
     * @param itemService       ItemService
     * @param ingredientService IngredientService
     * @param historyService    IngredientHistoryService
     * @return                  boolean
     */
    public boolean checkConstraintAfterDeleteItem(
            @NotNull final Long id,
            @NotNull final ItemService itemService,
            @NotNull final IngredientService ingredientService,
            @NotNull final IngredientHistoryService historyService
    ) {
        // Check constraint before delete item by id
        final var isPassed = isItemNotExistById(id, itemService);
        return logWrapper(isPassed, "checkConstraintAfterDeleteItem: {}");
    }

    /**
     * Check ingredient entity by id
     * @param id                Entity ID
     * @param ingredientService IngredientService
     * @return                  boolean
     */
    public boolean isIngredientExistById(
            Long                id,
            IngredientService   ingredientService
    ) {
        return logWrapper(nonNull(ingredientService.getById(id)), "isIngredientExistById: {}");
    }

    /**
     * Check item entity by id
     * @param id                Entity ID
     * @param itemService I     temService
     * @return                  boolean
     */
    public boolean isItemExistById(
            @NotNull final Long        id,
            @NotNull final ItemService itemService
    ) {
        return logWrapper(nonNull(itemService.getById(id)), "isItemExistById: {}");
    }

    /**
     * Check item not entity by id
     * @param id                Entity ID
     * @param itemService       ItemService
     * @return                  boolean
     */
    public boolean isItemNotExistById(
            @NotNull final Long        id,
            @NotNull final ItemService itemService
    ) {
        return logWrapper(isNull(itemService.getById(id)), "isItemNotExistById: {}");
    }

    /**
     * Check constraints before saving items
     * @param request           BatchItemsRequest
     * @param ingredientService IngredientService
     * @param itemService       ItemService
     * @param historyService    IngredientHistoryService
     * @param eventService      IngredientEventService
     * @return                  boolean
     */
    public boolean checkConstraintBeforeSaveItems(
            @NotNull final BatchItemsRequest request,
            @NotNull final IngredientService ingredientService,
            @NotNull final ItemService itemService,
            @NotNull final IngredientHistoryService historyService,
            @NotNull final IngredientEventService eventService,
            @NotNull final SupplierClient supplierClient
    ) {
        // Check constraint on ingredient
        var isPassed =  checkSaveItemRequestIngredientIsChild(request, ingredientService) &&
                        checkBatchItemQuantityWhenCodeSetIsProvided(request) &&
                        checkItemCanBeAdd(request, request.getQuantity(), supplierClient) &&
                        checkItemExpiration(request);

        // Check all code are whether unique
        if(nonNull(request.getCodes()) && !request.getCodes().isEmpty())
            isPassed &= request.getCodes().stream()
                        .map(code -> isNull(itemService.getByCode(request.getClientId(), code)))
                        .reduce(true, (a, b) -> a && b);

        // Track add item batch history if check constrain is failed
        if(!isPassed) {
            TransactionLogic.trackEvent(request, INGREDIENT_ITEM_BATCH_ADD, FAILED,
                    ingredientService, historyService, eventService);
        }

        return logWrapper(isPassed, "checkConstraintBeforeSaveItems: {}");
    }

    private boolean checkItemCanBeAdd(
            final @NotNull ItemRequest request, final float quantity,
            final @NotNull SupplierClient supplierClient) {
        return supplierClient.isIngredientCanBeProvidable(request.getImportId(),
                request.getIngredientId(), quantity);
    }

    /**
     * Check constraint after save items
     * @param request           BatchItemsRequest
     * @param ingredientService IngredientService
     * @param itemService       ItemService
     * @param historyService    IngredientHistoryService
     * @param eventService      IngredientEventService
     * @return                  boolean
     */
    public boolean checkConstraintAfterSaveItems(
            @NotNull final BatchItemsRequest request,
            @NotNull final IngredientService ingredientService,
            @NotNull final ItemService itemService,
            @NotNull final IngredientHistoryService historyService,
            @NotNull final IngredientEventService eventService
    ) {
        final var isPassed = isNull(request.getCodes()) || request.getCodes().isEmpty() ||
                             request.getCodes().stream()
                                .map(code -> nonNull(itemService.getByCode(request.getClientId(), code)))
                                .reduce(true, (a, b) -> a && b);
        TransactionLogic.trackEvent(
                request, INGREDIENT_ITEM_BATCH_ADD,
                isPassed ? SUCCESS : FAILED,
                ingredientService, historyService, eventService);
        return logWrapper(isPassed, "checkConstraintAfterSaveItems: {}");
    }

    /**
     * Check batch item quantity when code set is provided
     * @param request   BatchItemsRequest
     * @return          boolean
     */
    public Boolean checkBatchItemQuantityWhenCodeSetIsProvided(
            @NotNull final BatchItemsRequest request
    ) {
        return ConstraintWrapper.builder()
                .name("Check batch item quantity when code set is provided")
                .check(() -> isNull(request.getCodes()) || request.getCodes().isEmpty() ||
                             Objects.equals(request.getCodes().size(), request.getQuantity()))
                .exception(new ConstraintViolateException("Item quantity and code set size are not equal"))
                .build().constraintCheck();
    }

    /**
     * Check ingredient entity by id
     * @param id                Entity ID
     * @param ingredientService IngredientService
     * @return                  boolean
     */
    public boolean checkConstraintBeforeSyncInventoryByIngredient(
            Long id,
            IngredientService ingredientService
    ) {
        final var ingredient = ingredientService.getById(id);
        return logWrapper(nonNull(ingredient) && !ingredient.isCategory()
                , "checkConstraintBeforeSyncInventoryByIngredient: {}");
    }

    /**
     * Log wrapper
     * @param result    boolean
     * @param message   Log message
     * @return          boolean
     */
    private boolean logWrapper(boolean result, String message) {
        log.info(message, result);
        return result;
    }
}
