package com.fromlabs.inventory.inventoryservice.item;

import com.fromlabs.inventory.inventoryservice.ingredient.IngredientEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public ItemEntity getById(Long id) {
        return this.repository.findById(id).orElse(null);
    }

    /**
     * Get item by code
     * @param clientId  Long
     * @param code      String of code
     * @return ItemEntity
     * @see ItemEntity
     */
    public ItemEntity getByCode(Long clientId, String code) {
        return this.repository.findByClientIdAndCode(clientId, code);
    }

    /**
     * Get item by name
     * @param clientId  Long
     * @param name      String of code
     * @return          List&lt;ItemEntity&gt;
     */
    public List<ItemEntity> getByName(Long clientId, String name) {
        return this.repository.findByClientIdAndName(clientId, name);
    }

    /**
     * Get all items as list
     * @param clientId  Long
     * @return List&lt;ItemEntity&gt;
     */
    public List<ItemEntity> getAll(Long clientId) {
        return this.repository.findAllByClientId(clientId);
    }

    /**
     * Get all items with pagination
     * @param clientId  Long
     * @param pageable Pageable
     * @return Page&lt;ItemEntity&gt;
     * @see ItemEntity
     */
    public Page<ItemEntity> getPage(Long clientId, Pageable pageable) {
        return this.repository.findAllByClientId(clientId, pageable);
    }

    /**
     * Save item entity
     * @param entity Saved entity
     * @return ItemEntity
     * @see ItemEntity
     */
    public ItemEntity save(ItemEntity entity) {
        return this.repository.save(entity);
    }

    /**
     * Delete item entity
     * @param entity Deleted entity
     * @see ItemEntity
     */
    public void delete(ItemEntity entity) {
        this.repository.delete(entity);
    }

    /**
     * Delete multiple items
     * @param entities Collection of deleted items
     * @see Collection
     * @see ItemEntity
     */
    public void deleteAll(Collection<ItemEntity> entities) {
        this.repository.deleteAll(entities);
    }

    /**
     * Ger all item with pagination
     * @param specification Specification
     * @param pageable Pageable
     * @return Page<ItemEntity>
     * @see ItemEntity
     * @see Specification
     * @see Pageable
     */
    public Page<ItemEntity> getPage(Specification<ItemEntity> specification, Pageable pageable) {
        return this.repository.findAll(specification, pageable);
    }

    /**
     * Delete multiple items
     * @param ids Collection of id of deleted items
     * @see Collection
     * @see ItemEntity
     */
    public void deleteAllByIds(Collection<Long> ids) {
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
    public List<ItemEntity> getAllByIngredient(Long clientId, IngredientEntity ingredient) {
        return this.repository.findAllByClientIdAndIngredient(clientId, ingredient);
    }
}
