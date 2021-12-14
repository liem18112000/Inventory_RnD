/*
 * Copyright (c) 2021. Liem Doan
 */

package com.fromlabs.inventory.inventoryservice.utility;

import com.fromlabs.inventory.inventoryservice.common.dto.SimpleDto;
import com.fromlabs.inventory.inventoryservice.common.entity.BaseEntity;
import com.fromlabs.inventory.inventoryservice.common.transaction.*;
import com.fromlabs.inventory.inventoryservice.ingredient.*;
import com.fromlabs.inventory.inventoryservice.ingredient.beans.*;
import com.fromlabs.inventory.inventoryservice.ingredient.config.IngredientConfigEntity;
import com.fromlabs.inventory.inventoryservice.inventory.*;
import com.fromlabs.inventory.inventoryservice.inventory.beans.*;
import com.fromlabs.inventory.inventoryservice.inventory.factory.InventoryEntityFactory;
import com.fromlabs.inventory.inventoryservice.item.*;
import com.fromlabs.inventory.inventoryservice.item.beans.*;
import com.fromlabs.inventory.inventoryservice.item.strategy.DeleteStrategy;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.net.InetAddress;
import java.time.Instant;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import static com.fromlabs.inventory.inventoryservice.common.factory.FactoryCreateType.DEFAULT;
import static com.fromlabs.inventory.inventoryservice.ingredient.IngredientEntity.from;
import static com.fromlabs.inventory.inventoryservice.inventory.beans.InventorySpecification.filter;
import static com.fromlabs.inventory.inventoryservice.utility.TransactionWrapper.*;
import static java.util.Objects.*;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.ResponseEntity.*;

/**
 * <h1>Transaction Logic Layer</h1>
 *
 * <h2>Brief Information</h2>
 *
 * <p>
 *     This the fourth layer in the segment of non-infrastructure service layer.
 *     In detail, it play as the most important segment service of all.
 *     Here is the business domain logic or high-level application logic is located
 * </p>
 *
 * <p>
 *     It is a mandatory layer as it must be implemented to keep the
 *     domain service layer or high-level application layer run
 * </p>
 *
 * <h2>Usages</h2>
 *
 * <p>Developer can implement the logic here clearly with the injection of all dependency of services</p>
 *
 * <p></p>
 */
@UtilityClass
@Slf4j
public class TransactionLogic {

    //<editor-fold desc="getIngredientPageWithFilter">

    /**
     * getIngredientPageWithFilter
     * @param request           IngredientPageRequest
     * @param ingredientService IngredientService
     * @param inventoryService  InventoryService
     * @return Page&lt;IngredientDto&gt;
     */
    public Page<IngredientDto> getIngredientPageWithFilter(
            IngredientPageRequest   request,
            IngredientService       ingredientService,
            InventoryService        inventoryService
    ) {
        log.info("getIngredientPageWithFilter : start");

        // Check pre-condition
        assert nonNull(request);
        assert nonNull(ingredientService);
        assert nonNull(inventoryService);

        // Get page of Ingredient Entity
        var page = getIngredientDtoPage(request, ingredientService);

        // If request parent id is null or there is no ingredient with the ke key return page
        if (isNull(request.getParentId()) || isNull(ingredientService.get(request.getParentId()))) return page;

        // Otherwise, page is updated as each item's quantity will be retrieved from inventory
        return page.map(ingredient -> setIngredientQuantity(inventoryService, ingredient));
    }

    /**
     * setIngredientQuantity
     * @param inventoryService  InventoryService
     * @param ingredient        IngredientDto
     * @return IngredientDto
     */
    public IngredientDto setIngredientQuantity(
            InventoryService    inventoryService,
            IngredientDto       ingredient
    ) {
        log.info("setIngredientQuantity");

        // Check pre-condition
        assert nonNull(inventoryService);
        assert nonNull(ingredient);

        // Get inventory by ingredient
        final var inventory = getInventoryEntity(inventoryService, ingredient);

        // If inventory exist, set inventory quantity to DTO and then return Ingredient DTO
        // Otherwise set quantity to zero as default
        ingredient.setQuantity(isNull(inventory) ? 0f : inventory.getQuantity());
        return ingredient;
    }

