package com.fromlabs.inventory.supplierservice.common.factory;

import com.fromlabs.inventory.supplierservice.common.entity.BaseEntityWithCreatedAt;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
public class BaseEntityWithCreateAtFactory<ID extends Serializable> extends BaseEntityFactory<ID> {

    public BaseEntityWithCreatedAt<ID> create() {
        BaseEntityWithCreatedAt<ID> entity = (BaseEntityWithCreatedAt<ID>) super.create();
        entity.setCreatedAt(DEFAULT_TIMESTAMP_PROPERTY);
        return entity;
    }

    public BaseEntityWithCreatedAt<ID> createEmpty() {
        return new BaseEntityWithCreatedAt<>();
    }

    public BaseEntityWithCreatedAt<ID> createRandom() {
        BaseEntityWithCreatedAt<ID> entity = (BaseEntityWithCreatedAt<ID>) super.createRandom();
        entity.setCreatedAt(DEFAULT_TIMESTAMP_PROPERTY);
        return entity;
    }

    public String getEntityClassName() {
        return super.getEntityClassName();
    }
}
