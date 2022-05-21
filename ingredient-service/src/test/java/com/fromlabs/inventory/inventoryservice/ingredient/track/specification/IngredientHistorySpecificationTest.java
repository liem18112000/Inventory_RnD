package com.fromlabs.inventory.inventoryservice.ingredient.track.specification;

import com.fromlabs.inventory.inventoryservice.InventoryServiceApplication;
import com.fromlabs.inventory.inventoryservice.ingredient.IngredientEntity;
import com.fromlabs.inventory.inventoryservice.ingredient.event.IngredientEventEntity;
import com.fromlabs.inventory.inventoryservice.ingredient.track.beans.request.IngredientHistoryPageRequest;
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
class IngredientHistorySpecificationTest {

    @Test
    void filter() {
        var request = new IngredientHistoryPageRequest();
        var ingredient = new IngredientEntity();
        var event = new IngredientEventEntity();
        final var spec = IngredientHistorySpecification
                .filter(request, ingredient, event);
        Assertions.assertNotNull(spec);
    }
}