    /**
     * getInventoryEntity
     * @param inventoryService  InventoryService
     * @param ingredient        IngredientDto
     * @return InventoryEntity
     */
    private InventoryEntity getInventoryEntity(
            InventoryService inventoryService,
            IngredientDto ingredient
    ) {
        log.info("getIngredientPageWithFilter : getInventoryEntity");

        // Check pre-condition
        assert nonNull(inventoryService);
        assert nonNull(ingredient);

        // Convert ingredient DTO to entity
        final var entity = IngredientEntity.from(ingredient);

        // Get inventory from ingredient and then return
        return inventoryService.get(entity);
    }

    /**
     * getIngredientDtoPage
     * @param request           IngredientPageRequest
     * @param ingredientService IngredientService
     * @return Page&lt;IngredientDto&gt;
     */
    private Page<IngredientDto> getIngredientDtoPage(
            IngredientPageRequest   request,
            IngredientService       ingredientService
    ) {
        log.info("getIngredientPageWithFilter : getIngredientDtoPage");

        // Check pre-condition
        assert nonNull(request);
        assert nonNull(ingredientService);

        // Get ingredient entity page
        final var page = requireNonNull(getIngredientEntityPage(request, ingredientService));

        // Convert ingredient entity page to ingredient DTO page
        return IngredientDto.from(page);
    }

    /**
     * getIngredientEntityPage
     * @param request           IngredientPageRequest
     * @param ingredientService IngredientService
     * @return Page&lt;IngredientEntity&gt;
     */
    private Page<IngredientEntity> getIngredientEntityPage(
            IngredientPageRequest request,
            IngredientService     ingredientService
    ) {
        log.info("getIngredientPageWithFilter : getIngredientEntityPage");

        // Check pre-condition
        assert nonNull(request.getParentId());
        assert nonNull(request.getPageable());
        assert nonNull(ingredientService);

        // Get ingredient entity and parent
        final var entity = from(request);
        final var parent = ingredientService.get(request.getParentId());

        // Get specification from entity and parent
        final var specification = IngredientSpecification.filter(entity, parent);

        // Get ingredient page
        return ingredientService.getPage(specification, request.getPageable());
    }

    //</editor-fold>

    //<editor-fold desc="getIngredientByIdWithQuantity">

    /**
     * Get Ingredient by id with quantity
     * @param id                Ingredient ID
     * @param ingredientService IngredientService
     * @param inventoryService  InventoryService
     * @return                  IngredientDto
     */
    public IngredientDto getIngredientByIdWithQuantity(
            Long                id,
            IngredientService   ingredientService,
            InventoryService    inventoryService
    ) {
        log.info("getIngredientByIdWithQuantity");

        // Check pre-condition
        assert nonNull(ingredientService);
        assert nonNull(inventoryService);

        // Gwt ingredient by id
        final var ingredient = requireNonNull(ingredientService.get(id));

        // Convert to ingredient entity to DTO
        var dto = requireNonNull(IngredientDto.from(ingredient));

        // If ingredient is child set ingredient quantity
        if(nonNull(ingredient.getParent())) setIngredientQuantity(inventoryService, dto);

        // Otherwise, return the DTO
        return dto;
    }

    //</editor-fold>

    //<editor-fold desc="getIngredientByCodeWithQuantity">

