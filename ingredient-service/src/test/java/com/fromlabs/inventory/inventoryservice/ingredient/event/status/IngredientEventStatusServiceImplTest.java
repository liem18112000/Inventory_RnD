package com.fromlabs.inventory.inventoryservice.ingredient.event.status;

import com.fromlabs.inventory.inventoryservice.InventoryServiceApplication;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT, classes = InventoryServiceApplication.class)
@ActiveProfiles({"dev","liem-local"} )
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class IngredientEventStatusServiceImplTest {

    @Autowired
    private IngredientEventStatusServiceImpl service;

    @Test
    void getAll() {
        Assertions.assertFalse(service.getAll().isEmpty());
    }
}