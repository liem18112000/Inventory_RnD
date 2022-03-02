package com.fromlabs.inventory.inventoryservice.config.config.entity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ConfigurationEntityRepository
        extends JpaRepository<ConfigurationEntity, Long> {
    Optional<ConfigurationEntity> findByClientIdAndName(Long clientId, String name);
    List<ConfigurationEntity> findAllByClientId(Long clientId);
}