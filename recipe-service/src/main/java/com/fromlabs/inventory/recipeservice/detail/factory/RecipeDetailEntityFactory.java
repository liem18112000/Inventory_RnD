package com.fromlabs.inventory.recipeservice.detail.factory;

import com.fromlabs.inventory.recipeservice.common.factory.BaseEntityWithLongIDFactory;
import com.fromlabs.inventory.recipeservice.common.factory.FactoryCreateType;
import com.fromlabs.inventory.recipeservice.detail.RecipeDetailEntity;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class RecipeDetailEntityFactory extends BaseEntityWithLongIDFactory {

    public RecipeDetailEntity create() {
        RecipeDetailEntity entity = (RecipeDetailEntity) super.create();

        return entity;
    }

    public RecipeDetailEntity createRandom() {
        RecipeDetailEntity entity = (RecipeDetailEntity) super.createRandom();

        return entity;
    }

    public RecipeDetailEntity createEmpty() {
        return new RecipeDetailEntity();
    }

    public String getEntityClassName() {
        return this.getClass().getName();
    }

    static private final RecipeDetailEntityFactory factory = new RecipeDetailEntityFactory();

    static public RecipeDetailEntity create(FactoryCreateType createType) {
        if(createType == FactoryCreateType.DEFAULT) return factory.create();
        else if(createType == FactoryCreateType.RANDOM) return factory.createRandom();
        else return factory.createEmpty();
    }
}
