package com.fromlabs.inventory.inventoryservice.ingredient.config.mapper;

import com.fromlabs.inventory.inventoryservice.InventoryServiceApplication;
import com.fromlabs.inventory.inventoryservice.ingredient.IngredientEntity;
import com.fromlabs.inventory.inventoryservice.ingredient.beans.dto.IngredientDto;
import com.fromlabs.inventory.inventoryservice.ingredient.beans.request.IngredientRequest;
import com.fromlabs.inventory.inventoryservice.ingredient.config.IngredientConfigEntity;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT, classes = InventoryServiceApplication.class)
@ActiveProfiles({"dev","liem-local"} )
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class IngredientConfigMapperTest {

    @Test
    public void toEntity() {
        var request = new IngredientRequest();
        request.setClientId(1L);
        request.setName("Name");
        request.setDescription("Description");
        request.setMinimumQuantity(10f);
        request.setMaximumQuantity(10000f);
        final var entity = IngredientConfigMapper.toEntity(
                request, new IngredientEntity());
        Assertions.assertEquals("Name", entity.getName());
        Assertions.assertEquals("Description", entity.getDescription());
        Assertions.assertEquals(10f, entity.getMinimumQuantity());
        Assertions.assertEquals(10000f, entity.getMaximumQuantity());
        Assertions.assertEquals(1L, entity.getClientId());
    }

    @Test
    public void toDto() {
        var entity = new IngredientConfigEntity();
        entity.setClientId(1L);
        entity.setName("Name");
        entity.setDescription("Description");
        entity.setMinimumQuantity(10f);
        entity.setMaximumQuantity(10000f);
        final var dto = IngredientConfigMapper.toDto(
                entity, IngredientDto.builder().build());
        Assertions.assertEquals("Name", dto.getName());
        Assertions.assertEquals("Description", dto.getDescription());
        Assertions.assertEquals(10f, dto.getMinimumQuantity());
        Assertions.assertEquals(10000f, dto.getMaximumQuantity());
        Assertions.assertEquals(1L, dto.getTenantId());
    }
}
