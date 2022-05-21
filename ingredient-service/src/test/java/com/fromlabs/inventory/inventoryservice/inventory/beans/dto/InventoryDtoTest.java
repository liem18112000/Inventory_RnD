package com.fromlabs.inventory.inventoryservice.inventory.beans.dto;

import com.fromlabs.inventory.inventoryservice.InventoryServiceApplication;
import com.fromlabs.inventory.inventoryservice.ingredient.beans.dto.IngredientDto;
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
class InventoryDtoTest {

    @Test
    public void test() {
        final var dto = InventoryDto.builder()
                .id(1L).clientId(1L)
                .ingredient(IngredientDto.builder().build())
                .name("Name").description("Description")
                .quantity(1f).reserved(1f).available(1f)
                .unit("Unit").unitType("UnitType")
                .build();
        Assertions.assertEquals(1L, dto.getId());
        Assertions.assertEquals(1L, dto.getClientId());
        Assertions.assertNotNull(dto.getIngredient());
        Assertions.assertEquals("Name", dto.getName());
        Assertions.assertEquals("Description", dto.getDescription());
        Assertions.assertEquals(1f, dto.getAvailable());
        Assertions.assertEquals(1f, dto.getQuantity());
        Assertions.assertEquals(1f, dto.getReserved());
        Assertions.assertEquals("Unit", dto.getUnit());
        Assertions.assertEquals("UnitType", dto.getUnitType());
    }
}