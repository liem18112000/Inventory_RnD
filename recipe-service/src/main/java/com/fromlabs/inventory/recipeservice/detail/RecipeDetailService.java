package com.fromlabs.inventory.recipeservice.detail;

import com.fromlabs.inventory.recipeservice.recipe.RecipeEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public interface RecipeDetailService {
    RecipeDetailEntity get(Long id);
    RecipeDetailEntity get(String code);
    RecipeDetailEntity get(Long clientId, String name);
    Page<RecipeDetailEntity> getPage(Specification<RecipeDetailEntity> specification, Pageable pageable);
    List<RecipeDetailEntity> getAll(Specification<RecipeDetailEntity> specification);
    RecipeDetailEntity save(RecipeDetailEntity entity);
    void delete(RecipeDetailEntity entity);
}
