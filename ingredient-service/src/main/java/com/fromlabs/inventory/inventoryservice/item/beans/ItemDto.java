package com.fromlabs.inventory.inventoryservice.item.beans;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fromlabs.inventory.inventoryservice.ingredient.beans.IngredientDto;
import com.fromlabs.inventory.inventoryservice.item.ItemEntity;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Item Dto
 */
@Builder(toBuilder = true)
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ItemDto {
    private Long id;
    private Long clientId;
    private IngredientDto ingredient;
    private Long importId;
    private String name;
    private String code;
    private String description;
    private String unit;
    private String unitType;
    private String expiredAt;

    /**
     * Convert item entity to DTO
     * @param entity ItemEntity
     * @return ItemDto
     */
    static public ItemDto from(ItemEntity entity) {
        return ItemDto.builder()
                .id(entity.getId())
                .clientId(entity.getClientId())
                .importId(entity.getImportId())
                .ingredient(IngredientDto.from(entity.getIngredient()))
                .code(entity.getCode())
                .name(entity.getName())
                .description(entity.getDescription())
                .unit(entity.getUnit())
                .unitType(entity.getUnitType())
                .expiredAt(entity.getExpiredAt())
                .build();
    }

    /**
     * Convert list of item entity to list DTO
     * @param entities List&lt;ItemEntity&gt;
     * @return List&lt;ItemDto&gt;
     */
    public static List<ItemDto> from(List<ItemEntity> entities) {
        return entities.stream().map(ItemDto::from).collect(Collectors.toList());
    }

    /**
     * Convert list of item entity to list DTO
     * @param entities Page&lt;ItemEntity&gt;
     * @return Page&lt;ItemDto&gt;
     */
    public static Page<ItemDto> from(Page<ItemEntity> entities) {
        return entities.map(ItemDto::from);
    }
}
