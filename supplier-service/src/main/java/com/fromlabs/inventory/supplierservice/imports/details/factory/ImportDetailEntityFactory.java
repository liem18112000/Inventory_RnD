/*
 * Copyright (c) 2021. Liem Doan
 */

package com.fromlabs.inventory.supplierservice.imports.details.factory;

import com.fromlabs.inventory.supplierservice.common.factory.BaseEntityWithCreateAtAndLongIDFactory;
import com.fromlabs.inventory.supplierservice.common.factory.FactoryCreateType;
import com.fromlabs.inventory.supplierservice.imports.details.ImportDetailEntity;

/**
 * Import detail entity factory
 * @author Liem
 */
public class ImportDetailEntityFactory extends BaseEntityWithCreateAtAndLongIDFactory {

    /**
     * Create ImportDetailEntity by default
     * @return  ImportDetailEntity
     */
    public ImportDetailEntity create() {
        var entity = (ImportDetailEntity) super.create();
        entity.setQuantity(DEFAULT_NUMBER_PROPERTY.floatValue());
        entity.setIngredientId(2L);
        return entity;
    }

    /**
     * Create ImportDetailEntity by random
     * @return  ImportDetailEntity
     */
    public ImportDetailEntity createRandom() {
        var entity = (ImportDetailEntity) super.createRandom();
        entity.setQuantity(RANDOM_NUMBER_PROPERTY.floatValue());
        entity.setIngredientId(RANDOM_NUMBER_PROPERTY);
        return entity;
    }

    /**
     * Create ImportDetailEntity by empty
     * @return ImportDetailEntity
     */
    public ImportDetailEntity createEmpty() {
        return new ImportDetailEntity();
    }

    public String getEntityClassName() {
        return this.getClass().getName();
    }

    static private final ImportDetailEntityFactory factory = new ImportDetailEntityFactory();

    /**
     * Utility method for creating ImportEntity with specific strategy
     * @param createType    FactoryCreateType
     * @return              ImportEntity
     */
    static public ImportDetailEntity create(FactoryCreateType createType) {
        switch (createType) {
            case DEFAULT:   return factory.create();
            case RANDOM:    return factory.createRandom();
            default:        return factory.createEmpty();
        }
    }
}
