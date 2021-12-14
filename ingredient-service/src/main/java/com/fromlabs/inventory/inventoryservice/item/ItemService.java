package com.fromlabs.inventory.inventoryservice.item;

import com.fromlabs.inventory.inventoryservice.ingredient.IngredientEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.Collection;
import java.util.List;

/**
 * Service interface of ItemEntity
 */
public interface ItemService {
    /**
     * Get item by id
     * @param id Long
     * @return ItemEntity
     * @see ItemEntity
     */
    ItemEntity getById(Long id);

    /**
     * Get item by code
     * @param clientId  Long
     * @param code      String of code
     * @return ItemEntity
     * @see ItemEntity
     */
    ItemEntity getByCode(Long clientId, String code);

    /**
     * Get item by name
     * @param clientId  Long
     * @param name      String of name
     * @return          List&lt;ItemEntity&gt;
     */
    List<ItemEntity> getByName(Long clientId, String name);

    /**
     * Get all item as list
     * @param clientId Long
     * @return List&lt;ItemEntity&gt;
     * @see ItemEntity
     */
    List<ItemEntity> getAll(Long clientId);

    /**
     * Get all item with pagination
     * @param clientId  Long
     * @param pageable  Pageable
     * @return Page&lt;ItemEntity&gt;
     * @see ItemEntity
     */
    Page<ItemEntity> getPage(Long clientId, Pageable pageable);

    /**
     * Save item entity
     * @param entity Saved entity
     * @return ItemEntity
     * @see ItemEntity
     */
    ItemEntity save(ItemEntity entity);

    /**
     * Delete item entity
     * @param entity Deleted entity
     * @see ItemEntity
     */
    void delete(ItemEntity entity);

    /**
     * Delete multiple item
     * @param entities Collection of deleted items
     * @see Collection
     * @see ItemEntity
     */
    void deleteAll(Collection<ItemEntity> entities);

    /**
     * Ger all item with pagination
     * @param specification Specification
     * @param pageable Pageable
     * @return Page<ItemEntity>
     * @see ItemEntity
     * @see Specification
     * @see Pageable
     */
    Page<ItemEntity> getPage(Specification<ItemEntity> specification, Pageable pageable);

    /**
     * Delete multiple items
     * @param ids Collection of id of deleted items
     * @see Collection
     * @see ItemEntity
     */
    void deleteAllByIds(Collection<Long> ids);

    /**
     * Get all item entity by client ID and ingredient
     * @param clientId  Long
     * @param ingredient    IngredientEntity
     * @return List&lt;ItemEntity&gt;
     * @see IngredientEntity
     * @see ItemEntity
     */
    List<ItemEntity> getAllByIngredient(Long clientId, IngredientEntity ingredient);
}
