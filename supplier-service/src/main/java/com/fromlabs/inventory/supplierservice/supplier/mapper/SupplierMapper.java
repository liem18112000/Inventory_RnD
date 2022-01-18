package com.fromlabs.inventory.supplierservice.supplier.mapper;

import com.fromlabs.inventory.supplierservice.supplier.SupplierEntity;
import com.fromlabs.inventory.supplierservice.supplier.beans.dto.SupplierDto;
import com.fromlabs.inventory.supplierservice.supplier.beans.request.SupplierPageRequest;
import com.fromlabs.inventory.supplierservice.supplier.beans.request.SupplierRequest;
import lombok.experimental.UtilityClass;
import org.springframework.data.domain.Page;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.fromlabs.inventory.supplierservice.common.factory.FactoryCreateType.DEFAULT;
import static com.fromlabs.inventory.supplierservice.common.factory.FactoryCreateType.EMPTY;
import static com.fromlabs.inventory.supplierservice.supplier.factory.SupplierEntityFactory.create;

/**
 * Supplier mapper
 * @author Liem
 */
@UtilityClass
public class SupplierMapper {

    //<editor-fold desc="CONVERT TO DTO">

    /**
     * Convert entity to DTO
     * @param entity    SupplierEntity
     * @return          SupplierDto
     */
    public SupplierDto toDto(SupplierEntity entity) {
        var builder = SupplierDto.supplierBuilder()
                .id(entity.getId())
                .tenantId(entity.getClientId())
                .name(entity.getName())
                .code(entity.getCode())
                .description(entity.getDescription())
                .createAt(entity.getUpdateAt())
                .updateAt(entity.getUpdateAt())
                .activated(entity.isActive());

        return Objects.nonNull(entity.getParent()) ? builder.build() :
                builder.children(toDto(entity.getChildren())).build();
    }

    /**
     * Convert list of entity to list of DTO
     * @param entities  List&lt;SupplierEntity&gt;
     * @return          List&lt;SupplierDto&gt;
     */
    public List<SupplierDto> toDto(List<SupplierEntity> entities) {
        return entities.stream().map(SupplierMapper::toDto).collect(Collectors.toList());
    }

    /**
     * Convert page of entity to list of DTO
     * @param entities  Page&lt;SupplierEntity&gt;
     * @return          Page&lt;SupplierEntity&gt;
     */
    public Page<SupplierDto> toDto(Page<SupplierEntity> entities) {
        return entities.map(SupplierMapper::toDto);
    }

    //</editor-fold>

    //<editor-fold desc="CONVERT TO ENTITY">

    /**
     * Convert request to entity
     * @param request   SupplierRequest
     * @return          SupplierEntity
     */
    public static SupplierEntity toEntity(@NotNull final SupplierRequest request) {
        var entity = create(DEFAULT);
        entity.setClientId(request.getTenantId());
        entity.setName(request.getName());
        entity.setCode(request.getCode());
        entity.setDescription(request.getDescription());
        entity.setGroup(Objects.isNull(request.getParentId()));
        entity.setCreateAt(request.getCreatedAt());
        return entity;
    }

    /**
     * Convert request to entity
     * @param request   SupplierRequest
     * @return          SupplierEntity
     */
    public static SupplierEntity toEntity(SupplierPageRequest request) {
        var entity = create(EMPTY);
        entity.setClientId(request.getTenantId());
        entity.setName(request.getName());
        entity.setCode(request.getCode());
        entity.setDescription(request.getDescription());
        entity.setGroup(Objects.isNull(request.getParentId()) || request.getParentId() <= 0);
        entity.setCreateAt(request.getCreateAt());
        entity.setUpdateAt(request.getUpdateAt());
        return entity;
    }

    //</editor-fold>
}
