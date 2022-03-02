/*
 * Copyright (c) 2021. Liem Doan
 */

package com.fromlabs.inventory.inventoryservice.ingredient.mapper;

import com.fromlabs.inventory.inventoryservice.ingredient.IngredientEntity;
import com.fromlabs.inventory.inventoryservice.ingredient.beans.dto.IngredientDto;
import com.fromlabs.inventory.inventoryservice.ingredient.beans.request.*;
import lombok.experimental.UtilityClass;
import org.springframework.data.domain.Page;
import org.springframework.util.StringUtils;

import javax.validation.constraints.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.fromlabs.inventory.inventoryservice.common.factory.FactoryCreateType.*;
import static com.fromlabs.inventory.inventoryservice.ingredient.factory.IngredientEntityFactory.create;
import static java.util.Objects.*;

/**
 * Ingredient mapper for convert DTO to entity , Request to entity and vice versa
 * @author Liem
 */
@UtilityClass
public class IngredientMapper {

    //<editor-fold desc="CONVERT TO ENTITY">

    /**
     * Covert to entity from page request
     * @param request   IngredientPageRequest
     * @return          IngredientEntity
     */
    public IngredientEntity toEntity(
            @NotNull final IngredientPageRequest request
    ) {
        var entity = create(EMPTY);
        entity.setClientId(request.getClientId());
        entity.setName(request.getName());
        entity.setCode(request.getCode());
        entity.setDescription(request.getDescription());
        entity.setUnit(request.getUnit());
        entity.setUnitType(request.getUnitType());
        entity.setCreateAt(request.getCreateAt());
        entity.setUpdateAt(request.getUpdateAt());
        entity.setCategory(getCategoryFlagFromRequest(request));
        return entity;
    }

    /**
     * Convert request to entity
     * @param request   IngredientRequest
     * @return          IngredientEntity
     */
    public IngredientEntity toEntity(
            @NotNull final IngredientRequest request
    ) {
        var entity = create(DEFAULT);
        entity.setId(request.getId());
        entity.setClientId(request.getClientId());
        entity.setName(request.getName());
        entity.setCode(request.getCode());
        entity.setDescription(getDescriptionFromRequest(request));
        entity.setUnit(request.getUnit());
        entity.setUnitType(request.getUnitType());
        entity.setCategory(isNull(request.getParentId()));
        return entity;
    }

    /**
     * Convert dto back to entity
     * @param dto   IngredientDto
     * @return  IngredientEntity
     */
    public IngredientEntity toEntity(
            @NotNull final IngredientDto dto
    ) {
        var entity = new IngredientEntity();
        entity.setId(dto.getId());
        entity.setClientId(dto.getClientId());
        entity.setName(dto.getName());
        entity.setCode(dto.getCode());
        entity.setDescription(dto.getDescription());
        entity.setUnit(dto.getUnit());
        entity.setUnitType(dto.getUnitType());
        entity.setChildren(getChildrenDto(dto)) ;
        entity.setCreateAt(dto.getCreateAt());
        entity.setUpdateAt(dto.getUpdateAt());
        return entity;
    }

    //</editor-fold>

    //<editor-fold desc="COVERT TO DTO">

    /**
     * Convert ingredient entity to ingredient dto
     * @param entity    IngredientEntity
     * @return          IngredientDto
     */
    public IngredientDto toDto(
            final IngredientEntity entity
    ){
        if (Objects.isNull(entity)) {
            return null;
        }
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
     * @return          List&lt;IngredientDto&gt;
     */
    public List<IngredientDto> toDto(
            @NotNull final List<IngredientEntity> entities
    ) {
        return entities.stream().map(IngredientMapper::toDto).collect(Collectors.toList());
    }

    /**
     * Convert page of ingredient entity to page of ingredient dto
     * @param entities  Page&lt;IngredientEntity&gt;
     * @return          Page&lt;IngredientDto&gt;
     */
    public Page<IngredientDto> toDto(
            @NotNull final Page<IngredientEntity> entities
    ) {
        return entities.map(IngredientMapper::toDto);
    }

    //</editor-fold>

    //<editor-fold desc="UTILITIES">

    /**
     * Get description from request
     * @param request   IngredientRequest
     * @return          String
     */
    private String getDescriptionFromRequest(
            @NotNull final IngredientRequest request
    ) {
        return StringUtils.hasLength(request.getDescription()) ?
                request.getDescription() : request.getName();
    }

    /**
     * Get category flag from page request
     * @param request   IngredientPageRequest
     * @return          boolean
     */
    private boolean getCategoryFlagFromRequest(
            @NotNull final IngredientPageRequest request
    ) {
        return isNull(request.getParentId()) || request.getParentId().equals(-1L);
    }

    /**
     * Get children DTO from children entities
     * @param dto   IngredientDto
     * @return      list of ingredient entity
     */
    private List<IngredientEntity> getChildrenDto(
            @NotNull final IngredientDto dto
    ) {
        return  nonNull(dto.getChildren()) ?
                dto.getChildren().stream().map(IngredientMapper::toEntity).collect(Collectors.toList()) :
                new ArrayList<>();
    }

    //</editor-fold>

}
