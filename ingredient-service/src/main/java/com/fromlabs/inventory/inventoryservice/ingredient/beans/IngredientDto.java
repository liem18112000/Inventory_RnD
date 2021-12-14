package com.fromlabs.inventory.inventoryservice.ingredient.beans;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fromlabs.inventory.inventoryservice.ingredient.IngredientEntity;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.Page;

import javax.persistence.Column;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Ingredient Data Transfer Object
 */
@Builder(toBuilder = true)
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class IngredientDto {
    private Long id;
    private Long clientId;
    private String name;
    private String code;
    private String description;
    private List<IngredientDto> children;
    private String unit;
    private String unitType;
    private Float quantity;
    private String createAt;
    private String updateAt;

    /**
     * Convert ingredient entity to ingredient dto
     * @param entity    IngredientEntity
     * @return          IngredientDto
     * @see IngredientEntity
     */
    static public IngredientDto from(IngredientEntity entity){
        var dto = IngredientDto.builder()
                .id(entity.getId())
                .clientId(entity.getClientId())
                .name(entity.getName())
                .code(entity.getCode())
                .unitType(entity.getUnitType())
                .unit(entity.getUnit())
                .createAt(entity.getCreateAt())
                .updateAt(entity.getUpdateAt())
                .description(entity.getDescription());
        return entity.isCategory() ? dto.build() : dto.unit(entity.getUnit()).unitType(entity.getUnitType()).build();
    }

    /**
     * Convert list of ingredient entity to list of ingredient dto
     * @param entities  List&lt;IngredientEntity&gt;
     * @return List&lt;IngredientDto&gt;
     * @see IngredientEntity
     */
    static public List<IngredientDto> from(List<IngredientEntity> entities) {
        return entities.stream().map(IngredientDto::from).collect(Collectors.toList());
    }

    /**
     * Convert page of ingredient entity to page of ingredient dto
     * @param entities  List&lt;IngredientEntity&gt;
     * @return IngredientEntity
     * @see Page
     */
    static public Page<IngredientDto> from(Page<IngredientEntity> entities) {
        return entities.map(IngredientDto::from);
    }
}
