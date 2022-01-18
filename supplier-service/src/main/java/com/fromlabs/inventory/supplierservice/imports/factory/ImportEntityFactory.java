/*
 * Copyright (c) 2021. Liem Doan
 */

package com.fromlabs.inventory.supplierservice.imports.factory;

import com.fromlabs.inventory.supplierservice.common.factory.BaseEntityWithCreateAtAndLongIDFactory;
import com.fromlabs.inventory.supplierservice.common.factory.FactoryCreateType;
import com.fromlabs.inventory.supplierservice.imports.ImportEntity;

/**
 * Import entity factory
 * @author Liem
 */
public class ImportEntityFactory extends BaseEntityWithCreateAtAndLongIDFactory {

    /**
     * Create ImportEntity by default
     * @return  ImportEntity
     */
    public ImportEntity create() {
        var entity = (ImportEntity) super.create();
        entity.setCode(DEFAULT_STRING_PROPERTY);
        return entity;
    }

    /**
     * Create ImportEntity by random
     * @return  ImportEntity
     */
    public ImportEntity createRandom() {
        var entity = (ImportEntity) super.createRandom();
        entity.setCode(RANDOM_STRING_PROPERTY);
        return entity;
    }

    /**
     * Create ImportEntity by empty
     * @return ImportEntity
     */
    public ImportEntity createEmpty() {
        return new ImportEntity();
    }

    public String getEntityClassName() {
        return this.getClass().getName();
    }

    static private final ImportEntityFactory factory = new ImportEntityFactory();

    /**
     * Utility method for creating ImportEntity with specific strategy
     * @param createType    FactoryCreateType
     * @return              ImportEntity
     */
    static public ImportEntity create(FactoryCreateType createType) {
        switch (createType) {
            case DEFAULT:   return factory.create();
            case RANDOM:    return factory.createRandom();
            default:        return factory.createEmpty();
        }
    }
}
