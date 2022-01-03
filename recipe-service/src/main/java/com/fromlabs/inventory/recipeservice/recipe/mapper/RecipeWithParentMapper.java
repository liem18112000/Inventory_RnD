package com.fromlabs.inventory.recipeservice.recipe.mapper;

import com.fromlabs.inventory.recipeservice.common.dto.SimpleDto;
import com.fromlabs.inventory.recipeservice.recipe.RecipeEntity;
import com.fromlabs.inventory.recipeservice.recipe.beans.dto.RecipeWithParentDto;
import lombok.experimental.UtilityClass;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.Objects.nonNull;

/**
 * Recipe wih parent mapper
 * @author Liem
 */
@UtilityClass
public class RecipeWithParentMapper {

    public RecipeWithParentDto toDto(RecipeEntity entity) {
        final var parent = entity.getParent();
        return RecipeWithParentDto.recipeWithParentBuilder()
                .id(entity.getId())
                .tenantId(entity.getClientId())
                .name(entity.getName())
                .code(entity.getCode())
                .description(entity.getDescription())
                .createAt(entity.getUpdateAt())
                .updateAt(entity.getUpdateAt())
                .activated(entity.isActivated())
                .children(nonNull(entity.getChildren()) ? RecipeMapper.toDto(entity.getChildren()) : null)
                .parent(nonNull(parent) ? SimpleDto.builder().label(parent.getName()).value(parent.getId()).build() : null)
                .build();
    }

    public List<RecipeWithParentDto> toDto(List<RecipeEntity> entities) {
        return entities.stream().map(RecipeWithParentMapper::toDto).collect(Collectors.toList());
    }

    public Page<RecipeWithParentDto> toDto(Page<RecipeEntity> entities) {
        return entities.map(RecipeWithParentMapper::toDto);
    }
}
