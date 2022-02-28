package com.fromlabs.inventory.inventoryservice.config.config.service.impl;

import com.fromlabs.inventory.inventoryservice.common.dto.SimpleDto;
import com.fromlabs.inventory.inventoryservice.config.config.entity.ConfigurationEntity;
import com.fromlabs.inventory.inventoryservice.config.config.entity.ConfigurationEntityRepository;
import com.fromlabs.inventory.inventoryservice.config.config.service.ConfigurationService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor = @__({@Autowired}))
@Slf4j
public class ConfigurationServiceImpl implements ConfigurationService {

    private final ConfigurationEntityRepository repository;

    /**
     * Get an entity by id
     *
     * @param id Entity Identifier
     * @return Single entity
     */
    @Override
    public ConfigurationEntity getById(final Long id) {
        if (Objects.isNull(id)) {
            return null;
        }

        return this.repository.findById(id).orElse(null);
    }

    /**
     * Save an entity
     *
     * @param entity Entity Identifier
     * @return Saved Entity
     */
    @Override
    public ConfigurationEntity save(final ConfigurationEntity entity) {
        if (Objects.isNull(entity)) {
            throw new IllegalArgumentException("Entity is null");
        }
        return this.repository.save(entity);
    }

    /**
     * Delete an entity
     *
     * @param entity Entity Identifier
     */
    @Override
    public void delete(final ConfigurationEntity entity) {
        if (Objects.isNull(entity)) {
            throw new IllegalArgumentException("Entity is null");
        }
        this.repository.delete(entity);
    }

    @Override
    public ConfigurationEntity getByName(
            final Long clientId, final String name) {
        return this.repository.findByClientIdAndName(clientId, name).orElse(null);
    }

    @Override
    public Set<SimpleDto> getLabelValue(Long clientId) {
        if (Objects.isNull(clientId)) {
            return Set.of();
        }
        return this.repository.findAllByClientId(clientId).stream()
                .map(config -> SimpleDto.builder().label(config.getName()).value(config.getValue()).build())
                .collect(Collectors.toSet());
    }
}
