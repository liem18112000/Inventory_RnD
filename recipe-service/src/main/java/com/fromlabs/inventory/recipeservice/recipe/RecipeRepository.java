package com.fromlabs.inventory.recipeservice.recipe;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecipeRepository extends JpaRepository<RecipeEntity, Long>, JpaSpecificationExecutor<RecipeEntity> {
    RecipeEntity findByCode(String code);
    RecipeEntity findByClientIdAndName(Long clientId, String name);
    Page<RecipeEntity> findAllByClientIdAndParentIdIsNull(Long clientId, Pageable pageable);
    List<RecipeEntity> findAllByClientIdAndParentIdIsNull(Long clientId);
    List<RecipeEntity> findAllByClientIdAndParentIdIsNotNull(Long clientId);
    Page<RecipeEntity> findAllByClientIdAndParentId(Long clientId, Long parentId, Pageable pageable);
    List<RecipeEntity> findAllByClientIdAndParentId(Long clientId, Long parentId);
}
