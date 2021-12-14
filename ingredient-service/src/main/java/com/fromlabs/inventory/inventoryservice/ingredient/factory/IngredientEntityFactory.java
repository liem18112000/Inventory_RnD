/*
 * Copyright (c) 2021. Liem Doan
 */

package com.fromlabs.inventory.inventoryservice.ingredient.factory;

import com.fromlabs.inventory.inventoryservice.common.factory.BaseEntityWithCreateAtAndLongIDFactory;
import com.fromlabs.inventory.inventoryservice.common.factory.FactoryCreateType;
import com.fromlabs.inventory.inventoryservice.ingredient.IngredientEntity;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@NoArgsConstructor
public class IngredientEntityFactory extends BaseEntityWithCreateAtAndLongIDFactory {

    public IngredientEntity create() {
        var entity = (IngredientEntity) super.create();
        entity.setChildren(new ArrayList<>());
        entity.setCategory(false);
        return entity;
    }

    public IngredientEntity createRandom() {
        var entity = (IngredientEntity) super.createRandom();
        entity.setChildren(new ArrayList<>());
        entity.setCategory(false);
        return entity;
    }

    public IngredientEntity createEmpty() {
        return new IngredientEntity();
    }

    public String getEntityClassName() {
        return this.getClass().getName();
    }

    static private final IngredientEntityFactory factory = new IngredientEntityFactory();

    static public IngredientEntity create(FactoryCreateType createType) {
        if(createType == FactoryCreateType.DEFAULT) return factory.create();
        else if(createType == FactoryCreateType.RANDOM) return factory.createRandom();
        else return factory.createEmpty();
    }
}
