package com.fromlabs.inventory.inventoryservice.ingredient.event;

import com.fromlabs.inventory.inventoryservice.InventoryServiceApplication;
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
class IngredientEventEntityTest {

    @Test
    void testEquals() {
        var entity1 = new IngredientEventEntity();
        entity1.setId(1L);
        var entity2 = new IngredientEventEntity();
        entity2.setId(1L);
        Assertions.assertTrue(entity1.equals(entity2));
    }
}