    /**
     * Get ingredient by code with quantity
     * @param code              Unique Ingredient code
     * @param ingredientService IngredientService
     * @param inventoryService  InventoryService
     * @return                  IngredientDto
     */
    public IngredientDto getIngredientByCodeWithQuantity(
            String              code,
            IngredientService   ingredientService,
            InventoryService    inventoryService
    ) {
        log.info("getIngredientByCodeWithQuantity");

        // Check pre-condition
        assert nonNull(ingredientService);
        assert nonNull(inventoryService);

        // Get ingredient by code
        final var ingredient = requireNonNull(ingredientService.get(code));

        // Convert ingredient entity to DTO
        var dto = requireNonNull(IngredientDto.from(ingredient));

        // If ingredient is child set ingredient quantity
        if(nonNull(ingredient.getParent())) setIngredientQuantity(inventoryService, dto);

        // Otherwise, return the DTO
        return dto;
    }

    //</editor-fold>

    //<editor-fold desc="getInventoryPageWithFilter">

    /**
     * Atomic method for getting inventory with pagination and filter
     * @param request           InventoryPageRequest
     * @param ingredientService IngredientService
     * @param inventoryService  IngredientService
     * @return                  Page&lt;InventoryDto&gt;
     */
    public Page<InventoryDto> getInventoryPageWithFilter(
            InventoryPageRequest    request,
            IngredientService       ingredientService,
            InventoryService        inventoryService
    ) {
        log.info("getInventoryPageWithFilter : start");

        // Check pre-condition
        assert nonNull(request);
        assert nonNull(ingredientService);
        assert nonNull(inventoryService);

        // Get inventory entity page
        final var inventoryPage = requireNonNull(getInventoryEntityPage(request, ingredientService, inventoryService));

        // Convert inventory entity page to inventory DTO page and then return
        return InventoryDto.from(inventoryPage);
    }

    /**
     * Get all inventory with pagination
     * @param request           InventoryPageRequest
     * @param ingredientService IngredientService
     * @param inventoryService  InventoryService
     * @return Page&lt;InventoryEntity&gt;
     */
    private Page<InventoryEntity> getInventoryEntityPage(
            InventoryPageRequest    request,
            IngredientService       ingredientService,
            InventoryService        inventoryService
    ) {
        log.info("getInventoryPageWithFilter : getInventoryEntityPage");

        // Check pre-condition
        assert nonNull(request);
        assert nonNull(ingredientService);
        assert nonNull(request.getPageable());

        // Get specification from request
        final var specification = getInventoryEntitySpecification(request, ingredientService);

        // Get page of inventory from specification and pageable
        return inventoryService.getPage(specification, request.getPageable());
    }

    /**
     * getInventoryEntitySpecification
     * @param request           InventoryPageRequest
     * @param ingredientService IngredientService
     * @return Specification&lt;InventoryEntity&gt;
     */
    private Specification<InventoryEntity> getInventoryEntitySpecification(
            InventoryPageRequest request,
            IngredientService    ingredientService
    ) {
        log.info("getInventoryPageWithFilter : getInventoryEntitySpecification");
        return filter(InventoryEntity.from(request), getIngredientEntity(request, ingredientService));
    }

    /**
     * getIngredientEntity
     * @param request           InventoryPageRequest
     * @param ingredientService IngredientService
     * @return  IngredientEntity
     */
    private IngredientEntity getIngredientEntity(
            InventoryPageRequest request,
            IngredientService    ingredientService
    ) {
        log.info("getInventoryPageWithFilter : getIngredientEntity");

        // Check pre-condition
        assert nonNull(ingredientService);
        assert nonNull(request);

        // If ingredient id from request is null return null
        // Otherwise, return the ingredient with provided id from request
        return isNull(request.getIngredientId()) ? null : ingredientService.get(request.getIngredientId());
    }

    //</editor-fold>

    //<editor-fold desc="getItemPageWithFiler">

    /**
     * Atomic method for getting all item with pagination and filter
     * @param request ItemPageRequest
     * @return Page&lt;ItemDto&gt;
     */
    public Page<ItemDto> getItemPageWithFiler(
            ItemPageRequest     request,
            IngredientService   ingredientService,
            ItemService         itemService
    ) {
        log.info("getItemPageWithFiler : start");
        return ItemDto.from(getItemEntityPage(request, ingredientService, itemService));
    }

