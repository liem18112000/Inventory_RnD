package com.fromlabs.inventory.notificationservice.common.factory;


import com.fromlabs.inventory.notificationservice.common.entity.BaseEntityWithCreatedAt;

public class BaseEntityWithCreateAtAndStringIDFactory extends BaseEntityWithStringIDFactory {
    public BaseEntityWithCreatedAt<String> create() {
        return (BaseEntityWithCreatedAt<String>) super.create();
    }

    public BaseEntityWithCreatedAt<String> createRandom() {
        return (BaseEntityWithCreatedAt<String>) super.createRandom();
    }

    public BaseEntityWithCreatedAt<String> createEmpty() {
        return new BaseEntityWithCreatedAt<>();
    }

    public String getEntityClassName() {
        return this.getClass().getName();
    }
}
