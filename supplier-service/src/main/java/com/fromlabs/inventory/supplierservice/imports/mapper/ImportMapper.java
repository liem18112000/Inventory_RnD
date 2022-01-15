package com.fromlabs.inventory.supplierservice.imports.mapper;

import com.fromlabs.inventory.supplierservice.imports.ImportEntity;
import com.fromlabs.inventory.supplierservice.imports.beans.dto.ImportDto;
import com.fromlabs.inventory.supplierservice.imports.beans.request.ImportPageRequest;
import com.fromlabs.inventory.supplierservice.imports.beans.request.ImportRequest;
import com.fromlabs.inventory.supplierservice.imports.factory.ImportEntityFactory;
import com.fromlabs.inventory.supplierservice.supplier.SupplierEntity;
import com.fromlabs.inventory.supplierservice.supplier.mapper.SupplierMapper;
import lombok.experimental.UtilityClass;
import org.springframework.data.domain.Page;

import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.fromlabs.inventory.supplierservice.common.factory.FactoryCreateType.DEFAULT;
import static com.fromlabs.inventory.supplierservice.common.factory.FactoryCreateType.EMPTY;
import static com.fromlabs.inventory.supplierservice.imports.factory.ImportEntityFactory.*;

/**
 * Import mapper
 * @author Liem
 */
@UtilityClass
public class ImportMapper {

    //<editor-fold desc="CONVERT TO DTO">

    /**
     * Convert entity to DTO
     * @param entity    SupplierEntity
     * @return          SupplierDto
     */
    public ImportDto toDto(ImportEntity entity) {
        return  ImportDto.importBuilder()
                .id(entity.getId())
                .tenantId(entity.getClientId())
                .supplier(SupplierMapper.toDto(entity.getSupplier()))
                .name(entity.getName())
                .code(entity.getCode())
                .description(entity.getDescription())
                .createAt(entity.getUpdateAt())
                .updateAt(entity.getUpdateAt())
                .isActive(entity.isActive())
                .build();
    }

    /**
     * Convert list of entity to list of DTO
     * @param entities  List&lt;ImportEntity&gt;
     * @return          List&lt;ImportDto&gt;
     */
    public List<ImportDto> toDto(List<ImportEntity> entities) {
        return entities.stream().map(ImportMapper::toDto).collect(Collectors.toList());
    }

    /**
     * Convert page of entity to list of DTO
     * @param entities  Page&lt;ImportEntity&gt;
     * @return          Page&lt;ImportDto&gt;
     */
    public Page<ImportDto> toDto(Page<ImportEntity> entities) {
        return entities.map(ImportMapper::toDto);
    }

    //</editor-fold>

    //<editor-fold desc="CONVERT TO ENTITY">

    /**
     * Convert request to entity
     * @param request   ImportRequest
     * @param supplier  SupplierEntity
     * @return          ImportEntity
     */
    public static ImportEntity toEntity(ImportRequest request, SupplierEntity supplier) {
        var entity = create(DEFAULT);
        entity.setClientId(request.getTenantId());
        entity.setName(request.getName());
        entity.setCode(request.getCode());
        entity.setSupplier(supplier);
        entity.setDescription(request.getDescription());
        entity.setCreateAt(request.getCreatedAt());
        entity.setUpdateAt(Instant.now().toString());
        return entity;
    }

    /**
     * Convert request to entity
     * @param request   SupplierRequest
     * @return          SupplierEntity
     */
    public static ImportEntity toEntity(ImportPageRequest request) {
        var entity = create(EMPTY);
        entity.setClientId(request.getTenantId());
        entity.setName(request.getName());
        entity.setCode(request.getCode());
        entity.setDescription(request.getDescription());
        entity.setCreateAt(request.getCreateAt());
        entity.setUpdateAt(request.getUpdateAt());
        return entity;
    }

    //</editor-fold>
}