    /**
     * getItemEntityPage
     * @param request           ItemPageRequest
     * @param ingredientService IngredientService
     * @param itemService       ItemService
     * @return Page&lt;ItemEntity&gt;
     */
    private Page<ItemEntity> getItemEntityPage(
            ItemPageRequest     request,
            IngredientService   ingredientService,
            ItemService         itemService
    ) {
        log.info("getItemPageWithFiler : getItemEntityPage");

        // Check pre-condition
        assert nonNull(request);
        assert nonNull(ingredientService);
        assert nonNull(itemService);
        assert nonNull(request.getPageable());

        // Get item specification
        final var specification = requireNonNull(getItemEntitySpecification(request, ingredientService));

        // Get item entity page from specification and pageable
        return itemService.getPage(specification, request.getPageable());
    }

    /**
     * getItemEntitySpecification
     * @param request           ItemPageRequest
     * @param ingredientService IngredientService
     * @return Specification&lt;ItemEntity&gt;
     */
    private Specification<ItemEntity> getItemEntitySpecification(
            ItemPageRequest     request,
            IngredientService   ingredientService
    ) {
        log.info("getItemPageWithFiler : getItemEntitySpecification");

        // Check pre-condition
        assert nonNull(request);
        assert nonNull(ingredientService);

        // Get item value and ingredient from request
        final var item = ItemEntity.from(request);
        final var ingredient = getIngredientEntity(request, ingredientService);

        // Get specification and then return
        return ItemSpecification.filter(item, ingredient);
    }

    /**
     * getIngredientEntity
     * @param request           ItemPageRequest
     * @param ingredientService IngredientService
     * @return  IngredientEntity
     */
    private IngredientEntity getIngredientEntity(
            ItemPageRequest     request,
            IngredientService   ingredientService
    ) {
        log.info("getItemPageWithFiler : getIngredientEntity");

        // Check pre-condition
        assert nonNull(request);
        assert nonNull(ingredientService);
        assert nonNull(request.getIngredientId());

        return ingredientService.get(request.getIngredientId());
    }

    //</editor-fold>

    //<editor-fold desc="saveIngredientAndConfig">

    /**
     * Save ingredient and config
     * @param request           IngredientRequest
     * @param ingredientService IngredientService
     * @param transactFlag      AtomicBoolean
     * @return                  ResponseEntity
     */
    public ResponseEntity<?> saveIngredientAndConfig(
            IngredientRequest   request,
            IngredientService   ingredientService,
            AtomicBoolean       transactFlag
    ) {
        log.info("saveIngredientAndConfig: start");

        // Check pre-condition
        assert nonNull(request);
        assert nonNull(ingredientService);
        assert nonNull(transactFlag);

        // Create atomic references for ingredient and config
        AtomicReference<IngredientEntity> ingredient = new AtomicReference<>();
        AtomicReference<IngredientConfigEntity> config = new AtomicReference<>();

        // Build and then run transaction group
        final var result = (RequestTransactionResult) TransactionGuard.builder()
                .transactions(Arrays.asList(
                        getSaveIngredientTransaction(request, ingredientService, ingredient),
                        getSaveConfigTransaction(request, ingredientService, ingredient, config)
                )).build().transact();
        assert nonNull(result);

        // Set transaction flag for later use
        transactFlag.set(result.isTransactionSuccess());

        // Return result
        return status(transactFlag.get() ? CREATED : BAD_REQUEST).body(result);
    }

    //<editor-fold desc="TRANSACTION FLOW">

