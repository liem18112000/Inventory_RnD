/*
 * Copyright (c) 2021. Liem Doan
 */

package com.fromlabs.inventory.inventoryservice.inventory.mapper;

import com.fromlabs.inventory.inventoryservice.ingredient.mapper.IngredientMapper;
import com.fromlabs.inventory.inventoryservice.inventory.InventoryEntity;
import com.fromlabs.inventory.inventoryservice.inventory.beans.dto.InventoryDto;
import com.fromlabs.inventory.inventoryservice.inventory.beans.request.InventoryPageRequest;
import lombok.experimental.UtilityClass;
import org.springframework.data.domain.Page;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class InventoryMapper {

    //<editor-fold desc="CONVERT TO DTO">

    /**
     * Convert inventory entity to inventory dto
     * @param entity InventoryEntity
     * @return InventoryDto
     */
    public InventoryDto toDto(InventoryEntity entity) {
        return InventoryDto.builder()
                .id(entity.getId())
                .clientId(entity.getClientId())
                .ingredient(IngredientMapper.toDto(entity.getIngredient()))
                .name(entity.getName())
                .description(entity.getDescription())
                .quantity(entity.getQuantity())
                .reserved(entity.getReserved())
                .available(Math.max(entity.getQuantity() - entity.getReserved(), 0f))
                .unit(entity.getUnit())
                .unitType(entity.getUnitType())
                .build();
    }

    /**
     * Convert list of inventory entity to list of inventory dto
     * @param entities List&lt;InventoryEntity&gt;
     * @return List&lt;InventoryDto&gt;
     */
    public List<InventoryDto> toDto(List<InventoryEntity> entities) {
        return entities.stream().map(InventoryMapper::toDto).collect(Collectors.toList());
    }

    /**
     * Convert page of inventory entity to page of inventory dto
     * @param entities Page&lt;InventoryEntity&gt;
     * @return Page&lt;InventoryDto&gt;
     */
    public Page<InventoryDto> toDto(Page<InventoryEntity> entities) {
        return entities.map(InventoryMapper::toDto);
    }

    //</editor-fold>

    //<editor-fold desc="CONVERT TO ENTITY">

    public InventoryEntity toEntity(
            @NotNull final InventoryPageRequest request
    ) {
        var entity = new InventoryEntity();
        entity.setClientId(request.getClientId());
        entity.setName(request.getName());
        entity.setDescription(request.getDescription());
        entity.setUnit(request.getUnit());
        entity.setUnitType(request.getUnitType());
        entity.setUpdateAt(request.getUpdateAt());
        return entity;
    }

    //</editor-fold>
}
