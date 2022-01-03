package com.fromlabs.inventory.recipeservice.recipe.mapper;

import com.fromlabs.inventory.recipeservice.recipe.RecipeEntity;
import com.fromlabs.inventory.recipeservice.recipe.beans.dto.RecipeDto;
import com.fromlabs.inventory.recipeservice.recipe.beans.request.RecipePageRequest;
import com.fromlabs.inventory.recipeservice.recipe.beans.request.RecipeRequest;
import lombok.experimental.UtilityClass;
import org.springframework.data.domain.Page;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.fromlabs.inventory.recipeservice.common.factory.FactoryCreateType.EMPTY;
import static com.fromlabs.inventory.recipeservice.recipe.factory.RecipeEntityFactory.create;

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

    //<editor-fold desc="CONVERT TO ENTITY">

    public RecipeEntity toEntity(@NotNull final RecipePageRequest request) {
        var entity = create(EMPTY);
        entity.setClientId(request.getTenantId());
        entity.setName(request.getName());
        entity.setCode(request.getCode());
        entity.setDescription(request.getDescription());
        entity.setGroup(RecipeEntity.checkIsGroupFromPageRequest(request));
        return entity;
    }

    public RecipeEntity toEntity(RecipeRequest request) {
        var entity = create(EMPTY);
        entity.setClientId(request.getTenantId());
        entity.setName(request.getName());
        entity.setCode(request.getCode());
        entity.setDescription(request.getDescription());
        entity.setGroup(Objects.isNull(request.getParentId()));
        entity.setUpdateAt(request.getCreatedAt());
        return entity;
    }

    //</editor-fold>
}
