/*
 * Copyright (c) 2021. Liem Doan
 */

package com.fromlabs.inventory.inventoryservice.ingredient.track;

import com.fromlabs.inventory.inventoryservice.InventoryServiceApplication;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT, classes = InventoryServiceApplication.class)
@ActiveProfiles({"dev","liem-local"} )
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class IngredientHistoryServiceTest {

    @Mock
    private IngredientHistoryRepository repository;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void getByIdWithException() {
        var entity = new IngredientHistoryEntity();
        entity.setId(1L);
        var service = new IngredientHistoryServiceImpl(this.repository);
        Mockito.when(this.repository.findById(entity.getId()))
                .thenReturn(Optional.of(entity));
        final var getEntity = Assertions.assertDoesNotThrow(() ->
                service.getByIdWithException(entity.getId()));
        Assertions.assertEquals(entity.getId(), getEntity.getId());
    }

    @Test
    void getByIngredient() {
    }

    @Test
    void getAll() {
    }
}