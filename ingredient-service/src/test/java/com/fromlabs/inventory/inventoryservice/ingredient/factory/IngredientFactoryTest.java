package com.fromlabs.inventory.inventoryservice.ingredient.factory;

import com.fromlabs.inventory.inventoryservice.InventoryServiceApplication;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static com.fromlabs.inventory.inventoryservice.common.factory.FactoryCreateType.RANDOM;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT, classes = InventoryServiceApplication.class)
@ActiveProfiles({"dev","liem-local"} )
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class IngredientFactoryTest {

    @Test
    public void createRandom() {
        final var ingredient = IngredientEntityFactory.create(RANDOM);
        Assertions.assertFalse(ingredient.isCategory());
        Assertions.assertTrue(ingredient.getChildren().isEmpty());
    }

}
