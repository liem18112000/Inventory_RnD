package com.fromlabs.inventory.inventoryservice.ingredient.mapper;

import com.fromlabs.inventory.inventoryservice.InventoryServiceApplication;
import com.fromlabs.inventory.inventoryservice.ingredient.IngredientEntity;
import com.fromlabs.inventory.inventoryservice.ingredient.beans.dto.IngredientDto;
import com.fromlabs.inventory.inventoryservice.ingredient.beans.request.IngredientRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT, classes = InventoryServiceApplication.class)
@ActiveProfiles({"dev","liem-local"} )
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class IngredientMapperTest {

    @Test
    public void toEntityFromRequest() {
        var request = new IngredientRequest();
        request.setId(2L);
        request.setClientId(1L);
        request.setName("Name");
        request.setCode("Code");
        request.setDescription("Description");
        request.setUnit("Unit");
        request.setUnitType("UnitType");
        request.setParentId(1L);
        final var entity = IngredientMapper.toEntity(request);
        Assertions.assertEquals("Name", entity.getName());
        Assertions.assertEquals("Code", entity.getCode());
        Assertions.assertEquals("Description", entity.getDescription());
        Assertions.assertEquals("Unit", entity.getUnit());
        Assertions.assertEquals("UnitType", entity.getUnitType());
        Assertions.assertEquals(1L, entity.getClientId());
        Assertions.assertEquals(2L, entity.getId());
        Assertions.assertFalse(entity.isCategory());
    }

    @Test
    public void toEntityFromDto() {
        final var dto = IngredientDto.builder()
                .id(2L)
                .name("Name")
                .code("Code")
                .description("Description")
                .unit("Unit")
                .unitType("UnitType")
                .clientId(1L)
                .build();
        final var entity = IngredientMapper.toEntity(dto);
        Assertions.assertEquals("Name", entity.getName());
        Assertions.assertEquals("Code", entity.getCode());
        Assertions.assertEquals("Description", entity.getDescription());
        Assertions.assertEquals("Unit", entity.getUnit());
        Assertions.assertEquals("UnitType", entity.getUnitType());
        Assertions.assertEquals(1L, entity.getClientId());
        Assertions.assertEquals(2L, entity.getId());
        Assertions.assertFalse(entity.isCategory());
    }

    @Test
    public void toDtoFromEntity() {
        var entity = new IngredientEntity();
        entity.setId(2L);
        entity.setClientId(1L);
        entity.setName("Name");
        entity.setCode("Code");
        entity.setDescription("Description");
        entity.setUnit("Unit");
        entity.setUnitType("UnitType");
        final var dto = IngredientMapper.toDto(entity);
        Assertions.assertEquals("Name", dto.getName());
        Assertions.assertEquals("Code", dto.getCode());
        Assertions.assertEquals("Description", dto.getDescription());
        Assertions.assertEquals("Unit", dto.getUnit());
        Assertions.assertEquals("UnitType", dto.getUnitType());
        Assertions.assertEquals(1L, dto.getClientId());
        Assertions.assertEquals(2L, dto.getId());
    }

    @Test
    public void toDtoFromEntityList() {
        var entity = new IngredientEntity();
        entity.setId(2L);
        entity.setClientId(1L);
        entity.setName("Name");
        entity.setCode("Code");
        entity.setDescription("Description");
        entity.setUnit("Unit");
        entity.setUnitType("UnitType");
        final var dtoList = IngredientMapper.toDto(List.of(entity));
        Assertions.assertNotNull(dtoList);
        Assertions.assertFalse(dtoList.isEmpty());
        dtoList.forEach(dto -> {
            Assertions.assertEquals("Name", dto.getName());
            Assertions.assertEquals("Code", dto.getCode());
            Assertions.assertEquals("Description", dto.getDescription());
            Assertions.assertEquals("Unit", dto.getUnit());
            Assertions.assertEquals("UnitType", dto.getUnitType());
            Assertions.assertEquals(1L, dto.getClientId());
            Assertions.assertEquals(2L, dto.getId());
        });
    }

    @Test
    public void toDtoFromEntityPage() {
        var entity = new IngredientEntity();
        entity.setId(2L);
        entity.setClientId(1L);
        entity.setName("Name");
        entity.setCode("Code");
        entity.setDescription("Description");
        entity.setUnit("Unit");
        entity.setUnitType("UnitType");
        final var entityPage = new PageImpl<>(List.of(entity));
        final var dtoPage = IngredientMapper.toDto(entityPage);
        Assertions.assertNotNull(dtoPage);
        Assertions.assertFalse(dtoPage.isEmpty());
        dtoPage.forEach(dto -> {
            Assertions.assertEquals("Name", dto.getName());
            Assertions.assertEquals("Code", dto.getCode());
            Assertions.assertEquals("Description", dto.getDescription());
            Assertions.assertEquals("Unit", dto.getUnit());
            Assertions.assertEquals("UnitType", dto.getUnitType());
            Assertions.assertEquals(1L, dto.getClientId());
            Assertions.assertEquals(2L, dto.getId());
        });
    }
}
