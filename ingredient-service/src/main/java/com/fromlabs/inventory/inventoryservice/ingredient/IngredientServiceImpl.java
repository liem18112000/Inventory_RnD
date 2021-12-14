package com.fromlabs.inventory.inventoryservice.ingredient;

import com.fromlabs.inventory.inventoryservice.ingredient.config.IngredientConfigEntity;
import com.fromlabs.inventory.inventoryservice.ingredient.config.IngredientConfigRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Ingredient service implementation
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class IngredientServiceImpl implements IngredientService{

    final private IngredientRepository repository;
    final private IngredientConfigRepository configRepository;

    public IngredientServiceImpl(
            IngredientRepository repository,
            IngredientConfigRepository configRepository
    ) {
        this.repository = repository;
        this.configRepository = configRepository;
    }

    public IngredientEntity get(Long id) {
        return this.repository.findById(id).orElse(null);
    }

    public IngredientEntity get(String code) {
        return this.repository.findByCode(code);
    }

    public IngredientEntity get(Long clientId, String name) {
        return this.repository.findByClientIdAndName(clientId, name);
    }

    public Page<IngredientEntity> getPage(Long clientId, Pageable pageable) {
        return this.repository.findAllByClientIdAndParentIdIsNull(clientId, pageable);
    }

    public List<IngredientEntity> getAll(Long clientId) {
        return this.repository.findAllByClientIdAndParentIdIsNull(clientId);
    }

    public List<IngredientEntity> getAllChild(Long clientId) {
        return this.repository.findAllByClientIdAndParentIdIsNotNull(clientId);
    }

    public Page<IngredientEntity> getPage(Long clientId, Long parentId, Pageable pageable) {
        return this.repository.findAllByClientIdAndParentId(clientId, parentId, pageable);
    }

    public List<IngredientEntity> getAll(Long clientId, Long parentId) {
        return this.repository.findAllByClientIdAndParentId(clientId, parentId);
    }

    public Page<IngredientEntity> getPage(Specification<IngredientEntity> specification, Pageable pageable) {
        return this.repository.findAll(specification, pageable);
    }

    public List<IngredientEntity> getAll(Specification<IngredientEntity> specification) {
        return this.repository.findAll(specification);
    }

    public IngredientEntity save(IngredientEntity entity) {
        return this.repository.save(entity);
    }

    public void delete(IngredientEntity entity) {
        this.repository.delete(entity);
    }

    public IngredientConfigEntity getConfig(Long id) {
        return this.configRepository.getById(id);
    }

    public IngredientConfigEntity getConfig(Long clientId, IngredientEntity entity) {
        return this.configRepository.findByClientIdAndIngredient(clientId, entity);
    }

    public List<IngredientConfigEntity> getAllConfig(Long clientId) {
        return this.configRepository.findAllByClientId(clientId);
    }

    public Page<IngredientConfigEntity> getPageConfig(Long clientId, Pageable pageable) {
        return this.configRepository.findAllByClientId(clientId, pageable);
    }

    public Page<IngredientConfigEntity> getPageConfig(Specification<IngredientConfigEntity> specification, Pageable pageable) {
        return this.configRepository.findAll(specification, pageable);
    }

    public List<IngredientConfigEntity> getAllConfig(Specification<IngredientConfigEntity> specification) {
        return this.configRepository.findAll(specification);
    }

    public IngredientConfigEntity saveConfig(IngredientConfigEntity entity) {
        return this.configRepository.save(entity);
    }

    public void deleteConfig(IngredientConfigEntity entity) {
        this.configRepository.delete(entity);
    }
}
