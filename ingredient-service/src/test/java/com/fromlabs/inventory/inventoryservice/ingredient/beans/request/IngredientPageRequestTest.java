package com.fromlabs.inventory.inventoryservice.ingredient.beans.request;

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
class IngredientPageRequestTest {

    @Test
    void testEquals() {
        var request = new IngredientPageRequest();
        request.setClientId(1L);
        request.setSort("id,asc");
        var equalRequest = new IngredientPageRequest();
        equalRequest.setClientId(1L);
        equalRequest.setSort("id,asc");
        Assertions.assertTrue(request.equals(equalRequest));
    }

    @Test
    void testHashCode() {
        var request = new IngredientPageRequest();
        request.setClientId(1L);
        assertEquals(0, request.hashCode());
    }
}