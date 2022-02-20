/*
 * Copyright (c) 2021. Liem Doan
 */

package com.fromlabs.inventory.inventoryservice.item.mapper;

import com.fromlabs.inventory.inventoryservice.client.supplier.SupplierClient;
import com.fromlabs.inventory.inventoryservice.ingredient.mapper.IngredientMapper;
import com.fromlabs.inventory.inventoryservice.item.ItemEntity;
import com.fromlabs.inventory.inventoryservice.item.beans.dto.ItemDto;
import com.fromlabs.inventory.inventoryservice.item.beans.request.ItemPageRequest;
import com.fromlabs.inventory.inventoryservice.item.beans.request.ItemRequest;
import lombok.experimental.UtilityClass;
import org.springframework.data.domain.Page;

import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Item mapper
 * @author Liem
 */
@UtilityClass
public class ItemMapper {

    //<editor-fold desc="CONVERT TO ENTITY">

    /**
     * Convert item request to entity
     * @param request   ItemRequest
     * @return          ItemEntity
     */
    public ItemEntity toEntity(
            @NotNull final ItemRequest request
    ) {
        var item = new ItemEntity();
        item.setId(request.getId());
        item.setClientId(request.getClientId());
        item.setCode(request.getCode());
        item.setName(request.getName());
        item.setDescription(request.getDescription());
        item.setUnit(request.getUnit());
        item.setUnitType(request.getUnitType());
        item.setExpiredAt(request.getExpiredAt());
        item.setUpdateAt(Instant.now().toString());
        return item;
    }

    /**
     * Convert item page request to entity
     * @param request   ItemPageRequest
     * @return          ItemEntity
     */
    public ItemEntity toEntity(
            @NotNull final ItemPageRequest request
    ) {
        var item = new ItemEntity();
        item.setClientId(request.getClientId());
        item.setCode(request.getCode());
        item.setName(request.getName());
        item.setDescription(request.getDescription());
        item.setUnit(request.getUnit());
        item.setUnitType(request.getUnitType());
        item.setExpiredAt(request.getExpiredAt());
        item.setUpdateAt(request.getUpdateAt());
        return item;
    }

    //</editor-fold>

    //<editor-fold desc="CONVERT TO REQUEST">

    /**
     * Convert item entity to item request
     * @return  ItemRequest
     */
    public ItemRequest toRequest(
            @NotNull final ItemEntity entity
    ){
        var request = new ItemRequest();
        request.setId(entity.getId());
        request.setClientId(entity.getClientId());
        request.setCode(entity.getCode());
        request.setDescription(entity.getDescription());
        request.setExpiredAt(entity.getExpiredAt());
        request.setName(entity.getName());
        request.setUnit(entity.getUnit());
        request.setUnitType(entity.getUnitType());
        request.setUpdateAt(entity.getUpdateAt());
        request.setImportId(entity.getImportId());
        request.setIngredientId(entity.getIngredient().getId());
        return request;
    }

    //</editor-fold>

    //<editor-fold desc="CONVERT TO DTO">

    /**
     * Convert item entity to DTO
     * @param entity ItemEntity
     * @return ItemDto
     */
    public ItemDto toDto(
            @NotNull final ItemEntity entity,
            @NotNull final SupplierClient supplierClient
    ) {
        return ItemDto.builder()
                .id(entity.getId())
                .clientId(entity.getClientId())
                .imports(supplierClient.getImportById(entity.getImportId()))
                .ingredient(IngredientMapper.toDto(entity.getIngredient()))
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
    public List<ItemDto> toDto(
            @NotNull final List<ItemEntity> entities,
            @NotNull final SupplierClient supplierClient
    ) {
        return entities.stream().map(entity -> ItemMapper.toDto(entity, supplierClient))
                .collect(Collectors.toList());
    }

    /**
     * Convert list of item entity to list DTO
     * @param entities Page&lt;ItemEntity&gt;
     * @return Page&lt;ItemDto&gt;
     */
    public Page<ItemDto> toDto(
            @NotNull final Page<ItemEntity> entities,
            @NotNull final SupplierClient supplierClient
    ) {
        return entities.map(entity -> ItemMapper.toDto(entity, supplierClient));
    }

    //</editor-fold>
}
