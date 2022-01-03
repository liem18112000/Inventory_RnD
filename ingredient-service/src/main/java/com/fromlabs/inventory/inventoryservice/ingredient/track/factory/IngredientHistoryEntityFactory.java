/*
 * Copyright (c) 2021. Liem Doan
 */

package com.fromlabs.inventory.inventoryservice.ingredient.track.factory;

import com.fromlabs.inventory.inventoryservice.common.factory.BaseEntityWithLongIDFactory;
import com.fromlabs.inventory.inventoryservice.common.factory.FactoryCreateType;
import com.fromlabs.inventory.inventoryservice.ingredient.track.IngredientHistoryEntity;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDateTime;

import static com.fromlabs.inventory.inventoryservice.ingredient.event.status.IngredientEventStatus.UNKNOWN;

/**
 * Ingredient history entity factory
 * @author Liem
 */
@NoArgsConstructor
public class IngredientHistoryEntityFactory extends BaseEntityWithLongIDFactory {

    public IngredientHistoryEntity create() {
        var entity = (IngredientHistoryEntity) super.create();
        entity.setActorRole(IngredientHistoryEntity.DEFAULT_ACTOR_ROLE);
        entity.setActorName(DEFAULT_STRING_PROPERTY);
        entity.setTrackTimestamp(LocalDateTime.now().toString());
        entity.setExtraInformation(DEFAULT_STRING_PROPERTY);
        entity.setUpdateAt(Instant.now().toString());
        entity.setEventStatus(UNKNOWN.getName());
        return entity;
    }

    public IngredientHistoryEntity createRandom() {
        var entity = (IngredientHistoryEntity) super.createRandom();
        entity.setActorName(IngredientHistoryEntity.DEFAULT_ACTOR_ROLE.concat(String.valueOf(RANDOM_NUMBER_PROPERTY)));
        entity.setActorName(RANDOM_STRING_PROPERTY);
        entity.setTrackTimestamp(DEFAULT_TIMESTAMP_PROPERTY);
        entity.setExtraInformation(DEFAULT_TIMESTAMP_PROPERTY);
        entity.setUpdateAt(Instant.now().toString());
        entity.setEventStatus(UNKNOWN.getName());
        return entity;
    }

    public IngredientHistoryEntity createEmpty() {
        return new IngredientHistoryEntity();
    }

    public String getEntityClassName() {
        return this.getClass().getName();
    }

    static private final IngredientHistoryEntityFactory factory = new IngredientHistoryEntityFactory();

    static public IngredientHistoryEntity create(
            FactoryCreateType createType
    ) {
        switch (createType) {
            case DEFAULT:   return factory.create();
            case RANDOM:    return factory.createRandom();
            default:        return factory.createEmpty();
        }
    }
}
