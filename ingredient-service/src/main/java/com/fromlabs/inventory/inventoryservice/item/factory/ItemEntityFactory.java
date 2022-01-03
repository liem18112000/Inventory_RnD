/*
 * Copyright (c) 2021. Liem Doan
 */

package com.fromlabs.inventory.inventoryservice.item.factory;

import com.fromlabs.inventory.inventoryservice.common.factory.BaseEntityWithLongIDFactory;
import com.fromlabs.inventory.inventoryservice.common.factory.FactoryCreateType;
import com.fromlabs.inventory.inventoryservice.item.ItemEntity;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ItemEntityFactory extends BaseEntityWithLongIDFactory {

    public ItemEntity create() {
        return (ItemEntity) super.create();
    }

    public ItemEntity createRandom() {
        return (ItemEntity) super.createRandom();
    }

    public ItemEntity createEmpty() {
        return new ItemEntity();
    }

    public String getEntityClassName() {
        return this.getClass().getName();
    }

    static private final ItemEntityFactory factory = new ItemEntityFactory();

    static public ItemEntity create(FactoryCreateType createType) {
        switch (createType) {
            case DEFAULT:   return factory.create();
            case RANDOM:    return factory.createRandom();
            default:        return factory.createEmpty();
        }
    }
}
