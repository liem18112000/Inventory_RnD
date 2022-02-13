package com.fromlabs.inventory.supplierservice.common.factory;

import com.fromlabs.inventory.supplierservice.common.entity.BaseEntityWithCreatedAt;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class BaseEntityWithCreateAtAndLongIDFactory extends BaseEntityWithLongIDFactory {

    public BaseEntityWithCreatedAt<Long> create() {
        var entity = (BaseEntityWithCreatedAt<Long>) super.create();
        entity.setCreatedAt(DEFAULT_TIMESTAMP_PROPERTY);
        return entity;
    }

    public BaseEntityWithCreatedAt<Long> createRandom() {
        var entity = (BaseEntityWithCreatedAt<Long>) super.createRandom();
        entity.setCreatedAt(DEFAULT_TIMESTAMP_PROPERTY);
        return entity;
    }

    public BaseEntityWithCreatedAt<Long> createEmpty() {
        return new BaseEntityWithCreatedAt<>();
    }

    public String getEntityClassName() {
        return this.getClass().getName();
    }
}
