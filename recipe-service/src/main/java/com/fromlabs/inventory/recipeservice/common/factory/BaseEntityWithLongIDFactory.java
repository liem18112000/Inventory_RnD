package com.fromlabs.inventory.recipeservice.common.factory;

import com.fromlabs.inventory.recipeservice.common.entity.BaseEntity;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class BaseEntityWithLongIDFactory extends BaseEntityFactory<Long> {

    public BaseEntity<Long> create() {
        BaseEntity<Long> entity = super.create();
        entity.setId(DEFAULT_NUMBER_PROPERTY);
        return entity;
    }

    public BaseEntity<Long> createRandom() {
        BaseEntity<Long> entity = super.createRandom();
        entity.setId(RANDOM_NUMBER_PROPERTY);
        return entity;
    }

    public BaseEntity<Long> createEmpty() {
        return new BaseEntity<>();
    }

    public String getEntityClassName() {
        return this.getClass().getName();
    }
}
