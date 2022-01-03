package com.fromlabs.inventory.inventoryservice.item;

import com.fromlabs.inventory.inventoryservice.ingredient.IngredientEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.List;

/**
 * Service Implementation of ItemEntity
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class ItemServiceImpl implements ItemService {

    private final ItemRepository repository;

    /**
     * Constructor
     * @param repository ItemRepository
     */
    public ItemServiceImpl(ItemRepository repository) {
        this.repository = repository;
    }

    /**
     * Get item by id
     * @param id Long
     * @return ItemEntity
     * @see ItemEntity
     */
    public ItemEntity getById(@NotNull final Long id) {
        return this.repository.findById(id).orElse(null);
    }

    /**
     * Get item by code
     * @param clientId  Long
     * @param code      String of code
     * @return ItemEntity
     * @see ItemEntity
     */
    public ItemEntity getByCode(@NotNull final Long clientId, @NotNull final String code) {
        return this.repository.findByClientIdAndCode(clientId, code);
    }

    /**
     * Get item by name
     * @param clientId  Long
     * @param name      String of code
     * @return          List&lt;ItemEntity&gt;
     */
    public List<ItemEntity> getByName(@NotNull final Long clientId, @NotNull final String name) {
        return this.repository.findByClientIdAndName(clientId, name);
    }

    /**
     * Get all items as list
     * @param clientId  Long
     * @return List&lt;ItemEntity&gt;
     */
    public List<ItemEntity> getAll(@NotNull final Long clientId) {
        return this.repository.findAllByClientId(clientId);
    }

    /**
     * Get all items with pagination
     * @param clientId  Long
     * @param pageable Pageable
     * @return Page&lt;ItemEntity&gt;
     * @see ItemEntity
     */
    public Page<ItemEntity> getPage(@NotNull final Long clientId, @NotNull final Pageable pageable) {
        return this.repository.findAllByClientId(clientId, pageable);
    }

    /**
     * Save item entity
     * @param entity Saved entity
     * @return ItemEntity
     * @see ItemEntity
     */
    public ItemEntity save(@NotNull final ItemEntity entity) {
        return this.repository.save(entity);
    }

    /**
     * Delete item entity
     * @param entity Deleted entity
     * @see ItemEntity
     */
    public void delete(@NotNull final ItemEntity entity) {
        this.repository.delete(entity);
    }

    /**
     * Delete multiple items
     * @param entities Collection of deleted items
     * @see Collection
     * @see ItemEntity
     */
    public void deleteAll(@NotNull final Collection<ItemEntity> entities) {
        this.repository.deleteAll(entities);
    }

    /**
     * Ger all item with pagination
     * @param specification Specification
     * @param pageable Pageable
     * @return Page&lt;ItemEntity&gt;
     * @see ItemEntity
     * @see Specification
     * @see Pageable
     */
    public Page<ItemEntity> getPage(
            @NotNull final Specification<ItemEntity> specification,
            @NotNull final Pageable pageable
    ) {
        return this.repository.findAll(specification, pageable);
    }

    /**
     * Delete multiple items
     * @param ids Collection of id of deleted items
     * @see Collection
     * @see ItemEntity
     */
    public void deleteAllByIds(
            @NotNull final Collection<Long> ids
    ) {
        this.repository.deleteAllById(ids);
    }

    /**
     * Get all item entity by client ID and ingredient
     * @param clientId  Long
     * @param ingredient    IngredientEntity
     * @return List&lt;ItemEntity&gt;
     * @see IngredientEntity
     * @see ItemEntity
     */
    public List<ItemEntity> getAllByIngredient(
            @NotNull final Long clientId,
            @NotNull final IngredientEntity ingredient
    ) {
        return this.repository.findAllByClientIdAndIngredient(clientId, ingredient);
    }

    /**
     * Save all items
     * @param items Items
     * @return Items
     */
    public List<ItemEntity> save(
            @NotNull final List<ItemEntity> items
    ) {
        return this.repository.saveAll(items);
    }

    /**
     * Get all item with filter
     * @param specification Specification
     * @return List of item
     */
    public List<ItemEntity> getAll(
            @NotNull final Specification<ItemEntity> specification
    ) {
        return this.repository.findAll(specification);
    }
}
