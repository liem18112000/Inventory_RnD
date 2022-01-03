/*
 * Copyright (c) 2021. Liem Doan
 */

package com.fromlabs.inventory.inventoryservice.common.service;

import com.fromlabs.inventory.inventoryservice.common.entity.BaseEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.io.Serializable;

/**
 * Page and soring with filter service
 * {@inheritDoc}
 */
public interface PagingAndSortingWithSpecificationService
        <ENTITY extends BaseEntity<ID>, ID extends Serializable>
        extends CrudService<ENTITY, ID>
{

    /**
     * Get page of entity with filter
     * @param specification Specification
     * @param pageable Pageable
     * @return  Page of entity
     */
    Page<ENTITY> getPage(Specification<ENTITY> specification, Pageable pageable);
}
