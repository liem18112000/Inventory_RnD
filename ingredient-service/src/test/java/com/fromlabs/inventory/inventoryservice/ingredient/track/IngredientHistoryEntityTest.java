package com.fromlabs.inventory.inventoryservice.ingredient.track;

import com.fromlabs.inventory.inventoryservice.InventoryServiceApplication;
import com.fromlabs.inventory.inventoryservice.ingredient.track.beans.request.IngredientHistoryRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static com.fromlabs.inventory.inventoryservice.ingredient.track.IngredientHistoryEntity.DEFAULT_ACTOR_ROLE;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT, classes = InventoryServiceApplication.class)
@ActiveProfiles({"dev","liem-local"} )
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class IngredientHistoryEntityTest {

    @Test
    void update() {
        final var request = new IngredientHistoryRequest(
                1L, 1L, 1L, "Name",
                "ActorName", "ActorRole", "Event",
                "EventStatus", "Description", "Extra"
        );

        var track = new IngredientHistoryEntity();
        final var updated = track.update(request);
        Assertions.assertEquals(request.getName(), updated.getName());
        Assertions.assertEquals(request.getDescription(), updated.getDescription());
        Assertions.assertEquals(request.getExtraInformation(), updated.getExtraInformation());
        Assertions.assertEquals(DEFAULT_ACTOR_ROLE, updated.getActorName());
        Assertions.assertEquals(DEFAULT_ACTOR_ROLE, updated.getActorRole());
        Assertions.assertEquals("Extra", updated.getExtraInformation());
        Assertions.assertNull(updated.getEvent());
        Assertions.assertNull(updated.getEventStatus());
        Assertions.assertEquals("ItemRequest", updated.getExtraInformationClass());
    }
}