    /**
     * Rollback transaction for save ingredient config
     * @param ingredientService IngredientService
     * @param config            AtomicReference&lt;IngredientConfigEntity&gt;
     * @return                  Object
     */
    public Object saveIngredientConfigTransactionRollback(
            IngredientService                       ingredientService,
            AtomicReference<IngredientConfigEntity> config
    ) {
        log.info("Rollback saveConfigTransaction");

        // Check pre-condition
        assert nonNull(config);
        assert nonNull(ingredientService);

        // Delete config
        ingredientService.deleteConfig(config.get());

        // return deleted config
        return config;
    }

    /**
     * Process function for save ingredient config
     * @param request           IngredientRequest
     * @param ingredientService IngredientService
     * @param ingredient        AtomicReference&lt;IngredientEntity&gt;
     * @param config            AtomicReference&lt;IngredientConfigEntity>&gt;
     * @return                  SaveIngredientResponse
     */
    public SaveIngredientResponse saveConfigTransactionProcess(
            IngredientRequest                       request,
            IngredientService                       ingredientService,
            AtomicReference<IngredientEntity>       ingredient,
            AtomicReference<IngredientConfigEntity> config
    ) {
        // Check pre-condition
        assert nonNull(request);
        assert nonNull(ingredient);
        assert nonNull(config);
        assert nonNull(ingredientService);

        // Get ingredient entity from atomic reference
        final var ingredientEntity = ingredient.get();
        assert nonNull(ingredientEntity);

        // Save ingredient config entity if ingredient has a parent
        final var parentId = request.getParentId();
        if(nonNull(parentId) && parentId > 0){
            final var savedConfig = ingredientService.saveConfig(getIngredientConfigEntity(request, ingredientEntity));
            config.set(requireNonNull(savedConfig));
        }

        // Convert ingredient entity to DTO get config entity
        final var ingredientDto = IngredientDto.from(ingredientEntity);
        final var configEntity = config.get();

        // Build SaveIngredientResponse response and then return
        return SaveIngredientResponse.builder().ingredient(ingredientDto).config(configEntity).build();
    }

    /**
     * Rollback transaction for save ingredient
     * @param ingredientService IngredientService
     * @param ingredient        AtomicReference&lt;IngredientEntity&gt;
     * @return                  Object
     */
    public Object saveIngredientTransactionRollBack(
            IngredientService                   ingredientService,
            AtomicReference<IngredientEntity>   ingredient
    ) {
        log.info("Rollback saveIngredientTransaction");

        // Check pre-condition
        assert nonNull(ingredient);
        assert nonNull(ingredient.get());
        assert nonNull(ingredientService);

        // Delete ingredient
        ingredientService.delete(ingredient.get());

        // Return deleted ingredient
        return ingredient.get();
    }

    /**
     * Process function for save ingredient
     * @param request           IngredientRequest
     * @param ingredientService IngredientService
     * @param ingredient        AtomicReference&lt;IngredientEntity&gt;
     * @return                  IngredientEntity
     */
    public IngredientEntity saveIngredientTransactionProcess(
            IngredientRequest                   request,
            IngredientService                   ingredientService,
            AtomicReference<IngredientEntity>   ingredient
    ) {
        // Check pre-condition
        assert nonNull(request);
        assert nonNull(ingredient);
        assert nonNull(ingredientService);

        // Save ingredient entity and set to atomic reference and then return
        final var savedIngredient = ingredientService.save(getIngredientEntity(request, ingredientService));
        ingredient.set(savedIngredient);
        return ingredient.get();
    }

    //</editor-fold>

    /**
     * getIngredientConfigEntity
     * @param request       IngredientRequest
     * @param ingredient    IngredientEntity
     * @return              IngredientConfigEntity
     */
    private IngredientConfigEntity getIngredientConfigEntity(
            IngredientRequest   request,
            IngredientEntity    ingredient
    ) {
        log.info("saveIngredientAndConfig: getIngredientConfigEntity");

        // Check pre-condition
        assert nonNull(request);
        assert nonNull(ingredient);

        // Convert config request to entity
        return IngredientConfigEntity.from(request, ingredient);
    }

