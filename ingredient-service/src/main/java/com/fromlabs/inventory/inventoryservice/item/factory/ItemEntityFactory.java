/*
 * Copyright (c) 2021. Liem Doan
 */

package com.fromlabs.inventory.inventoryservice.item.factory;

import com.fromlabs.inventory.inventoryservice.common.factory.BaseEntityWithLongIDFactory;
import com.fromlabs.inventory.inventoryservice.common.factory.FactoryCreateType;
import com.fromlabs.inventory.inventoryservice.inventory.InventoryEntity;
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
        if(createType == FactoryCreateType.DEFAULT) return factory.create();
        else if(createType == FactoryCreateType.RANDOM) return factory.createRandom();
        else return factory.createEmpty();
    }
}
