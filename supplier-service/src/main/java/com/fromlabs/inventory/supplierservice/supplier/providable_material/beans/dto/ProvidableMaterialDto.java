/*
 * Copyright (c) 2021. Liem Doan
 */

package com.fromlabs.inventory.supplierservice.supplier.providable_material.beans;

import com.fromlabs.inventory.supplierservice.client.ingredient.IngredientClient;
import com.fromlabs.inventory.supplierservice.client.ingredient.bean.IngredientDto;
import com.fromlabs.inventory.supplierservice.common.dto.BaseDto;
import com.fromlabs.inventory.supplierservice.supplier.beans.SupplierDto;
import com.fromlabs.inventory.supplierservice.supplier.providable_material.ProvidableMaterialEntity;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

@EqualsAndHashCode(callSuper = true)
@Data
public class ProvidableMaterialDto extends BaseDto<Long> {

    private float           minimumQuantity;
    private float           maximumQuantity;
    private IngredientDto   ingredient;
    private SupplierDto     supplier;

    @Builder(builderMethodName = "materialBuilder")
    public ProvidableMaterialDto(
            Long id, Long tenantId, String name,
            String description, String updateAt, String createAt,
            boolean activated, float min, float max,
            IngredientDto ingredient, SupplierDto supplier
    ) {
        super(id, tenantId, name, description, updateAt, createAt, activated);
        this.ingredient         = ingredient;
        this.supplier           = supplier;
        this.minimumQuantity    = min;
        this.maximumQuantity    = max;
    }

    public ProvidableMaterialDto() {
    }

    /**
     * Convert entity to DTO
     * @param entity    ProvidableMaterialEntity
     * @return          ProvidableMaterialDto
     */
    static public ProvidableMaterialDto from(
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
                 .supplier(SupplierDto.from(entity.getSupplier()))
                 .build();
    }

    /**
     * Convert list of entity to list of DTO
     * @param entities  List&lt;SupplierEntity&gt;
     * @return          List&lt;SupplierDto&gt;
     */
    static public List<ProvidableMaterialDto> from(
            List<ProvidableMaterialEntity> entities,
            IngredientClient ingredientClient
    ) {
        return entities.stream().map(entity -> from(entity, ingredientClient)).collect(Collectors.toList());
    }

    /**
     * Convert page of entity to list of DTO
     * @param entities  Page&lt;SupplierEntity&gt;
     * @return          Page&lt;SupplierEntity&gt;
     */
    static public Page<ProvidableMaterialDto> from(
            Page<ProvidableMaterialEntity> entities,
            IngredientClient ingredientClient
    ) {
        return entities.map(entity -> from(entity, ingredientClient));
    }
}
