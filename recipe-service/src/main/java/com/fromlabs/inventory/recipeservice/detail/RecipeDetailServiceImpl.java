package com.fromlabs.inventory.recipeservice.detail;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * {@inheritDoc}
 */
@Service
@Transactional(rollbackFor = {Exception.class, Throwable.class})
public class RecipeDetailServiceImpl implements RecipeDetailService {

    private final RecipeDetailRepository repository;

    public RecipeDetailServiceImpl(RecipeDetailRepository repository) {
        this.repository = repository;
    }

    /**
     * {@inheritDoc}
     */
    public RecipeDetailEntity getById(@NotNull final Long id) {
        return this.repository.findById(id).orElse(null) ;
    }

    /**
     * {@inheritDoc}
     */
    public RecipeDetailEntity getByCode(@NotNull final String code) {
        return this.repository.findByCode(code);
    }

    /**
     * {@inheritDoc}
     */
    public RecipeDetailEntity getByName(@NotNull final Long clientId, @NotNull final String name) {
        return this.repository.findByClientIdAndName(clientId, name);
    }

    /**
     * {@inheritDoc}
     */
    public Page<RecipeDetailEntity> getPage(
            @NotNull final Specification<RecipeDetailEntity> specification, @NotNull final Pageable pageable) {
        return this.repository.findAll(specification, pageable);
    }

    /**
     * {@inheritDoc}
     */
    public List<RecipeDetailEntity> getAll(@NotNull final Specification<RecipeDetailEntity> specification) {
        return this.repository.findAll(specification);
    }

    /**
     * {@inheritDoc}
     */
    public RecipeDetailEntity save(@NotNull final RecipeDetailEntity entity) {
        return this.repository.save(entity);
    }

    /**
     * {@inheritDoc}
     */
    public void delete(@NotNull final RecipeDetailEntity entity) {
        this.repository.delete(entity);
    }
}
