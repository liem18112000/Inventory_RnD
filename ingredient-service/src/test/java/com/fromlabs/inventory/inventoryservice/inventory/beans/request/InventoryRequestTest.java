package com.fromlabs.inventory.inventoryservice.inventory.beans.request;

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
class InventoryRequestTest {

    @Test
    public void test() {
        var request = new InventoryRequest(1L, 1L, "Name", "Description");
        Assertions.assertEquals(1L, request.getId());
        Assertions.assertEquals(1L, request.getClientId());
        Assertions.assertEquals("Name", request.getName());
        Assertions.assertEquals("Description", request.getDescription());
    }

}