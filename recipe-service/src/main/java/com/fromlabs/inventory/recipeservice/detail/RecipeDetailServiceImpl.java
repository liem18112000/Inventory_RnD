package com.fromlabs.inventory.recipeservice.detail;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(rollbackFor = {Exception.class, Throwable.class})
public class RecipeDetailServiceImpl implements RecipeDetailService {

    private final RecipeDetailRepository repository;

    public RecipeDetailServiceImpl(RecipeDetailRepository repository) {
        this.repository = repository;
    }

    public RecipeDetailEntity get(Long id) {
        return this.repository.findById(id).orElse(null) ;
    }

    public RecipeDetailEntity get(String code) {
        return this.repository.findByCode(code);
    }

    public RecipeDetailEntity get(Long clientId, String name) {
        return this.repository.findByClientIdAndName(clientId, name);
    }

    public Page<RecipeDetailEntity> getPage(Specification<RecipeDetailEntity> specification, Pageable pageable) {
        return this.repository.findAll(specification, pageable);
    }

    public List<RecipeDetailEntity> getAll(Specification<RecipeDetailEntity> specification) {
        return this.repository.findAll(specification);
    }

    public RecipeDetailEntity save(RecipeDetailEntity entity) {
        return this.repository.save(entity);
    }

    public void delete(RecipeDetailEntity entity) {
        this.repository.delete(entity);
    }
}
