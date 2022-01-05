package com.fromlabs.inventory.supplierservice.supplier.providable_material.mapper;

import com.fromlabs.inventory.supplierservice.client.ingredient.IngredientClient;
import com.fromlabs.inventory.supplierservice.supplier.SupplierEntity;
import com.fromlabs.inventory.supplierservice.supplier.mapper.SupplierMapper;
import com.fromlabs.inventory.supplierservice.supplier.providable_material.ProvidableMaterialEntity;
import com.fromlabs.inventory.supplierservice.supplier.providable_material.beans.dto.ProvidableMaterialDto;
import com.fromlabs.inventory.supplierservice.supplier.providable_material.beans.request.ProvidableMaterialPageRequest;
import com.fromlabs.inventory.supplierservice.supplier.providable_material.beans.request.ProvidableMaterialRequest;
import lombok.experimental.UtilityClass;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

import static com.fromlabs.inventory.supplierservice.common.factory.FactoryCreateType.DEFAULT;
import static com.fromlabs.inventory.supplierservice.common.factory.FactoryCreateType.EMPTY;
import static com.fromlabs.inventory.supplierservice.supplier.providable_material.factory.ProvidableMaterialEntityFactory.create;

/**
 * Providable material mapper
 * @author Liem
 */
@UtilityClass
public class ProvidableMaterialMapper {

    //<editor-fold desc="CONVERT TO DTO">

    /**
     * Convert entity to DTO
     * @param entity    ProvidableMaterialEntity
     * @return          ProvidableMaterialDto
     */
    static public ProvidableMaterialDto toDto(
            ProvidableMaterialEntity entity,
            IngredientClient ingredientClient
    ) {
        return ProvidableMaterialDto.materialBuilder()
                .id(entity.getId())
                .tenantId(entity.getClientId())
                .name(entity.getName())
                .description(entity.getDescription())
                .createAt(entity.getUpdateAt())
                .updateAt(entity.getUpdateAt())
                .activated(entity.isActive())
                .min(entity.getMinimumQuantity())
                .max(entity.getMaximumQuantity())
                .ingredient(ingredientClient.getIngredientById(entity.getClientId(), entity.getIngredientId()))
                .supplier(SupplierMapper.toDto(entity.getSupplier()))
                .build();
    }

    /**
     * Convert list of entity to list of DTO
     * @param entities  List&lt;SupplierEntity&gt;
     * @return          List&lt;SupplierDto&gt;
     */
    static public List<ProvidableMaterialDto> toDto(
            List<ProvidableMaterialEntity> entities,
            IngredientClient ingredientClient
    ) {
        return entities.stream().map(entity -> toDto(entity, ingredientClient)).collect(Collectors.toList());
    }

    /**
     * Convert page of entity to list of DTO
     * @param entities  Page&lt;SupplierEntity&gt;
     * @return          Page&lt;SupplierEntity&gt;
     */
    static public Page<ProvidableMaterialDto> toDto(
            Page<ProvidableMaterialEntity> entities,
            IngredientClient ingredientClient
    ) {
        return entities.map(entity -> toDto(entity, ingredientClient));
    }

    //</editor-fold>

    //<editor-fold desc="CONVERT TO ENTITY">

    /**
     * Covert request to entity
     * @param request   ProvidableMaterialRequest
     * @return          ProvidableMaterialEntity
     */
    public static ProvidableMaterialEntity toEntity(
            ProvidableMaterialRequest request
    ) {
        var entity = create(DEFAULT);
        entity.setIngredientId(request.getIngredientId());
        entity.setClientId(request.getClientId());
        entity.setName(request.getName());
        entity.setDescription(request.getDescription());
        entity.setMinimumQuantity(request.getMinimumQuantity());
        entity.setMaximumQuantity(request.getMaximumQuantity());
        entity.setActive(request.isActive());
        return entity;
    }

    /**
     * Covert request to entity
     * @param request   ProvidableMaterialRequest
     * @return          ProvidableMaterialEntity
     */
    public static ProvidableMaterialEntity toEntity(
            ProvidableMaterialPageRequest request,
            SupplierEntity supplier
    ) {
        var entity = create(EMPTY);
        entity.setSupplier(supplier);
        entity.setIngredientId(request.getIngredientId());
        entity.setClientId(request.getClientId());
        entity.setName(request.getName());
        entity.setDescription(request.getDescription());
        entity.setUpdateAt(request.getUpdateAt());
        return entity;
    }

    //</editor-fold>
}
