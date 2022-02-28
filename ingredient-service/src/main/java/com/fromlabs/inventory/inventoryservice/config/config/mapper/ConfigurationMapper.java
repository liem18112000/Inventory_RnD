package com.fromlabs.inventory.inventoryservice.config.config.mapper;

import com.fromlabs.inventory.inventoryservice.config.config.beans.ConfigurationDto;
import com.fromlabs.inventory.inventoryservice.config.config.entity.ConfigurationEntity;
import lombok.experimental.UtilityClass;

import java.util.Objects;

@UtilityClass
public class ConfigurationMapper {

    public ConfigurationDto toDto(
            final ConfigurationEntity entity) {
        if (Objects.isNull(entity)) {
            return null;
        }

        return ConfigurationDto.builder()
                .id(entity.getId())
                .clientId(entity.getClientId())
                .name(entity.getName())
                .value(entity.getValue())
                .description(entity.getDescription())
                .updateAt(entity.getUpdateAt())
                .isActive(entity.isActive())
                .build();
    }

    public ConfigurationEntity toEntity(
            final ConfigurationDto dto) {
        if (Objects.isNull(dto)) {
            throw new IllegalArgumentException("DTO is null");
        }

        var entity = new ConfigurationEntity();
        entity.setId(dto.getId());
        entity.setClientId(dto.getTenantId());
        entity.setName(dto.getName());
        entity.setValue(dto.getValue());
        entity.setDescription(dto.getDescription());
        entity.setActive(dto.isActive());
        entity.setUpdateAt(dto.getUpdateAt());
        return entity;
    }
}
