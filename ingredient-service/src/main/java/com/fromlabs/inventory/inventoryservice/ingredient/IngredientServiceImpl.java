package com.fromlabs.inventory.inventoryservice.ingredient;

import com.fromlabs.inventory.inventoryservice.ingredient.config.IngredientConfigEntity;
import com.fromlabs.inventory.inventoryservice.ingredient.config.IngredientConfigService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Ingredient service implementation
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class IngredientServiceImpl implements IngredientService{

    //<editor-fold desc="SETUP">

    final private IngredientRepository repository;
    final private IngredientConfigService configService;

    public IngredientServiceImpl(
            @NotNull final IngredientRepository repository,
            @NotNull final IngredientConfigService configService
    ) {
        this.repository = repository;
        this.configService = configService;
    }

    //</editor-fold>

    //<editor-fold desc="INGREDIENT">

    public IngredientEntity getById(
            @NotNull final Long id
    ) {
        return this.repository.findById(id).orElse(null);
    }

    public IngredientEntity getByCode(
            @NotNull final String code
    ) {
        return this.repository.findByCode(code);
    }

    public IngredientEntity getByName(
            @NotNull final Long clientId,
            @NotNull final String name
    ) {
        return this.repository.findByClientIdAndName(clientId, name);
    }

    public Page<IngredientEntity> getPage(
            @NotNull final Long clientId,
            @NotNull final Pageable pageable
    ) {
        return this.repository.findAllByClientIdAndParentIdIsNull(clientId, pageable);
    }

    public List<IngredientEntity> getAll(
            @NotNull final Long clientId
    ) {
        return this.repository.findAllByClientIdAndParentIdIsNull(clientId);
    }

    public List<IngredientEntity> getAllChild(
            @NotNull final Long clientId
    ) {
        return this.repository.findAllByClientIdAndParentIdIsNotNull(clientId);
    }

    public Page<IngredientEntity> getPage(
            @NotNull final Long clientId,
            @NotNull final Long parentId,
            @NotNull final Pageable pageable
    ) {
        return this.repository.findAllByClientIdAndParentId(clientId, parentId, pageable);
    }

    public List<IngredientEntity> getAll(
            @NotNull final Long clientId,
            @NotNull final Long parentId
    ) {
        return this.repository.findAllByClientIdAndParentId(clientId, parentId);
    }

    public Page<IngredientEntity> getPage(
            @NotNull final Specification<IngredientEntity> specification,
            @NotNull final Pageable pageable
    ) {
        return this.repository.findAll(specification, pageable);
    }

    public List<IngredientEntity> getAll(
            @NotNull final Specification<IngredientEntity> specification
    ) {
        return this.repository.findAll(specification);
    }

    public IngredientEntity save(
            @NotNull final IngredientEntity entity
    ) {
        return this.repository.save(entity);
    }

    public void delete(
            @NotNull final IngredientEntity entity
    ) {
        this.repository.delete(entity);
    }

    //</editor-fold>

    //<editor-fold desc="INGREDIENT CONFIG">

    public IngredientConfigEntity getConfig(
            @NotNull final Long id
    ) {
        return configService.getById(id);
    }

    public IngredientConfigEntity getConfig(
            @NotNull final Long clientId,
            @NotNull final IngredientEntity entity
    ) {
        return configService.getByIngredient(clientId, entity);
    }

    public List<IngredientConfigEntity> getAllConfig(
            @NotNull final Long clientId
    ) {
        return configService.getAll(clientId);
    }

    public Page<IngredientConfigEntity> getPageConfig(
            @NotNull final Long clientId,
            @NotNull final Pageable pageable
    ) {
        return configService.getPage(clientId, pageable);
    }

    public Page<IngredientConfigEntity> getPageConfig(
            @NotNull final Specification<IngredientConfigEntity> specification,
            @NotNull final Pageable pageable
    ) {
        return configService.getPage(specification, pageable);
    }

    public List<IngredientConfigEntity> getAllConfig(
            @NotNull final Specification<IngredientConfigEntity> specification
    ) {
        return configService.getAll(specification);
    }

    public IngredientConfigEntity saveConfig(
            @NotNull final IngredientConfigEntity entity
    ) {
        return configService.save(entity);
    }

    public void deleteConfig(
            @NotNull final IngredientConfigEntity entity
    ) {
        configService.delete(entity);
    }

    //</editor-fold>
}
