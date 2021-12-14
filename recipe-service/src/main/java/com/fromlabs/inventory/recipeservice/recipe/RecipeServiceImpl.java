package com.fromlabs.inventory.recipeservice.recipe;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(rollbackFor = {Exception.class, Throwable.class})
public class RecipeServiceImpl implements RecipeService {

    private final RecipeRepository repository;

    public RecipeServiceImpl(RecipeRepository repository) {
        this.repository = repository;
    }

    public RecipeEntity get(Long id) {
        return this.repository.findById(id).orElse(null);
    }

    public RecipeEntity get(String code) {
        return this.repository.findByCode(code);
    }

    public RecipeEntity get(Long clientId, String name) {
        return this.repository.findByClientIdAndName(clientId, name);
    }

    public Page<RecipeEntity> getPage(Long clientId, Pageable pageable) {
        return this.repository.findAllByClientIdAndParentIdIsNull(clientId, pageable);
    }

    public List<RecipeEntity> getAll(Long clientId) {
        return this.repository.findAllByClientIdAndParentIdIsNull(clientId);
    }

    public Page<RecipeEntity> getPage(Long clientId, Long parentId, Pageable pageable) {
        return this.repository.findAllByClientIdAndParentId(clientId, parentId, pageable);
    }

    public List<RecipeEntity> getAll(Long clientId, Long parentId) {
        return this.repository.findAllByClientIdAndParentId(clientId, parentId);
    }

    public Page<RecipeEntity> getPage(Specification<RecipeEntity> specification, Pageable pageable) {
        return this.repository.findAll(specification, pageable);
    }

    public List<RecipeEntity> getAll(Specification<RecipeEntity> specification) {
        return this.repository.findAll(specification);
    }

    public RecipeEntity save(RecipeEntity entity) {
        return this.repository.save(entity);
    }

    public void delete(RecipeEntity entity) {
        this.repository.delete(entity);
    }
}