    /**
     * getIngredientEntity
     * @param request           IngredientRequest
     * @param ingredientService IngredientService
     * @return                  IngredientEntity
     */
    private IngredientEntity getIngredientEntity(
            IngredientRequest request,
            IngredientService ingredientService
    ) {
        log.info("saveIngredientAndConfig: getIngredientEntity");

        // Check pre-condition
        assert nonNull(request);
        assert nonNull(ingredientService);

        // Convert request to ingredient entity
        var ingredient = from(request);
        final var parentId = request.getParentId();

        // If parent id is null or negative return ingredient
        if (isNull(parentId) || parentId <= 0L) return ingredient;

        // Otherwise, set ingredient parent and then return ingredient
        return ingredient.setParent(getIngredientParent(request, ingredientService));
    }

    /**
     * getIngredientParent
     * @param request           IngredientRequest
     * @param ingredientService IngredientService
     * @return                  IngredientEntity
     */
    private IngredientEntity getIngredientParent(
            IngredientRequest request,
            IngredientService ingredientService
    ) {
        log.info("saveIngredientAndConfig: getIngredientParent");

        // Check pre-condition
        assert nonNull(request);
        assert nonNull(ingredientService);

        // if parent id is null return null
        if(isNull(request.getParentId())) return null;

        // Otherwise, get parent ingredient by provide id
        return ingredientService.get(request.getParentId());
    }

    //</editor-fold>

    //<editor-fold desc="saveOrUpdateItem">

    /**
     * Save or update item
     * @param request           ItemRequest
     * @param ingredientService IngredientService
     * @param itemService       ItemService
     * @return                  ResponseEntity
     */
    public ResponseEntity<ItemDto> saveOrUpdateItem(
            ItemRequest         request,
            IngredientService   ingredientService,
            ItemService         itemService
    ) {
        // Check pre-condition
        assert nonNull(request);
        assert nonNull(ingredientService);
        assert nonNull(itemService);
        assert nonNull(request.getIngredientId());

        // Get ingredient by request ingredient id
        final var ingredient = requireNonNull(ingredientService.get(request.getIngredientId()));

        // Convert item request to item entity
        var item = ItemEntity.from(request);

        // Set information for item entity
        item.setIngredient(ingredient);
        item.setImportId(request.getImportId());

        // Save item entity
        final var savedItem = itemService.save(item);

        // Convert item entity to DTO and then return
        return ok(ItemDto.from(savedItem));
    }

    //</editor-fold>

    //<editor-fold desc="deleteAllItems">

    // TODO: Test this function
    /**
     * Atomic function for delete multiple item entity
     * @param request           ItemDeleteAllRequest
     * @param ingredientService IngredientService
     * @param itemService       ItemService
     * @return                  ResponseEntity
     * @see ResponseEntity
     * @see ItemDeleteAllRequest
     */
    public ResponseEntity<?> deleteAllItems(
            ItemDeleteAllRequest    request,
            IngredientService       ingredientService,
            ItemService             itemService
    ) {
        // Check pre-condition
        assert nonNull(request);
        assert nonNull(itemService);

        // Get deleted list of item based on strategy
        final var ingredient = ingredientService.get(request.getIngredientId());
        final var items = itemService.getAllByIngredient(request.getClientId(), ingredient)
                .stream().filter(TransactionLogic::isItemValid).collect(Collectors.toList());
        final var deleteItems = getAllItemByDeleteStrategy(request.getQuantity(), request.getDeleteStrategy(), items);

        // Delete list of item
        itemService.deleteAll(deleteItems);

        // Return deleted items as DTO
        return ok(ItemDto.from(items));
    }

