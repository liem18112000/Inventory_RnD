package com.fromlabs.inventory.recipeservice.recipe;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Recipe entity repository
 * @author Liem
 */
@Repository
public interface RecipeRepository extends JpaRepository<RecipeEntity, Long>, JpaSpecificationExecutor<RecipeEntity> {

    RecipeEntity findByCode(String code);

    RecipeEntity findByClientIdAndName(Long clientId, String name);

    List<RecipeEntity> findAllByClientIdAndParentIdIsNull(Long clientId);

    List<RecipeEntity> findAllByClientIdAndParentId(Long clientId, Long parentId);

}
