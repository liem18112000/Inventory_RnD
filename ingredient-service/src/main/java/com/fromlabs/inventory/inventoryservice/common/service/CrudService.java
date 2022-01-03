/*
 * Copyright (c) 2021. Liem Doan
 */

package com.fromlabs.inventory.inventoryservice.common.service;

import com.fromlabs.inventory.inventoryservice.common.entity.BaseEntity;

import java.io.Serializable;

/**
 * @author Liem
 * @param <ENTITY>  Entity Datatype
 * @param <ID>      Entity Identifier Datatype
 */
public interface CrudService<ENTITY extends BaseEntity<ID>, ID extends Serializable> {

    /**
     * Get an entity by id
     * @param id    Entity Identifier
     * @return      Single entity
     */
    ENTITY getById(ID id);

    /**
     * Save an entity
     * @param entity    Entity Identifier
     * @return          Saved Entity
     */
    ENTITY save(ENTITY entity);

    /**
     * Delete an entity
     * @param entity    Entity Identifier
     */
    void delete(ENTITY entity);


}