    /**
     * Get sub list of item based on strategy and quantity
     * @param quantity  Delete quantity
     * @param strategy  DeleteStrategy
     * @param items     List of items
     * @return
     */
    private List<ItemEntity> getAllItemByDeleteStrategy(
            long                quantity,
            DeleteStrategy      strategy,
            List<ItemEntity>    items
    ) {
        // check pre-condition
        assert nonNull(strategy);
        assert nonNull(items);
        assert !items.isEmpty();

        // If quantity is zero or less return empty list
        if(quantity <= 0) return new ArrayList<>();

        // Get sub list of item based on strategy
        switch (strategy) {

            // Descending sort of item expiration
            case ByOldest: items.sort(Comparator.comparing(item -> LocalDate.parse(((ItemEntity)item).getExpiredAt())).reversed()); break;

            // Ascending sort of item expiration
            case ByNewest: items.sort(Comparator.comparing(item -> LocalDate.parse(item.getExpiredAt()))); break;

            // Random shuffle of item
            case ByRandom: Collections.shuffle(items, ThreadLocalRandom.current()); break;

            // If the strategy is not found throw exception
            default: throw new IllegalStateException("Unexpected value: " + strategy);
        }

        return items.subList(0, Math.toIntExact(quantity - 1));
    }

    //</editor-fold>

    //<editor-fold desc="syncIngredientInInventory">

    /**
     * Sync an ingredient quantity nad status to inventory
     * @param clientId          Client ID
     * @param ingredient        IngredientEntity
     * @param inventoryService  InventoryService
     * @param itemService       ItemService
     * @return                  InventoryEntity
     */
    @SneakyThrows
    public InventoryEntity syncIngredientInInventory(
            Long                clientId,
            IngredientEntity    ingredient,
            InventoryService    inventoryService,
            ItemService         itemService
    ){
        // Check pre-condition
        assert nonNull(ingredient);
        assert nonNull(clientId);
        assert nonNull(inventoryService);
        assert nonNull(itemService);

        // Get inventory by ingredient
        var inventory = inventoryService.get(ingredient);

        // Set create inventory if it doesn't exist
        if(isNull(inventory)) inventory = createSyncedInventoryInformation(clientId, ingredient);

        // Otherwise, update inventory update timestamp
        else inventory.setUpdateAt(Instant.now().toString());

        // Set inventory quantity
        inventory.setQuantity((float) countActiveAndUnexpiredIngredientItem(clientId, ingredient, itemService));
        return inventoryService.save(inventory);
    }

    /**
     * Count active and unexpected ingredient item
     * @param clientId      Client ID
     * @param ingredient    IngredientEntity
     * @param itemService   ItemService
     * @return              long
     */
    private long countActiveAndUnexpiredIngredientItem(
            Long                clientId,
            IngredientEntity    ingredient,
            ItemService         itemService
    ) {
        // Check pre-condition
        assert nonNull(clientId);
        assert nonNull(ingredient);
        assert nonNull(itemService);

        // Filter all item satisfy active and not expired and get count
        return itemService.getAllByIngredient(clientId, ingredient).stream().filter(TransactionLogic::isItemValid).count();
    }

    /**
     * Check item is valid (active and unexpired)
     * @param item  ItemEntity
     * @return      boolean
     */
    private boolean isItemValid(ItemEntity item) {
        return !item.isExpired() && item.isActive();
    }

    /**
     * createSyncedInventoryInformation
     * @param clientId      Client id
     * @param ingredient    IngredientEntity
     * @return              InventoryEntity
     */
    private InventoryEntity createSyncedInventoryInformation(
            Long                clientId,
            IngredientEntity    ingredient
    ) {
        // Check pre-condition
        assert nonNull(clientId);
        assert nonNull(ingredient);

        // Set information to inventory entity
        InventoryEntity inventory = InventoryEntityFactory.create(DEFAULT);
        inventory.setIngredient(    ingredient);
        inventory.setClientId(      clientId);
        inventory.setName(          ingredient.getName().concat(" ".concat(ingredient.getCode())));
        inventory.setUnit(          ingredient.getUnit());
        inventory.setUnitType(      ingredient.getUnitType());
        inventory.setDescription(   ingredient.getDescription());
        return inventory;
    }

