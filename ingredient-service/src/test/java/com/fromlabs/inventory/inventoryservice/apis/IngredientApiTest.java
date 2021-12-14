/*
 * Copyright (c) 2021. Liem Doan
 */

package com.fromlabs.inventory.inventoryservice.apis;

import org.junit.jupiter.api.*;

/*
TODO : Quality control implement the API tests
 */
/**
 * Test runner for api testing
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class IngredientApiTest {

    //<editor-fold desc="SETUP">
    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }
    //</editor-fold>

    //<editor-fold desc="INGREDIENT">

    //<editor-fold desc="CATEGORY">
    @Test
    void getPageIngredientCategory() {
    }

    @Test
    void getAllIngredientCategory() {
    }
    //</editor-fold>

    //<editor-fold desc="TYPE">
    @Test
    void getPageIngredientType() {
    }

    @Test
    void getAllIngredientType() {
    }
    //</editor-fold>

    //<editor-fold desc="GENERAL">
    @Test
    void getIngredient() {
    }

    @Test
    void testGetIngredient() {
    }

    @Test
    void saveIngredient() {
    }

    @Test
    void updateIngredient() {
    }

    @Test
    void deleteIngredient() {
    }
    //</editor-fold>

    //<editor-fold desc="CONFIG">
    @Test
    void updateConfig() {
    }
    //</editor-fold>

    //</editor-fold>

    //<editor-fold desc="INVENTORY">

    //<editor-fold desc="CRUD">
    @Test
    void getPageInventory() {
    }

    @Test
    void getAllInventory() {
    }

    @Test
    void updateInventory() {
    }

    @Test
    void getInventoryById() {
    }
    //</editor-fold>

    //<editor-fold desc="SYNC">
    @Test
    void syncInventory() {
    }

    @Test
    void syncAllIngredientInInventory() {
    }
    //</editor-fold>

    //</editor-fold>

    //<editor-fold desc="ITEM">
    @Test
    void getItemById() {
    }

    @Test
    void saveItem() {
    }

    @Test
    void updateItem() {
    }

    @Test
    void deleteItem() {
    }

    @Test
    void deleteAllItems() {
    }
    //</editor-fold>
}