/*
 * Copyright (c) 2021. Liem Doan
 */

package com.fromlabs.inventory.inventoryservice.utility;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fromlabs.inventory.inventoryservice.client.supplier.SupplierClient;
import com.fromlabs.inventory.inventoryservice.client.supplier.beans.ImportDetailRequest;
import com.fromlabs.inventory.inventoryservice.common.dto.SimpleDto;
import com.fromlabs.inventory.inventoryservice.common.entity.BaseEntity;
import com.fromlabs.inventory.inventoryservice.common.transaction.*;
import com.fromlabs.inventory.inventoryservice.ingredient.*;
import com.fromlabs.inventory.inventoryservice.ingredient.beans.dto.IngredientDto;
import com.fromlabs.inventory.inventoryservice.ingredient.beans.request.IngredientPageRequest;
import com.fromlabs.inventory.inventoryservice.ingredient.beans.request.IngredientRequest;
import com.fromlabs.inventory.inventoryservice.ingredient.beans.response.SaveIngredientResponse;
import com.fromlabs.inventory.inventoryservice.ingredient.config.IngredientConfigEntity;
import com.fromlabs.inventory.inventoryservice.ingredient.config.mapper.IngredientConfigMapper;
import com.fromlabs.inventory.inventoryservice.ingredient.event.*;
import com.fromlabs.inventory.inventoryservice.ingredient.event.beans.enums.IngredientEvent;
import com.fromlabs.inventory.inventoryservice.ingredient.event.beans.dto.IngredientEventDto;
import com.fromlabs.inventory.inventoryservice.ingredient.event.mapper.IngredientEventMapper;
import com.fromlabs.inventory.inventoryservice.ingredient.event.status.IngredientEventStatus;
import com.fromlabs.inventory.inventoryservice.ingredient.specification.IngredientSpecification;
import com.fromlabs.inventory.inventoryservice.ingredient.track.*;
import com.fromlabs.inventory.inventoryservice.ingredient.track.beans.request.IngredientHistoryPageRequest;
import com.fromlabs.inventory.inventoryservice.ingredient.track.mapper.IngredientHistoryMapper;
import com.fromlabs.inventory.inventoryservice.ingredient.track.specification.IngredientHistorySpecification;
import com.fromlabs.inventory.inventoryservice.inventory.*;
import com.fromlabs.inventory.inventoryservice.inventory.beans.dto.InventoryDto;
import com.fromlabs.inventory.inventoryservice.inventory.beans.request.InventoryPageRequest;
import com.fromlabs.inventory.inventoryservice.inventory.factory.InventoryEntityFactory;
import com.fromlabs.inventory.inventoryservice.inventory.mapper.InventoryMapper;
import com.fromlabs.inventory.inventoryservice.item.*;
import com.fromlabs.inventory.inventoryservice.item.beans.dto.ItemDto;
import com.fromlabs.inventory.inventoryservice.item.beans.request.BatchItemsRequest;
import com.fromlabs.inventory.inventoryservice.item.beans.request.ItemDeleteAllRequest;
import com.fromlabs.inventory.inventoryservice.item.beans.request.ItemPageRequest;
import com.fromlabs.inventory.inventoryservice.item.beans.request.ItemRequest;
import com.fromlabs.inventory.inventoryservice.item.mapper.BatchItemsMapper;
import com.fromlabs.inventory.inventoryservice.item.mapper.ItemMapper;
import com.fromlabs.inventory.inventoryservice.item.specification.ItemSpecification;
import com.fromlabs.inventory.inventoryservice.item.strategy.DeleteStrategy;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import javax.validation.constraints.NotNull;
import java.net.InetAddress;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import static com.fromlabs.inventory.inventoryservice.common.factory.FactoryCreateType.*;
import static com.fromlabs.inventory.inventoryservice.ingredient.mapper.IngredientMapper.*;
import static com.fromlabs.inventory.inventoryservice.inventory.specification.InventorySpecification.filter;
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

    //<editor-fold desc="getLabelValueStatus">

    // TODO: Using status service
    /**
     * Get all status as label-value list
     * @return ResponseEntity
     */
    public ResponseEntity<?> getLabelValueStatus() {
        return ok(Arrays.stream(IngredientEventStatus.values())
                .map(status -> new SimpleDto(status.getName().toLowerCase(), status.getName())));
    }

    //</editor-fold>

    //<editor-fold desc="getLabelValueEvent">

    /**
     * Get all event as label-value list
     * @param clientId      Client ID
     * @param eventService  IngredientEventService
     * @return ResponseEntity
     */
    public ResponseEntity<?> getLabelValueEvent(
            @NotNull final Long clientId,
            @NotNull final IngredientEventService eventService
    ) {
        return  ok(eventService.getAll(clientId).stream()
                .map(event -> getEventSimpleDto(event, "_", " ")));
    }

    /**
     * Process event to simple DTO as label-value
     * @param event IngredientEventDto
     * @return SimpleDto
     */
    private SimpleDto getEventSimpleDto(
            @NotNull final IngredientEventDto event,
            @NotNull final String delimiter,
            @NotNull final String replacement
    ) {
        var dto = new SimpleDto();
        final var eventName = event.getName().replaceAll(delimiter, replacement);
        if(eventName.length() <= 1){
            dto.setLabel(eventName.toUpperCase());
        } else {
            final var firstCharacter = eventName.substring(0, 1);
            final var restCharacter = eventName.substring(firstCharacter.length());
            final var label = firstCharacter.toUpperCase().concat(restCharacter);
            dto.setLabel(label);
        }
        dto.setValue(event.getName());
        return dto;
    }

    //</editor-fold>

    //<editor-fold desc="getIngredientPageWithFilter">

    /**
     * getIngredientPageWithFilter
     * @param request           IngredientPageRequest
     * @param ingredientService IngredientService
     * @param inventoryService  InventoryService
     * @return Page&lt;IngredientDto&gt;
     */
    public Page<IngredientDto> getIngredientPageWithFilter(
            @NotNull final IngredientPageRequest request,
            @NotNull final IngredientService       ingredientService,
            @NotNull final InventoryService        inventoryService
    ) {
        log.info("getIngredientPageWithFilter : start");

        // Get page of Ingredient Entity
        var page = getIngredientDtoPage(request, ingredientService);

        // If request parent id is null or there is no ingredient with the ke key return page
        if (isNull(request.getParentId()) || isNull(ingredientService.getById(request.getParentId()))) return page;

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
            @NotNull final InventoryService    inventoryService,
            @NotNull final IngredientDto       ingredient
    ) {
        log.info("setIngredientQuantity");

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
            @NotNull final InventoryService inventoryService,
            @NotNull final IngredientDto ingredient
    ) {
        log.info("getIngredientPageWithFilter : getInventoryEntity");

        // Convert ingredient DTO to entity
        final var entity = toEntity(ingredient);

        // Get inventory from ingredient and then return
        return inventoryService.getByIngredient(entity);
    }

    /**
     * getIngredientDtoPage
     * @param request           IngredientPageRequest
     * @param ingredientService IngredientService
     * @return Page&lt;IngredientDto&gt;
     */
    private Page<IngredientDto> getIngredientDtoPage(
            @NotNull final IngredientPageRequest   request,
            @NotNull final IngredientService       ingredientService
    ) {
        log.info("getIngredientPageWithFilter : getIngredientDtoPage");

        // Get ingredient entity page
        final var page = getIngredientEntityPage(request, ingredientService);

        // Convert ingredient entity page to ingredient DTO page
        return toDto(page);
    }

    /**
     * getIngredientEntityPage
     * @param request           IngredientPageRequest
     * @param ingredientService IngredientService
     * @return Page&lt;IngredientEntity&gt;
     */
    private Page<IngredientEntity> getIngredientEntityPage(
            @NotNull final IngredientPageRequest request,
            @NotNull final IngredientService     ingredientService
    ) {
        log.info("getIngredientPageWithFilter : getIngredientEntityPage");

        // Check pre-condition
        assert nonNull(request.getParentId());
        assert nonNull(request.getPageable());

        // Get ingredient entity and parent
        final var entity = toEntity(request);
        final var parent = ingredientService.getById(request.getParentId());

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
            @NotNull final Long                id,
            @NotNull final IngredientService   ingredientService,
            @NotNull final InventoryService    inventoryService
    ) {
        log.info("getIngredientByIdWithQuantity");

        // Gwt ingredient by id
        final var ingredient = ingredientService.getById(id);

        // Convert to ingredient entity to DTO
        var dto = toDto(ingredient);
        if (Objects.isNull(dto)) {
            return null;
        }

        // If ingredient is child set ingredient quantity
        if (nonNull(ingredient.getParent())) {
            setIngredientQuantity(inventoryService, dto);
        }

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
            @NotNull final String              code,
            @NotNull final IngredientService   ingredientService,
            @NotNull final InventoryService    inventoryService
    ) {
        log.info("getIngredientByCodeWithQuantity");

        // Get ingredient by code
        final var ingredient = ingredientService.getByCode(code);

        // Convert ingredient entity to DTO
        var dto = toDto(ingredient);
        if (Objects.isNull(dto)) {
            return null;
        }

        // If ingredient is child set ingredient quantity
        if(nonNull(ingredient.getParent())) {
            setIngredientQuantity(inventoryService, dto);
        }

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
            @NotNull final InventoryPageRequest request,
            @NotNull final IngredientService       ingredientService,
            @NotNull final InventoryService        inventoryService
    ) {
        log.info("getInventoryPageWithFilter : start");

        // Get inventory entity page
        final var inventoryPage = requireNonNull(getInventoryEntityPage(request, ingredientService, inventoryService));

        // Convert inventory entity page to inventory DTO page and then return
        return InventoryMapper.toDto(inventoryPage);
    }

    /**
     * Get all inventory with pagination
     * @param request           InventoryPageRequest
     * @param ingredientService IngredientService
     * @param inventoryService  InventoryService
     * @return Page&lt;InventoryEntity&gt;
     */
    private Page<InventoryEntity> getInventoryEntityPage(
            @NotNull final InventoryPageRequest    request,
            @NotNull final IngredientService       ingredientService,
            @NotNull final InventoryService        inventoryService
    ) {
        log.info("getInventoryPageWithFilter : getInventoryEntityPage");

        // Check pre-condition
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
            @NotNull final InventoryPageRequest request,
            @NotNull final IngredientService    ingredientService
    ) {
        log.info("getInventoryPageWithFilter : getInventoryEntitySpecification");
        return filter(InventoryMapper.toEntity(request), getIngredientEntity(request, ingredientService));
    }

    /**
     * getIngredientEntity
     * @param request           InventoryPageRequest
     * @param ingredientService IngredientService
     * @return  IngredientEntity
     */
    private IngredientEntity getIngredientEntity(
            @NotNull final InventoryPageRequest request,
            @NotNull final IngredientService    ingredientService
    ) {
        log.info("getInventoryPageWithFilter : getIngredientEntity");

        // If ingredient id from request is null return null
        // Otherwise, return the ingredient with provided id from request
        return isNull(request.getIngredientId()) ? null : ingredientService.getById(request.getIngredientId());
    }

    //</editor-fold>

    //<editor-fold desc="getItemPageWithFiler">

    /**
     * Atomic method for getting all item with pagination and filter
     * @param request ItemPageRequest
     * @return Page&lt;ItemDto&gt;
     */
    public Page<ItemDto> getItemPageWithFiler(
            @NotNull final ItemPageRequest request,
            @NotNull final IngredientService   ingredientService,
            @NotNull final ItemService         itemService,
            @NotNull final SupplierClient supplierClient
    ) {
        log.info("getItemPageWithFiler : start");
        return ItemMapper.toDto(getItemEntityPage(request, ingredientService, itemService), supplierClient);
    }

    /**
     * getItemEntityPage
     * @param request           ItemPageRequest
     * @param ingredientService IngredientService
     * @param itemService       ItemService
     * @return Page&lt;ItemEntity&gt;
     */
    private Page<ItemEntity> getItemEntityPage(
            @NotNull final ItemPageRequest     request,
            @NotNull final IngredientService   ingredientService,
            @NotNull final ItemService         itemService
    ) {
        log.info("getItemPageWithFiler : getItemEntityPage");

        // Check pre-condition
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
            @NotNull final ItemPageRequest     request,
            @NotNull final IngredientService   ingredientService
    ) {
        log.info("getItemPageWithFiler : getItemEntitySpecification");


        // Get item value and ingredient from request
        final var item = ItemMapper.toEntity(request);
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
            @NotNull final ItemPageRequest     request,
            @NotNull final IngredientService   ingredientService
    ) {
        log.info("getItemPageWithFiler : getIngredientEntity");

        // Check pre-condition
        assert nonNull(request.getIngredientId());

        return ingredientService.getById(request.getIngredientId());
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
            IngredientRequest request,
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
     * @param config            AtomicReference&lt;IngredientConfigEntity&gt;
     * @return                  SaveIngredientResponse
     */
    public SaveIngredientResponse saveConfigTransactionProcess(
            @NotNull final IngredientRequest                       request,
            @NotNull final IngredientService                       ingredientService,
            @NotNull AtomicReference<IngredientEntity>       ingredient,
            @NotNull AtomicReference<IngredientConfigEntity> config
    ) {

        // Get ingredient entity from atomic reference
        final var ingredientEntity = ingredient.get();
        assert nonNull(ingredientEntity);

        // Save ingredient config entity if ingredient has a parent
        final var parentId = request.getParentId();
        if(nonNull(parentId) && parentId > 0){
            final var savedConfig = ingredientService
                    .saveConfig(getIngredientConfigEntity(request, ingredientEntity));
            config.set(requireNonNull(savedConfig));
        }

        // Convert ingredient entity to DTO get config entity
        final var ingredientDto = toDto(ingredientEntity);
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
            @NotNull final IngredientService            ingredientService,
            @NotNull AtomicReference<IngredientEntity>  ingredient
    ) {
        log.info("Rollback saveIngredientTransaction");

        // Check pre-condition
        assert nonNull(ingredient.get());

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
            @NotNull final IngredientRequest            request,
            @NotNull final IngredientService            ingredientService,
            @NotNull AtomicReference<IngredientEntity>  ingredient
    ) {
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
            @NotNull final IngredientRequest request,
            @NotNull final IngredientEntity  ingredient
    ) {
        log.info("saveIngredientAndConfig: getIngredientConfigEntity");

        // Convert config request to entity
        return IngredientConfigMapper.toEntity(request, ingredient);
    }

    /**
     * getIngredientEntity
     * @param request           IngredientRequest
     * @param ingredientService IngredientService
     * @return                  IngredientEntity
     */
    private IngredientEntity getIngredientEntity(
            @NotNull final IngredientRequest request,
            @NotNull final IngredientService ingredientService
    ) {
        log.info("saveIngredientAndConfig: getIngredientEntity");

        // Convert request to ingredient entity
        var ingredient = toEntity(request);
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
            @NotNull final IngredientRequest request,
            @NotNull final IngredientService ingredientService
    ) {
        log.info("saveIngredientAndConfig: getIngredientParent");

        // if parent id is null return null
        if(isNull(request.getParentId())) return null;

        // Otherwise, get parent ingredient by provide id
        return ingredientService.getById(request.getParentId());
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
            @NotNull final ItemRequest request,
            @NotNull final IngredientService   ingredientService,
            @NotNull final ItemService         itemService,
            @NotNull final SupplierClient supplierClient
    ) {
        // Check pre-condition
        assert nonNull(request.getIngredientId());

        // Get ingredient by request ingredient id
        final var ingredient = requireNonNull(ingredientService.getById(request.getIngredientId()));

        // Convert item request to item entity
        var item = ItemMapper.toEntity(request);

        // Add item to import detail if saving
        if (Objects.isNull(item.getId())) {
            final var addItemRequest = ImportDetailRequest.builder()
                    .clientId(request.getClientId())
                    .name("Import_".concat(LocalDateTime.now().toString()))
                    .description("Item is added to import")
                    .importId(request.getImportId())
                    .ingredientId(request.getIngredientId())
                    .build();
            final var detail = supplierClient.onAddNewItem(addItemRequest);
            if (Objects.isNull(detail)) {
                log.error("saveOrUpdateItem: add item to import detail failed: {}", addItemRequest);
                throw new IllegalStateException("saveOrUpdateItem: add item to import detail failed");
            }
        }

        // Set information for item entity
        item.setIngredient(ingredient);
        item.setImportId(request.getImportId());

        // Save item entity
        final var savedItem = itemService.save(item);

        // Convert item entity to DTO and then return
        return ok(ItemMapper.toDto(savedItem, supplierClient));
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
            @NotNull final ItemDeleteAllRequest    request,
            @NotNull final IngredientService       ingredientService,
            @NotNull final ItemService             itemService,
            @NotNull final SupplierClient          supplierClient
    ) {
        // Get deleted list of item based on strategy
        final var ingredient = ingredientService.getById(request.getIngredientId());
        final var items = itemService.getAllByIngredient(request.getClientId(), ingredient)
                .stream().filter(TransactionLogic::isItemValid).collect(Collectors.toList());
        final var deleteItems = getAllItemByDeleteStrategy(request.getQuantity(),
                request.getDeleteStrategy(), items);

        // Delete list of item
        itemService.deleteAll(deleteItems);

        // Return deleted items as DTO
        return ok(ItemMapper.toDto(items, supplierClient));
    }

    /**
     * Get sub list of item based on strategy and quantity
     * @param quantity  Delete quantity
     * @param strategy  DeleteStrategy
     * @param items     List of items
     * @return          List of deleted items
     */
    private List<ItemEntity> getAllItemByDeleteStrategy(
            final long                      quantity,
            @NotNull final DeleteStrategy   strategy,
            @NotNull final List<ItemEntity> items
    ) {
        // check pre-condition
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
            @NotNull final Long                clientId,
            @NotNull final IngredientEntity    ingredient,
            @NotNull final InventoryService    inventoryService,
            @NotNull final ItemService         itemService
    ){
        // Get inventory by ingredient
        var inventory = inventoryService.getByIngredient(ingredient);

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
            @NotNull final Long                clientId,
            @NotNull final IngredientEntity    ingredient,
            @NotNull final ItemService         itemService
    ) {
        // Filter all item satisfy active and not expired and get count
        return itemService.getAllByIngredient(clientId, ingredient).stream()
                .filter(TransactionLogic::isItemValid).count();
    }

    /**
     * Check item is valid (active and unexpired)
     * @param item  ItemEntity
     * @return      boolean
     */
    private boolean isItemValid(
            @NotNull final ItemEntity item
    ) {
        return !item.isExpired() && item.isActive();
    }

    /**
     * createSyncedInventoryInformation
     * @param clientId      Client id
     * @param ingredient    IngredientEntity
     * @return              InventoryEntity
     */
    private InventoryEntity createSyncedInventoryInformation(
            @NotNull final Long                clientId,
            @NotNull final IngredientEntity    ingredient
    ) {
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
            @NotNull final Long                 clientId,
            @NotNull final IngredientService    ingredientService,
            @NotNull final InventoryService     inventoryService,
            @NotNull final ItemService          itemService
    ) {
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
            @NotNull final Long                tenantId,
            @NotNull final IngredientService   ingredientService
    ) {
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

    //<editor-fold desc="getPageIngredientHistory">

    /**
     * Get page ingredient history
     * @param request           IngredientHistoryPageRequest
     * @param ingredientService IngredientService
     * @param historyService    IngredientHistoryService
     * @param eventService      IngredientEventService
     * @return                  ResponseEntity
     */
    public ResponseEntity<?> getPageIngredientHistory(
            @NotNull final IngredientHistoryPageRequest request,
            @NotNull final IngredientService            ingredientService,
            @NotNull final IngredientHistoryService     historyService,
            @NotNull final IngredientEventService       eventService
    ) {
        final var ingredient = ingredientService.getById(request.getIngredientId());
        final var event = eventService.getByName(request.getClientId(), request.getEvent());
        final var specification = IngredientHistorySpecification.filter(request, ingredient,
                isNull(event) ?  null : IngredientEventMapper.toEntity(event));
        final var page = historyService.getPage(specification, request.getPageable());
        return ok(IngredientHistoryMapper.toDto(page, eventService));
    }

    //</editor-fold>

    //<editor-fold desc="getHistoryByIngredient">

    /**
     * Get all history
     * @param tenantId          Tenant ID
     * @param ingredientId      Ingredient Unique ID (optional)
     * @param ingredientService IngredientService
     * @param historyService    IngredientHistoryService
     * @param eventService      IngredientEventService
     * @return                  ResponseEntity
     */
    public ResponseEntity<?> getAllHistory(
            @NotNull final Long tenantId,
            @NotNull final Long ingredientId,
            @NotNull final IngredientService ingredientService,
            @NotNull final IngredientHistoryService historyService,
            @NotNull final IngredientEventService eventService
    ) {
        final var histories = isNull(ingredientId) ? historyService.getAll(tenantId) :
                historyService.getByIngredient(tenantId, ingredientService.getById(ingredientId));
        final var historyDto = IngredientHistoryMapper.toDto(histories, eventService);
        return ok(historyDto);
    }

    //</editor-fold>

    //<editor-fold desc="getAllHistory">

    /**
     * Get all history by ingredient
     * @param tenantId          Tenant ID
     * @param historyService    IngredientHistoryService
     * @param eventService      IngredientEventService
     * @return                  ResponseEntity
     */
    public ResponseEntity<?> getAllHistory(
            @NotNull final Long tenantId,
            @NotNull final IngredientHistoryService historyService,
            @NotNull final IngredientEventService   eventService
    ) {
        final var histories = historyService.getAll(tenantId);
        final var historyDto = IngredientHistoryMapper.toDto(histories, eventService);
        return ok(historyDto);
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
            @NotNull final Long                id,
            @NotNull final IngredientService   ingredientService
    ) {
        // Get ingredient by ID and delete it
        final var ingredient = requireNonNull(ingredientService.getById(id));
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
            @NotNull final Long        id,
            @NotNull final ItemService itemService
    ) {
        // Get item by id and delete that item
        final var item = requireNonNull(itemService.getById(id));
        itemService.delete(item);
        return ok(item);
    }

    //</editor-fold>

    //<editor-fold desc="Save item in batch">

    // TODO: Implement saving batch item with transaction
    /**
     * Save item in batch
     * @param request           BatchItemsRequest
     * @param ingredientService IngredientService
     * @param itemService       ItemService
     * @return                  ResponseEntity
     */
    public ResponseEntity<?> saveItems(
            @NotNull BatchItemsRequest request,
            @NotNull final IngredientService ingredientService,
            @NotNull final ItemService       itemService,
            @NotNull final SupplierClient supplierClient
    ) {
        final var items = BatchItemsMapper.toEntity(request, ingredientService);
        final var addItemRequest = ImportDetailRequest.builder()
                .clientId(request.getClientId())
                .name("ImportBatch_".concat(LocalDateTime.now().toString()))
                .description("Batch of item is added to import")
                .importId(request.getImportId())
                .ingredientId(request.getIngredientId())
                .quantity(items.size())
                .build();
        final var detail = supplierClient.onAddNewItem(addItemRequest);
        if (Objects.isNull(detail)) {
            log.error("saveItems: add batch of item to import detail failed: {}", addItemRequest);
            throw new IllegalStateException("saveItems: add batch of item to import detail failed");
        }
        final var savedItems = itemService.save(items);
        final var itemDTOs = ItemMapper.toDto(savedItems, supplierClient);
        request.setCodes(itemDTOs.stream().map(ItemDto::getCode).collect(Collectors.toSet()));
        return ok().body(itemDTOs);
    }

    //</editor-fold>

    //<editor-fold desc="Track event">

    /**
     * Track event for ingredient
     * @param request           ItemRequest
     * @param eventName         IngredientEvent
     * @param eventStatus       IngredientEventStatus
     * @param ingredientService IngredientService
     * @param historyService    IngredientHistoryService
     */
    public void trackEvent(
            @NotNull final ItemRequest request,
            @NotNull final IngredientEvent eventName,
            @NotNull final IngredientEventStatus eventStatus,
            @NotNull final IngredientService ingredientService,
            @NotNull final IngredientHistoryService historyService,
            @NotNull final IngredientEventService eventService
    ) {
        final var event = IngredientEventMapper.toEntity(
                eventService.getByName(request.getClientId(), eventName.getEvent()));
        final var history = IngredientHistoryMapper.toEntity(
                request, ingredientService, event, eventStatus);
        saveTrackHistory(history, historyService);
    }

    /**
     * Track event for ingredient
     * @param request           IngredientRequest
     * @param eventName         IngredientEvent
     * @param eventStatus       IngredientEventStatus
     * @param ingredientService IngredientService
     * @param historyService    IngredientHistoryService
     */
    public void trackEvent(
            @NotNull final IngredientRequest request,
            @NotNull final IngredientEvent eventName,
            @NotNull final IngredientEventStatus eventStatus,
            @NotNull final IngredientService ingredientService,
            @NotNull final IngredientHistoryService historyService,
            @NotNull final IngredientEventService eventService
    ) {
        final var event = IngredientEventMapper.toEntity(
                eventService.getByName(request.getClientId(), eventName.getEvent()));
        final var history = IngredientHistoryMapper.toEntity(
                request, ingredientService, event, eventStatus);
        saveTrackHistory(history, historyService);
    }

    /**
     * Save tracked history and log
     * @param history           IngredientHistoryEntity
     * @param historyService    IngredientHistoryService
     */
    private void saveTrackHistory(
            @NotNull final IngredientHistoryEntity history,
            @NotNull final IngredientHistoryService historyService
    ) {
        final var trackedHistory = historyService.save(history);
        try {
            log.info("History track : {}", new ObjectMapper().writeValueAsString(trackedHistory));
        } catch (JsonProcessingException e) {
            log.warn("History log is not available");
        }
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
