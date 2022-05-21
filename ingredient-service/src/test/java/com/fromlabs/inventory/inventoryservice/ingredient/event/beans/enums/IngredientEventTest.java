package com.fromlabs.inventory.inventoryservice.ingredient.event.beans.enums;

import com.fromlabs.inventory.inventoryservice.InventoryServiceApplication;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static com.fromlabs.inventory.inventoryservice.ingredient.event.beans.enums.IngredientEvent.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT, classes = InventoryServiceApplication.class)
@ActiveProfiles({"dev","liem-local"} )
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class IngredientEventTest {
    @Test
    public void test() {
        Assertions.assertNotNull(INGREDIENT_ITEM_DEFAULT.getEvent());
        Assertions.assertNotNull(INGREDIENT_ITEM_DEFAULT.getEventMessage());

        Assertions.assertNotNull(INGREDIENT_ITEM_ADD.getEvent());
        Assertions.assertNotNull(INGREDIENT_ITEM_ADD.getEventMessage());

        Assertions.assertNotNull(INGREDIENT_ITEM_BATCH_ADD.getEvent());
        Assertions.assertNotNull(INGREDIENT_ITEM_BATCH_ADD.getEventMessage());

        Assertions.assertNotNull(INGREDIENT_ITEM_REMOVE.getEvent());
        Assertions.assertNotNull(INGREDIENT_ITEM_REMOVE.getEventMessage());

        Assertions.assertNotNull(INGREDIENT_ITEM_MODIFY.getEvent());
        Assertions.assertNotNull(INGREDIENT_ITEM_MODIFY.getEventMessage());

        Assertions.assertNotNull(INGREDIENT_CREATE.getEvent());
        Assertions.assertNotNull(INGREDIENT_CREATE.getEventMessage());

        Assertions.assertNotNull(INGREDIENT_REMOVE.getEvent());
        Assertions.assertNotNull(INGREDIENT_REMOVE.getEventMessage());

        Assertions.assertNotNull(INGREDIENT_MODIFY.getEvent());
        Assertions.assertNotNull(INGREDIENT_MODIFY.getEventMessage());

        Assertions.assertNotNull(INGREDIENT_CONFIG_MODIFY.getEventMessage());
        Assertions.assertNotNull(INGREDIENT_CONFIG_MODIFY.getEventMessage());
    }
}