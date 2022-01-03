/*
 * Copyright (c) 2021. Liem Doan
 */

package com.fromlabs.inventory.inventoryservice.inventory.factory;

import com.fromlabs.inventory.inventoryservice.common.factory.BaseEntityWithLongIDFactory;
import com.fromlabs.inventory.inventoryservice.common.factory.FactoryCreateType;
import com.fromlabs.inventory.inventoryservice.inventory.InventoryEntity;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class InventoryEntityFactory extends BaseEntityWithLongIDFactory {

    public InventoryEntity create() {
        return (InventoryEntity) super.create();
    }

    public InventoryEntity createRandom() {
        return (InventoryEntity) super.createRandom();
    }

    public InventoryEntity createEmpty() {
        return new InventoryEntity();
    }

    public String getEntityClassName() {
        return this.getClass().getName();
    }

    static private final InventoryEntityFactory factory = new InventoryEntityFactory();

    static public InventoryEntity create(FactoryCreateType createType) {
        if(createType == FactoryCreateType.DEFAULT) return factory.create();
        else if(createType == FactoryCreateType.RANDOM) return factory.createRandom();
        else return factory.createEmpty();
    }
}
