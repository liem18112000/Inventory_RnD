package com.fromlabs.inventory.recipeservice.recipe.mapper;

import com.fromlabs.inventory.recipeservice.recipe.RecipeEntity;
import com.fromlabs.inventory.recipeservice.recipe.beans.dto.RecipeDto;
import lombok.experimental.UtilityClass;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Recipe mapper
 * @author Liem
 */
@UtilityClass
public class RecipeMapper {

    //<editor-fold desc="CONVERT TO DTO">

    /**
     * Convert recipe entity to recipe DTO
     * @param entity    RecipeEntity
     * @return          RecipeDto
     */
    public RecipeDto toDto(RecipeEntity entity) {
        var builder = RecipeDto.recipeBuilder()
                .id(entity.getId())
                .tenantId(entity.getClientId())
                .name(entity.getName())
                .code(entity.getCode())
                .description(entity.getDescription())
                .createAt(entity.getUpdateAt())
                .updateAt(entity.getUpdateAt())
                .activated(entity.isActivated());
        return Objects.nonNull(entity.getParent()) ? builder.build() :
                builder.children(toDto(entity.getChildren())).build();
    }

    /**
     * Convert recipe entity list to recipe DTO list.
     * @param entities  List of recipe entity.
     * @return          List of recipe DTO.
     */
    public List<RecipeDto> toDto(List<RecipeEntity> entities) {
        return entities.stream().map(RecipeMapper::toDto).collect(Collectors.toList());
    }

    /**
     * Convert recipe entity page to recipe DTO page
     * @param entities  Page of recipe entity
     * @return          Page of recipe DTO
     */
    public Page<RecipeDto> toDto(Page<RecipeEntity> entities) {
        return entities.map(RecipeMapper::toDto);
    }

    //</editor-fold>
}
