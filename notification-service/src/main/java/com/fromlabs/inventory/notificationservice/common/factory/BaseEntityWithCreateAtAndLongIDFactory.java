package com.fromlabs.inventory.notificationservice.common.factory;

import com.fromlabs.inventory.notificationservice.common.entity.BaseEntityWithCreatedAt;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class BaseEntityWithCreateAtAndLongIDFactory extends BaseEntityWithLongIDFactory {

    public BaseEntityWithCreatedAt<Long> create() {
        var entity = (BaseEntityWithCreatedAt<Long>) super.create();
        entity.setCreateAt(DEFAULT_TIMESTAMP_PROPERTY);
        return entity;
    }

    public BaseEntityWithCreatedAt<Long> createRandom() {
        var entity = (BaseEntityWithCreatedAt<Long>) super.createRandom();
        entity.setCreateAt(DEFAULT_TIMESTAMP_PROPERTY);
        return entity;
    }

    public BaseEntityWithCreatedAt<Long> createEmpty() {
        return new BaseEntityWithCreatedAt<>();
    }

    public String getEntityClassName() {
        return this.getClass().getName();
    }
}
