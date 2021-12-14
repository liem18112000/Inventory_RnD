package com.fromlabs.inventory.recipeservice.common.factory;

import com.fromlabs.inventory.recipeservice.common.entity.BaseEntity;

import java.io.Serializable;
import java.time.Instant;

public interface EntityFactory<ID extends Serializable> {
    String DEFAULT_STRING_PROPERTY     = "Default";
    Long   DEFAULT_NUMBER_PROPERTY     = 0L;
    String DEFAULT_TIMESTAMP_PROPERTY  = Instant.now().toString();
    String RANDOM_STRING_PROPERTY      = "Random".concat(String.valueOf(System.currentTimeMillis()));
    Long   RANDOM_NUMBER_PROPERTY      = System.currentTimeMillis();
    BaseEntity<ID> create();
    BaseEntity<ID> createEmpty();
    BaseEntity<ID> createRandom();
    String getEntityClassName();
}
