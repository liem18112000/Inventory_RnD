package com.fromlabs.inventory.inventoryservice.inventory.beans;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fromlabs.inventory.inventoryservice.ingredient.IngredientEntity;
import com.fromlabs.inventory.inventoryservice.ingredient.beans.IngredientDto;
import com.fromlabs.inventory.inventoryservice.inventory.InventoryEntity;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Inventory DTO
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder(toBuilder = true)
@Data
public class InventoryDto {
    private Long id;
    private Long clientId;
    private IngredientDto ingredient;
    private String name;
    private String description;
    private Float quantity;
    private Float reserved;
    private Float available;
    private String unit;
    private String unitType;

    /**
     * Convert inventory entity to inventory dto
     * @param entity InventoryEntity
     * @return InventoryDto
     */
    static public InventoryDto from(InventoryEntity entity) {
        return InventoryDto.builder()
                .id(entity.getId())
                .clientId(entity.getClientId())
                .ingredient(IngredientDto.from(entity.getIngredient()))
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
    static public List<InventoryDto> from(List<InventoryEntity> entities) {
        return entities.stream().map(InventoryDto::from).collect(Collectors.toList());
    }

    /**
     * Convert page of inventory entity to page of inventory dto
     * @param entities Page&lt;InventoryEntity&gt;
     * @return Page&lt;InventoryDto&gt;
     */
    static public Page<InventoryDto> from(Page<InventoryEntity> entities) {
        return entities.map(InventoryDto::from);
    }
}
