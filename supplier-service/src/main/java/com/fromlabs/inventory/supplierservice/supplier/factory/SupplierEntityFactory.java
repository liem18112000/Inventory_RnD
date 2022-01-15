/*
 * Copyright (c) 2021. Liem Doan
 */

package com.fromlabs.inventory.supplierservice.supplier.factory;

import com.fromlabs.inventory.supplierservice.common.factory.BaseEntityWithCreateAtAndLongIDFactory;
import com.fromlabs.inventory.supplierservice.common.factory.FactoryCreateType;
import com.fromlabs.inventory.supplierservice.supplier.SupplierEntity;

import java.util.ArrayList;

/**
 * Factory of SupplierEntity
 * @author Liem
 */
public class SupplierEntityFactory extends BaseEntityWithCreateAtAndLongIDFactory {

    /**
     * Create SupplierEntity by default
     * @return  SupplierEntity
     */
    public SupplierEntity create() {
        var entity = (SupplierEntity) super.create();
        entity.setCode(DEFAULT_STRING_PROPERTY);
        entity.setChildren(new ArrayList<>());
        entity.setGroup(false);
        return entity;
    }

    /**
     * Create SupplierEntity by random
     * @return  SupplierEntity
     */
    public SupplierEntity createRandom() {
        var entity = (SupplierEntity) super.createRandom();
        entity.setCode(RANDOM_STRING_PROPERTY);
        entity.setChildren(new ArrayList<>());
        entity.setGroup(false);
        return entity;
    }

    /**
     * Create SupplierEntity by empty
     * @return SupplierEntity
     */
    public SupplierEntity createEmpty() {
        return new SupplierEntity();
    }

    public String getEntityClassName() {
        return this.getClass().getName();
    }

    static private final SupplierEntityFactory factory = new SupplierEntityFactory();

    /**
     * Utility method for creating SupplierEntity with specific strategy
     * @param createType    FactoryCreateType
     * @return              SupplierEntity
     */
    static public SupplierEntity create(FactoryCreateType createType) {
        switch (createType) {
            case DEFAULT:   return factory.create();
            case RANDOM:    return factory.createRandom();
            default:        return factory.createEmpty();
        }
    }

}
