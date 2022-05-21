package com.fromlabs.inventory.inventoryservice.ingredient.track.factory;

import com.fromlabs.inventory.inventoryservice.InventoryServiceApplication;
import com.fromlabs.inventory.inventoryservice.common.factory.FactoryCreateType;
import com.fromlabs.inventory.inventoryservice.ingredient.track.IngredientHistoryEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT, classes = InventoryServiceApplication.class)
@ActiveProfiles({"dev","liem-local"} )
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class IngredientHistoryEntityFactoryTest {

    @Test
    void create() {
        final var entity = IngredientHistoryEntityFactory
                .create(FactoryCreateType.RANDOM);
        Assertions.assertNotNull(entity);
    }
}