package com.fromlabs.inventory.inventoryservice.ingredient.track.beans.request;

import com.fromlabs.inventory.inventoryservice.InventoryServiceApplication;
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
class IngredientHistoryRequestTest {

    @Test
    public void test() {
        final var request = new IngredientHistoryRequest(1L, 1L, 1L,
                "Name", "ActorName", "ActorRole",
                "Event", "Status", "Description",
                "Extra");
        Assertions.assertEquals(1L, request.getId());
        Assertions.assertEquals(1L, request.getClientId());
        Assertions.assertEquals(1L, request.getIngredientId());
        Assertions.assertEquals("ActorName", request.getActorName());
        Assertions.assertEquals("ActorRole", request.getActorRole());
        Assertions.assertEquals("Event", request.getEvent());
        Assertions.assertEquals("Status", request.getEventStatus());
    }

}