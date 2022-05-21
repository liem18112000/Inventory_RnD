package com.fromlabs.inventory.inventoryservice.inventory.factory;

import com.fromlabs.inventory.inventoryservice.common.factory.FactoryCreateType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InventoryEntityFactoryTest {

    @Test
    void create() {
        Assertions.assertNotNull(InventoryEntityFactory.create(FactoryCreateType.EMPTY));
        Assertions.assertNotNull(InventoryEntityFactory.create(FactoryCreateType.DEFAULT));
        Assertions.assertNotNull(InventoryEntityFactory.create(FactoryCreateType.RANDOM));
        Assertions.assertNotNull((new InventoryEntityFactory()).getEntityClassName());
    }
}