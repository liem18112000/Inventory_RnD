package com.fromlabs.inventory.inventoryservice.ingredient;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Ingredient Repository
 */
@Repository
public interface IngredientRepository extends JpaRepository<IngredientEntity, Long>, JpaSpecificationExecutor<IngredientEntity> {
    IngredientEntity findByCode(String code);
    IngredientEntity findByClientIdAndName(Long clientId, String name);
    Page<IngredientEntity> findAllByClientIdAndParentIdIsNull(Long clientId, Pageable pageable);
    List<IngredientEntity> findAllByClientIdAndParentIdIsNull(Long clientId);
    List<IngredientEntity> findAllByClientIdAndParentIdIsNotNull(Long clientId);
    Page<IngredientEntity> findAllByClientIdAndParentId(Long clientId, Long parentId, Pageable pageable);
    List<IngredientEntity> findAllByClientIdAndParentId(Long clientId, Long parentId);
}
