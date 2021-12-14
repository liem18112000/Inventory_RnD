package com.fromlabs.inventory.recipeservice.detail;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface RecipeDetailRepository extends JpaRepository<RecipeDetailEntity, Long>, JpaSpecificationExecutor<RecipeDetailEntity> {
    RecipeDetailEntity findByCode(String code);
    RecipeDetailEntity findByClientIdAndName(Long clientId, String name);
}
