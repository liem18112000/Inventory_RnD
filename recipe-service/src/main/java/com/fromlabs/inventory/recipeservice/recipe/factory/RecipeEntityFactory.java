package com.fromlabs.inventory.recipeservice.recipe.factory;

import com.fromlabs.inventory.recipeservice.common.factory.BaseEntityWithLongIDFactory;
import com.fromlabs.inventory.recipeservice.common.factory.FactoryCreateType;
import com.fromlabs.inventory.recipeservice.recipe.RecipeEntity;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class RecipeEntityFactory extends BaseEntityWithLongIDFactory {

    public RecipeEntity create() {
        return (RecipeEntity) super.create();
    }

    public RecipeEntity createRandom() {
        return (RecipeEntity) super.createRandom();
    }

    public RecipeEntity createEmpty() {
        return new RecipeEntity();
    }

    public String getEntityClassName() {
        return this.getClass().getName();
    }

    static private final RecipeEntityFactory factory = new RecipeEntityFactory();

    static public RecipeEntity create(FactoryCreateType createType) {
        if(createType == FactoryCreateType.DEFAULT) return factory.create();
        else if(createType == FactoryCreateType.RANDOM) return factory.createRandom();
        else return factory.createEmpty();
    }
}