    //</editor-fold>

    //<editor-fold desc="syncAllIngredientInInventory">

    /**
     * Sync all inventory
     * @param clientId          Client ID
     * @param ingredientService IngredientService
     * @param inventoryService  InventoryService
     * @param itemService       ItemService
     * @return                  List&lt;InventoryEntity&gt;
     */
    public List<InventoryEntity> syncAllIngredientInInventory(
            Long clientId,
            IngredientService ingredientService,
            InventoryService inventoryService,
            ItemService itemService
    ) {
        // Check pre-condition
        assert nonNull(clientId);
        assert nonNull(ingredientService);
        assert nonNull(inventoryService);
        assert nonNull(itemService);

        // Get synced ingredient in inventory as list
        return ingredientService.getAllChild(clientId)
                // Filter all duplicated items
                .stream().distinct()
                // Map to synced ingredient in inventory
                .map(       ingredient -> syncIngredientInInventory(clientId, ingredient, inventoryService, itemService))
                // Convert stream to list
                .collect(   Collectors.toList());
    }

    //</editor-fold>

    //<editor-fold desc="getSimpleIngredientTypeActiveDto">

    /**
     * Get active ingredient type simple
     * @param tenantId          Tenant ID
     * @param ingredientService IngredientService
     * @return                  List&lt;SimpleDto&gt;
     */
    public List<SimpleDto> getSimpleIngredientTypeActiveDto(
            Long                tenantId,
            IngredientService   ingredientService
    ) {
        // Check pre-condition
        assert nonNull(tenantId);
        assert nonNull(ingredientService);

        // Get all active children ingredient as Label-Value DTO list
        return ingredientService.getAllChild(tenantId).stream()
                // Filter to children are active
                .filter(    BaseEntity::isActive)
                // Map to label as ingredient ID and value as ingredient name
                .map(       ingredient -> new SimpleDto(ingredient.getId(), ingredient.getName()))
                // Convert to list
                .collect(   Collectors.toList());
    }

    //</editor-fold>

    //<editor-fold desc="deleteIngredientEntity">

    /**
     * deleteIngredientEntity
     * @param id                Entity ID
     * @param ingredientService IngredientService
     * @return                  ResponseEntity
     */
    public ResponseEntity<?> deleteIngredientEntity(
            Long                id,
            IngredientService   ingredientService
    ) {
        // Check pre-condition
        assert nonNull(id);
        assert nonNull(ingredientService);

        // Get ingredient by ID and delete it
        final var ingredient = requireNonNull(ingredientService.get(id));
        ingredientService.delete(ingredient);
        return ok(ingredient);
    }
    //</editor-fold>

    //<editor-fold desc="deleteItemEntity">

    /**
     * deleteItemEntity
     * @param id            Entity ID
     * @param itemService   ItemService
     * @return              ResponseEntity
     */
    public ResponseEntity<?> deleteItemEntity(
            Long        id,
            ItemService itemService
    ) {
        // Check pre-condition
        assert nonNull(id);
        assert nonNull(itemService);

        // Get item by id and delete that item
        final var item = requireNonNull(itemService.getById(id));
        itemService.delete(item);
        return ok(item);
    }

    //</editor-fold>

    /**
     * Log out Http path with extra information of service
     * @param method        HTTP method
     * @param servicePath   Service path
     * @param subPath       Sub path
     * @return              String
     */
    public String path(
            HttpMethod  method,
            String      servicePath,
            String      subPath,
            String      apiVersion
    ) {
        // HTTP Request methods (GET, POST, PUT, DELETE, ...)
        return  method.name().concat(" ")
                // Get Host API
                .concat(InetAddress.getLoopbackAddress().getCanonicalHostName().trim())
                // Get service path
                .concat(servicePath.trim()).concat(subPath.trim()).concat(" ")
                // Get API version
                .concat(apiVersion.trim()).trim();
    }

}
