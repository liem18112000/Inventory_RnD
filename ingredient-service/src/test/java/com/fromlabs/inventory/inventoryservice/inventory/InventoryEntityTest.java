package com.fromlabs.inventory.inventoryservice.inventory;

import com.fromlabs.inventory.inventoryservice.InventoryServiceApplication;
import com.fromlabs.inventory.inventoryservice.inventory.beans.request.InventoryRequest;
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
class InventoryEntityTest {

    @Test
    public void equals() {
        var entity1 = new InventoryEntity();
        entity1.setId(1L);
        var entity2 = new InventoryEntity();
        entity2.setId(1L);
        Assertions.assertTrue(entity1.equals(entity2));
    }

    @Test
    public void update() {
        var request = new InventoryRequest(1L, 1L, "Name", "Description");
        var entity = new InventoryEntity();
        final var actual = entity.update(request);
        Assertions.assertEquals("Name", actual.getName());
        Assertions.assertEquals("Description", actual.getDescription());
    }

    @Test
    public void testHashCode() {
        var entity = new InventoryEntity();
        entity.setId(1L);
        Assertions.assertNotNull(entity.hashCode());
    }

}