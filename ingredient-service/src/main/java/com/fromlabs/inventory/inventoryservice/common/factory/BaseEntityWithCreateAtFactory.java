package com.fromlabs.inventory.inventoryservice.common.factory;

import com.fromlabs.inventory.inventoryservice.common.entity.BaseEntityWithCreatedAt;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
public class BaseEntityWithCreateAtFactory<ID extends Serializable> extends BaseEntityFactory<ID> {

    public BaseEntityWithCreatedAt<ID> create() {
        BaseEntityWithCreatedAt<ID> entity = (BaseEntityWithCreatedAt<ID>) super.create();
        entity.setCreateAt(DEFAULT_TIMESTAMP_PROPERTY);
        return entity;
    }

    public BaseEntityWithCreatedAt<ID> createEmpty() {
        return new BaseEntityWithCreatedAt<>();
    }

    public BaseEntityWithCreatedAt<ID> createRandom() {
        BaseEntityWithCreatedAt<ID> entity = (BaseEntityWithCreatedAt<ID>) super.createRandom();
        entity.setCreateAt(DEFAULT_TIMESTAMP_PROPERTY);
        return entity;
    }

}
