package com.fromlabs.inventory.inventoryservice.ingredient.config;

import com.fromlabs.inventory.inventoryservice.InventoryServiceApplication;
import com.fromlabs.inventory.inventoryservice.ingredient.IngredientEntity;
import com.fromlabs.inventory.inventoryservice.ingredient.beans.request.IngredientRequest;
import com.fromlabs.inventory.inventoryservice.ingredient.config.beans.request.IngredientConfigRequest;
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
public class IngredientConfigEntityTest {

    @Test
    public void update() {
        var request = new IngredientConfigRequest();
        request.setClientId(1L);
        request.setMaximumQuantity(10000f);
        request.setMinimumQuantity(10f);
        var config = new IngredientConfigEntity();
        final var updated = config.update(request);
        Assertions.assertEquals(1L, updated.getClientId());
        Assertions.assertEquals(10000f, updated.getMaximumQuantity());
        Assertions.assertEquals(10f, updated.getMinimumQuantity());
    }
}
