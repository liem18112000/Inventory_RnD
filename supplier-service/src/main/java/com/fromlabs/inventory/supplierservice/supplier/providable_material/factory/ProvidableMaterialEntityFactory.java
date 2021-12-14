/*
 * Copyright (c) 2021. Liem Doan
 */

package com.fromlabs.inventory.supplierservice.supplier.providable_material.factory;

import com.fromlabs.inventory.supplierservice.common.factory.BaseEntityWithLongIDFactory;
import com.fromlabs.inventory.supplierservice.common.factory.FactoryCreateType;
import com.fromlabs.inventory.supplierservice.supplier.providable_material.ProvidableMaterialEntity;

public class ProvidableMaterialEntityFactory extends BaseEntityWithLongIDFactory {

    protected final float DEFAULT_MAX_QUANTITY = 32767f;
    protected final float DEFAULT_MIN_QUANTITY = 1f;

    protected final float RANDOM_MAX_QUANTITY = (int) (Math.random() * (DEFAULT_MAX_QUANTITY / 2) + (DEFAULT_MAX_QUANTITY / 2));
    protected final float RANDOM_MIN_QUANTITY = (int) (Math.random() * (DEFAULT_MAX_QUANTITY / 2));

    /**
     * Create ProvidableMaterialEntity by default
     * @return  ProvidableMaterialEntity
     */
    public ProvidableMaterialEntity create() {
        var entity = (ProvidableMaterialEntity) super.create();
        entity.setMinimumQuantity(DEFAULT_MIN_QUANTITY);
        entity.setMaximumQuantity(DEFAULT_MAX_QUANTITY);
        return entity;
    }

    /**
     * Create ProvidableMaterialEntity by random
     * @return  ProvidableMaterialEntity
     */
    public ProvidableMaterialEntity createRandom() {
        var entity = (ProvidableMaterialEntity) super.createRandom();
        entity.setMinimumQuantity(RANDOM_MIN_QUANTITY);
        entity.setMaximumQuantity(RANDOM_MAX_QUANTITY);
        return entity;
    }

    /**
     * Create ProvidableMaterialEntity by empty
     * @return ProvidableMaterialEntity
     */
    public ProvidableMaterialEntity createEmpty() {
        return new ProvidableMaterialEntity();
    }

    public String getEntityClassName() {
        return this.getClass().getName();
    }

    static private final ProvidableMaterialEntityFactory factory = new ProvidableMaterialEntityFactory();

    /**
     * Utility method for creating ProvidableMaterialEntity with specific strategy
     * @param createType    FactoryCreateType
     * @return              ProvidableMaterialEntity
     */
    static public ProvidableMaterialEntity create(
            FactoryCreateType createType
    ) {
        switch (createType) {
            case DEFAULT:   return factory.create();
            case RANDOM:    return factory.createRandom();
            default:        return factory.createEmpty();
        }
    }
}
