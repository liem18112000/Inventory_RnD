package com.fromlabs.inventory.recipeservice.recipe;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public interface RecipeService {
    RecipeEntity get(Long id);
    RecipeEntity get(String code);
    RecipeEntity get(Long clientId, String name);
    Page<RecipeEntity> getPage(Long clientId, Pageable pageable);
    List<RecipeEntity> getAll(Long clientId);
    Page<RecipeEntity> getPage(Long clientId, Long parentId, Pageable pageable);
    List<RecipeEntity> getAll(Long clientId, Long parentId);
    Page<RecipeEntity> getPage(Specification<RecipeEntity> specification, Pageable pageable);
    List<RecipeEntity> getAll(Specification<RecipeEntity> specification);
    RecipeEntity save(RecipeEntity entity);
    void delete(RecipeEntity entity);
}
