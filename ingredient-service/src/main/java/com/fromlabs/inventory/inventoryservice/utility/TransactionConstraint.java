/*
 * Copyright (c) 2021. Liem Doan
 */

package com.fromlabs.inventory.inventoryservice.utility;

import com.fromlabs.inventory.inventoryservice.common.exception.ConstraintViolateException;
import com.fromlabs.inventory.inventoryservice.common.wrapper.ConstraintWrapper;
import com.fromlabs.inventory.inventoryservice.ingredient.*;
import com.fromlabs.inventory.inventoryservice.ingredient.beans.IngredientRequest;
import com.fromlabs.inventory.inventoryservice.inventory.InventoryService;
import com.fromlabs.inventory.inventoryservice.inventory.beans.InventoryRequest;
import com.fromlabs.inventory.inventoryservice.item.ItemService;
import com.fromlabs.inventory.inventoryservice.item.beans.*;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.fromlabs.inventory.inventoryservice.ingredient.beans.IngredientSpecification.*;
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
     * @param request IngredientRequest
     * @return boolean
     * @see Objects
     */
    public boolean beforeSaveIngredientCheckConstraint(
            IngredientRequest request,
            IngredientService ingredientService
    ) {
        final var isPassed =    checkIngredientNameDuplicate(request.getName(), ingredientService) &&
                                checkIngredientCodeDuplicate(request.getCode(), ingredientService) &&
                                checkIngredientParentExist(  request.getParentId(), ingredientService) &&
                                checkIngredientTypeUnitAndUnitType(request, ingredientService);
        return logWrapper(isPassed,"beforeSaveIngredientCheckConstraint : {}");
    }

    /**
     * Check ingredient type is match unit and unit type with it parent
     * @param request           IngredientRequest
     * @param ingredientService IngredientService
     * @return                  Boolean
     */
    public Boolean checkIngredientTypeUnitAndUnitType(
            IngredientRequest request,
            IngredientService ingredientService
    ) {
        final var parent = nonNull(request.getParentId()) ? ingredientService.get(request.getParentId()) : null;
        return ConstraintWrapper.builder()
                .name("Check constrain unit and unit type")
                .check(() -> isNull(parent) || ( parent.getUnit().equals(request.getUnit()) && parent.getUnitType().equals(request.getUnitType()) )  )
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
                .check(() -> isNull(parentId) || nonNull(ingredientService.get(parentId)))
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
     * @return                  boolean
     */
    public boolean afterSaveIngredientCheckConstraint(
            Long tenantId,
            IngredientRequest request,
            IngredientService ingredientService,
            AtomicBoolean transactFlag
    ) {
        if(transactFlag.get()) {
            final var ingredient = ingredientService.get(request.getCode());
            return logWrapper(nonNull(ingredient) &&
                    (isNull(request.getParentId()) || nonNull(ingredientService.getConfig(tenantId, ingredient))),
                    "afterSaveIngredientCheckConstraint: {}");
        }
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
            ItemDeleteAllRequest    request,
            IngredientService       ingredientService,
            InventoryService        inventoryService,
            ItemService             itemService
    ) {
        final var ingredient = ingredientService.get(request.getIngredientId());
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
        final var inventory = inventoryService.get(ingredient);
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
     * @return                  boolean
     */
    public boolean updateIngredientCheckConstraint(
            IngredientRequest request,
            IngredientService ingredientService
    ) {
        final var ingredient = ingredientService.get(request.getId());
        return logWrapper(nonNull(ingredientService.get(request.getId())) &&
                !(nonNull(request.getParentId()) && !ingredient.getChildren().isEmpty()) &&
                !(!request.getChildrenIds().isEmpty() && nonNull(ingredient.getParent())),
                "updateIngredientCheckConstraint: {}");
    }

    //<editor-fold desc="saveItemCheckConstraint">

    /**
     * Check item must not be existed before create (no duplicate code).
     * Check item's ingredient exist. And, that ingredient must not be category.
     * Finally, the expired date must be in the future.
     * @param request           ItemRequest
     * @param ingredientService IngredientService
     * @param itemService       ItemService
     * @return                  boolean
     */
    public boolean saveItemCheckConstraint(
            ItemRequest request,
            IngredientService ingredientService,
            ItemService itemService
    ) {
        final var isPassed =    checkItemExistByCode(request, itemService) &&
                                checkSaveItemRequestIngredientIsChild(request, ingredientService) &&
                                checkItemExpiration(request);
        return logWrapper(isPassed,"saveItemCheckConstraint: {}");
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
        final var ingredient = ingredientService.get(requireNonNull(requireNonNull(request).getIngredientId()));
        return checkIngredientIsChild(ingredient, request.getIngredientId());
    }

    //</editor-fold>

    //<editor-fold desc="updateItemCheckConstraint">

    /**
     * Check item must be existed before update.
     * Check item hase duplicated code.
     * Finally, the expired date must be in the future.
     * @param request       ItemRequest
     * @param itemService   ItemService
     * @return              boolean
     */
    public boolean updateItemCheckConstraint(
            ItemRequest request,
            ItemService itemService
    ) {
        final var isPassed = checkItemExistById(request, itemService) &&
                             checkItemExistByCode(request, itemService) &&
                             checkItemExpiration(request);
        return logWrapper(isPassed, "updateItemCheckConstraint: {}");
    }

    public Boolean checkItemExistById(
            ItemRequest request,
            ItemService itemService
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
    public Boolean checkItemExistByCode(
            ItemRequest request,
            ItemService itemService
    ) {
        return ConstraintWrapper.builder()
                .name("Check constrain item is existed by code")
                .check(() -> isNull(itemService.getByCode(request.getClientId(), request.getCode())) ||
                        itemService.getByCode(request.getClientId(), request.getCode()).getId().equals(request.getId()))
                .exception(new ConstraintViolateException("Item is not existed by code : ".concat(request.getCode())))
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
     * Check constraint when updating inventory
     * @param request           InventoryRequest
     * @param inventoryService  InventoryService
     * @return                  boolean
     */
    public boolean updateInventoryCheckConstraint(
            InventoryRequest request,
            InventoryService inventoryService
    ) {
        final var ingredient = inventoryService.get(request.getClientId(), request.getName());
        return logWrapper(nonNull(inventoryService.get(request.getId())) &&
                (isNull(ingredient) || ingredient.getId().equals(request.getId())),
                "updateInventoryCheckConstraint: {}");
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
        return logWrapper(nonNull(ingredientService.get(id)), "isIngredientExistById: {}");
    }

    /**
     * Check item entity by id
     * @param id                Entity ID
     * @param itemService ItemService
     * @return                  boolean
     */
    public boolean isItemExistById(
            Long        id,
            ItemService itemService
    ) {
        return logWrapper(nonNull(itemService.getById(id)), "isItemExistById: {}");
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
        final var ingredient = ingredientService.get(id);
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
