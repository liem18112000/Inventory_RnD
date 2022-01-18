package com.fromlabs.inventory.supplierservice.imports.details;

import com.fromlabs.inventory.supplierservice.common.exception.ObjectNotFoundException;
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
@Transactional
public class ImportDetailServiceImpl implements ImportDetailService {

    private final ImportDetailRepository repository;

    public ImportDetailServiceImpl(ImportDetailRepository repository) {
        this.repository = repository;
    }

    /**
     * Get an entity by id
     *
     * @param id Entity Identifier
     * @return Single entity
     */
    @Override
    public ImportDetailEntity getById(Long id) {
        return this.repository.findById(id).orElse(null);
    }

    /**
     * Save an entity
     *
     * @param entity Entity Identifier
     * @return Saved Entity
     */
    @Override
    public ImportDetailEntity save(ImportDetailEntity entity) {
        return this.repository.save(entity);
    }

    /**
     * Delete an entity
     *
     * @param entity Entity Identifier
     */
    @Override
    public void delete(ImportDetailEntity entity) {
        this.repository.delete(entity);
    }

    /**
     * Get page of entity with filter
     *
     * @param specification Specification
     * @param pageable      Pageable
     * @return Page of entity
     */
    @Override
    public Page<ImportDetailEntity> getPage(
            @NotNull final Specification<ImportDetailEntity> specification,
            @NotNull final Pageable pageable
    ) {
        return this.repository.findAll(specification, pageable);
    }

    /**
     * Get all entity by filter
     *
     * @param specification Specification
     * @return list if all entity by filter
     */
    @Override
    public List<ImportDetailEntity> getAll(
            @NotNull final Specification<ImportDetailEntity> specification
    ) {
        return this.repository.findAll(specification);
    }

    /**
     * Get import entity by id
     *
     * @param id Entity Unique ID
     * @return ImportDetailEntity
     * @throws ObjectNotFoundException throw if entity is not found
     */
    @Override
    public ImportDetailEntity getByIdWithException(
            @NotNull final Long id
    ) throws ObjectNotFoundException {
        return repository.findById(id).orElseThrow(() ->
                new ObjectNotFoundException("Import detail not found: "
                        .concat(String.valueOf(id))));
    }
}
