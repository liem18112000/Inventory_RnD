package com.fromlabs.inventory.recipeservice.common.factory;

import com.fromlabs.inventory.recipeservice.common.entity.BaseEntity;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class BaseEntityWithStringIDFactory extends BaseEntityFactory<String> {

    public BaseEntity<String> create() {
        BaseEntity<String> entity = super.create();
        entity.setId(DEFAULT_NUMBER_PROPERTY.toString());
        return entity;
    }

    public BaseEntity<String> createRandom() {
        BaseEntity<String> entity = super.createRandom();
        entity.setId(RANDOM_NUMBER_PROPERTY.toString());
        return entity;
    }

    public BaseEntity<String> createEmpty() {
        return new BaseEntity<>();
    }

    public String getEntityClassName() {
        return this.getClass().getName();
    }
}
