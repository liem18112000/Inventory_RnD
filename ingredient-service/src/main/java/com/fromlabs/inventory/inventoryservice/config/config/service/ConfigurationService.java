package com.fromlabs.inventory.inventoryservice.config.config.service;

import com.fromlabs.inventory.inventoryservice.common.dto.SimpleDto;
import com.fromlabs.inventory.inventoryservice.common.service.CrudService;
import com.fromlabs.inventory.inventoryservice.config.config.entity.ConfigurationEntity;

import java.util.Set;

public interface ConfigurationService extends CrudService<ConfigurationEntity, Long> {
    ConfigurationEntity getByName(Long clientId, String name);

    Set<SimpleDto> getLabelValue(Long clientId);
}
