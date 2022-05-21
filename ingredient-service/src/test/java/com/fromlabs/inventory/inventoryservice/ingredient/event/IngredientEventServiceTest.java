/*
 * Copyright (c) 2021. Liem Doan
 */

package com.fromlabs.inventory.inventoryservice.ingredient.event;

import com.fromlabs.inventory.inventoryservice.InventoryServiceApplication;
import com.fromlabs.inventory.inventoryservice.ingredient.event.status.IngredientEventStatus;
import com.fromlabs.inventory.inventoryservice.ingredient.event.status.IngredientEventStatusService;
import com.fromlabs.inventory.inventoryservice.ingredient.event.status.mapper.IngredientEventStatusMapper;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT, classes = InventoryServiceApplication.class)
@ActiveProfiles({"dev","liem-local"} )
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class IngredientEventServiceTest {

    @Mock
    private IngredientEventRepository repository;

    @Autowired
    private IngredientEventStatusService statusService;

    @Test
    void getByName() {
        var event = new IngredientEventEntity();
        event.setClientId(1L);
        event.setName("Name");
        Mockito.when(repository.findByClientIdAndName(
                event.getClientId(), event.getName()))
                .thenReturn(event);
        final var service = new IngredientEventServiceImpl(
                statusService, repository);
        final var actual = service.getByName(
                event.getClientId(), event.getName());
        Assertions.assertEquals(event.getClientId(), actual.getClientId());
        Assertions.assertEquals(event.getName(), actual.getName());
    }

    @Test
    void getAll() {
        var event = new IngredientEventEntity();
        event.setClientId(1L);
        Mockito.when(repository.findAllByClientId(
                        event.getClientId()))
                .thenReturn(List.of(event));
        final var service = new IngredientEventServiceImpl(
                statusService, repository);
        final var actuals = service.getAll(
                event.getClientId());
        actuals.forEach(actual -> {
            Assertions.assertEquals(event.getClientId(), actual.getClientId());
            Assertions.assertEquals(event.getName(), actual.getName());
        });
    }

    @Test
    void getAllStatus() {
        Assertions.assertFalse(statusService.getAll().isEmpty());
    }
}