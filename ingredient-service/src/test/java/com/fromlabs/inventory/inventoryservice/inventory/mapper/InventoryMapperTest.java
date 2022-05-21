package com.fromlabs.inventory.inventoryservice.inventory.mapper;

import com.fromlabs.inventory.inventoryservice.InventoryServiceApplication;
import com.fromlabs.inventory.inventoryservice.ingredient.IngredientEntity;
import com.fromlabs.inventory.inventoryservice.ingredient.beans.dto.IngredientDto;
import com.fromlabs.inventory.inventoryservice.inventory.InventoryEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT, classes = InventoryServiceApplication.class)
@ActiveProfiles({"dev","liem-local"} )
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class InventoryMapperTest {

    @Test
    void toDto() {
        var entity = new InventoryEntity();
        entity.setId(1L);
        entity.setClientId(1L);
        entity.setName("Name");
        entity.setDescription("Description");
        entity.setIngredient(new IngredientEntity());
        entity.setQuantity(2f);
        entity.setReserved(1f);
        entity.setUnit("Unit");
        entity.setUnitType("UnitType");

        final var actual = InventoryMapper.toDto(entity);

        Assertions.assertEquals(1L, actual.getId());
        Assertions.assertEquals(1L, actual.getClientId());
        Assertions.assertEquals("Name", actual.getName());
        Assertions.assertEquals("Description", actual.getDescription());
        Assertions.assertEquals(2f, actual.getQuantity());
        Assertions.assertEquals(1f, actual.getReserved());
        Assertions.assertEquals(1f, actual.getAvailable());
        Assertions.assertEquals(2f, actual.getQuantity());
        Assertions.assertEquals("Unit", actual.getUnit());
        Assertions.assertEquals("UnitType", actual.getUnitType());
    }

    @Test
    void toDtoList() {

        var entity = new InventoryEntity();
        entity.setId(1L);
        entity.setClientId(1L);
        entity.setName("Name");
        entity.setDescription("Description");
        entity.setIngredient(new IngredientEntity());
        entity.setQuantity(2f);
        entity.setReserved(1f);
        entity.setUnit("Unit");
        entity.setUnitType("UnitType");

        final var actuals = InventoryMapper.toDto(List.of(entity));

        actuals.forEach(actual -> {
            Assertions.assertEquals(1L, actual.getId());
            Assertions.assertEquals(1L, actual.getClientId());
            Assertions.assertEquals("Name", actual.getName());
            Assertions.assertEquals("Description", actual.getDescription());
            Assertions.assertEquals(2f, actual.getQuantity());
            Assertions.assertEquals(1f, actual.getReserved());
            Assertions.assertEquals(1f, actual.getAvailable());
            Assertions.assertEquals(2f, actual.getQuantity());
            Assertions.assertEquals("Unit", actual.getUnit());
            Assertions.assertEquals("UnitType", actual.getUnitType());
        });
    }

    @Test
    void toDtoPage() {

        var entity = new InventoryEntity();
        entity.setId(1L);
        entity.setClientId(1L);
        entity.setName("Name");
        entity.setDescription("Description");
        entity.setIngredient(new IngredientEntity());
        entity.setQuantity(2f);
        entity.setReserved(1f);
        entity.setUnit("Unit");
        entity.setUnitType("UnitType");

        final var actuals = InventoryMapper.toDto(new PageImpl<>(List.of(entity)));
        actuals.forEach(actual -> {
            Assertions.assertEquals(1L, actual.getId());
            Assertions.assertEquals(1L, actual.getClientId());
            Assertions.assertEquals("Name", actual.getName());
            Assertions.assertEquals("Description", actual.getDescription());
            Assertions.assertEquals(2f, actual.getQuantity());
            Assertions.assertEquals(1f, actual.getReserved());
            Assertions.assertEquals(1f, actual.getAvailable());
            Assertions.assertEquals(2f, actual.getQuantity());
            Assertions.assertEquals("Unit", actual.getUnit());
            Assertions.assertEquals("UnitType", actual.getUnitType());
        });
    }
}