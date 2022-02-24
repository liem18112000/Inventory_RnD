package com.fromlabs.inventory.supplierservice.imports.details.mapper;

import com.fromlabs.inventory.supplierservice.client.ingredient.IngredientClient;
import com.fromlabs.inventory.supplierservice.common.factory.FactoryCreateType;
import com.fromlabs.inventory.supplierservice.imports.ImportEntity;
import com.fromlabs.inventory.supplierservice.imports.details.ImportDetailEntity;
import com.fromlabs.inventory.supplierservice.imports.details.beans.dto.ImportDetailDto;
import com.fromlabs.inventory.supplierservice.imports.details.beans.request.ImportDetailRequest;
import com.fromlabs.inventory.supplierservice.imports.details.factory.ImportDetailEntityFactory;
import com.fromlabs.inventory.supplierservice.imports.mapper.ImportMapper;
import lombok.experimental.UtilityClass;
import org.springframework.data.domain.Page;

import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.fromlabs.inventory.supplierservice.common.factory.FactoryCreateType.DEFAULT;
import static com.fromlabs.inventory.supplierservice.imports.details.factory.ImportDetailEntityFactory.*;

/**
 * Import detail mapper
 * @author Liem
 */
@UtilityClass
public class ImportDetailMapper {

    //<editor-fold desc="TO DTO">

    /**
     * Convert to DTO
     * @param entity ImportDetailEntity
     * @param ingredientClient IngredientClient
     * @return ImportDetailDto
     */
    public ImportDetailDto toDto(
            final ImportDetailEntity entity,
            @NotNull final IngredientClient ingredientClient) {
        if (Objects.isNull(entity)) {
            return null;
        }
        return ImportDetailDto.importDetailBuilder()
                .id(entity.getId())
                .imports(ImportMapper.toDto(entity.getImportEntity()))
                .tenantId(entity.getClientId())
                .name(entity.getName())
                .description(entity.getDescription())
                .ingredient(ingredientClient.getIngredientById(entity.getClientId(), entity.getIngredientId()))
                .quantity(entity.getQuantity())
                .activated(entity.isActive())
                .createAt(entity.getCreatedAt())
                .updateAt(entity.getUpdatedAt())
                .build();
    }

    /**
     * Convert to DTO
     * @param entities List of ImportDetailEntity
     * @param ingredientClient IngredientClient
     * @return List of ImportDetailDto
     */
    public List<ImportDetailDto> toDto(final @NotNull List<ImportDetailEntity> entities,
                                       @NotNull final IngredientClient ingredientClient) {
        return entities.stream().map(entity -> ImportDetailMapper.toDto(entity, ingredientClient))
                .collect(Collectors.toList());
    }

    /**
     * Convert to DTO
     * @param entities List of ImportDetailEntity
     * @param ingredientClient IngredientClient
     * @return Page of ImportDetailDto
     */
    public Page<ImportDetailDto> toDto(final @NotNull Page<ImportDetailEntity> entities,
                                       @NotNull final IngredientClient ingredientClient) {
        return entities.map(entity -> ImportDetailMapper.toDto(entity, ingredientClient));
    }

    //</editor-fold>
}
