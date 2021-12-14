package com.fromlabs.inventory.inventoryservice.item;

import com.fromlabs.inventory.inventoryservice.ingredient.IngredientEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<ItemEntity, Long>, JpaSpecificationExecutor<ItemEntity> {
    Page<ItemEntity> findAll(Specification<ItemEntity> specification, Pageable pageable);
    ItemEntity findByClientIdAndCode(Long clientId, String code);
    List<ItemEntity> findByClientIdAndName(Long clientId, String Name);
    List<ItemEntity> findAllByClientId(Long clientId);
    Page<ItemEntity> findAllByClientId(Long clientId, Pageable pageable);
    List<ItemEntity> findAllByClientIdAndIngredient(Long clientId, IngredientEntity ingredient);
}
