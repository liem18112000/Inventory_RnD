package com.fromlabs.inventory.inventoryservice.ingredient;

import com.fromlabs.inventory.inventoryservice.InventoryServiceApplication;
import com.fromlabs.inventory.inventoryservice.ingredient.beans.request.IngredientRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT, classes = InventoryServiceApplication.class)
@ActiveProfiles({"dev","liem-local"} )
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class IngredientEntityTest {

    @Test
    public void setParent() {
        var ingredient = new IngredientEntity();
        var parent = new IngredientEntity();
        Assertions.assertEquals(parent, ingredient.setParent(parent));
    }

    @Test
    public void update() {
        var request = new IngredientRequest();
        request.setName("Name");
        request.setCode("Code");
        request.setDescription("Description");
        var ingredient = new IngredientEntity();
        final var updated = ingredient.update(request);
        Assertions.assertEquals("Name", updated.getName());
        Assertions.assertEquals("Code", updated.getCode());
        Assertions.assertEquals("Description", updated.getDescription());
        Assertions.assertTrue(updated.getChildren().isEmpty());
    }
}
