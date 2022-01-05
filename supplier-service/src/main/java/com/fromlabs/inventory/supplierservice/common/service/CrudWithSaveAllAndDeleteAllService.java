/*
 * Copyright (c) 2021. Liem Doan
 */

package com.fromlabs.inventory.supplierservice.common.service;


import com.fromlabs.inventory.supplierservice.common.entity.BaseEntity;

import java.io.Serializable;
import java.util.List;

public interface CrudWithSaveAllAndDeleteAllService
        <ENTITY extends BaseEntity<ID>, ID extends Serializable>
        extends CrudService<ENTITY, ID>
{
    /**
     * Save multiple entities
     * @param entities  Entities
     * @return          List of saved entities
     */
    List<ENTITY> saveAll(Iterable<ENTITY> entities);

    /**
     * Delete multiple entities
     * @param entities  Entities
     */
    void deleteAll(Iterable<ENTITY> entities);
}
