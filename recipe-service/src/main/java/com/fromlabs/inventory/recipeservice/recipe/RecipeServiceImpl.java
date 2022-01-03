package com.fromlabs.inventory.recipeservice.recipe;

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
public class RecipeServiceImpl implements RecipeService {

    private final RecipeRepository repository;

    /**
     * {@inheritDoc}
     */
    public RecipeServiceImpl(RecipeRepository repository) {
        this.repository = repository;
    }

    /**
     * {@inheritDoc}
     */
    public RecipeEntity getById(@NotNull final Long id) {
        return this.repository.findById(id).orElse(null);
    }

    /**
     * {@inheritDoc}
     */
    public RecipeEntity getByCode(@NotNull final String code) {
        return this.repository.findByCode(code);
    }

    /**
     * {@inheritDoc}
     */
    public RecipeEntity getByName(@NotNull final Long clientId, @NotNull final  String name) {
        return this.repository.findByClientIdAndName(clientId, name);
    }

    /**
     * {@inheritDoc}
     */
    public Page<RecipeEntity> getPage(@NotNull final Long clientId, @NotNull final  Pageable pageable) {
        return this.repository.findAllByClientIdAndParentIdIsNull(clientId, pageable);
    }

    /**
     * {@inheritDoc}
     */
    public List<RecipeEntity> getAll(@NotNull final Long clientId) {
        return this.repository.findAllByClientIdAndParentIdIsNull(clientId);
    }

    /**
     * {@inheritDoc}
     */
    public Page<RecipeEntity> getPage(
            @NotNull final Long clientId, @NotNull final Long parentId,
            @NotNull final Pageable pageable) {
        return this.repository.findAllByClientIdAndParentId(clientId, parentId, pageable);
    }

    /**
     * {@inheritDoc}
     */
    public List<RecipeEntity> getAll(@NotNull final Long clientId, @NotNull final Long parentId) {
        return this.repository.findAllByClientIdAndParentId(clientId, parentId);
    }

    /**
     * {@inheritDoc}
     */
    public Page<RecipeEntity> getPage(@NotNull final Specification<RecipeEntity> specification,
                                      @NotNull final Pageable pageable) {
        return this.repository.findAll(specification, pageable);
    }

    /**
     * {@inheritDoc}
     */
    public List<RecipeEntity> getAll(@NotNull final Specification<RecipeEntity> specification) {
        return this.repository.findAll(specification);
    }

    /**
     * {@inheritDoc}
     */
    public RecipeEntity save(@NotNull final RecipeEntity entity) {
        return this.repository.save(entity);
    }

    /**
     * {@inheritDoc}
     */
    public void delete(@NotNull final RecipeEntity entity) {
        this.repository.delete(entity);
    }
}
