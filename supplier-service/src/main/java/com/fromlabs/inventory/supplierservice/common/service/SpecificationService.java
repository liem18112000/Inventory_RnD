/*
 * Copyright (c) 2021. Liem Doan
 */

package com.fromlabs.inventory.inventoryservice.common.service;


import com.fromlabs.inventory.inventoryservice.common.entity.BaseEntity;
import org.springframework.data.jpa.domain.Specification;

import java.io.Serializable;
import java.util.List;

public interface SpecificationService
        <ENTITY extends BaseEntity<ID>, ID extends Serializable>
        extends CrudService<ENTITY, ID>
{
    List<ENTITY> getAll(Specification<ENTITY> specification);
}
