package com.fromlabs.inventory.apisecurity.token;

import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * Internal API key mapper
 */
@Component
public class InternalAPIKeyMapper {

    /**
     * Convert entity to DTO
     * @param entity InternalAPIKeyEntity
     * @return InternalAPIKeyDTO
     */
    public InternalAPIKeyDTO toDto(@NotNull final InternalAPIKeyEntity entity) {
        return InternalAPIKeyDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .keyHashed(entity.getHashedApiKey())
                .principal(entity.getAuthorityPrincipal())
                .role(entity.getApiKeyRole())
                .build();
    }

    public InternalAPIKeyEntity toEntity(@NotNull final InternalAPIKeyDTO apiKeyDTO) {
        var entity = new InternalAPIKeyEntity();
        entity.setName(apiKeyDTO.getName());
        entity.setApiKeyRole(apiKeyDTO.getRole());
        entity.setAuthorityPrincipal(apiKeyDTO.getPrincipal());
        entity.setHashedApiKey(apiKeyDTO.getKeyHashed());
        entity.setCreatedAt(LocalDateTime.now().toString());
        entity.setUpdatedAt(entity.getCreatedAt());
        entity.setActive(true);
        return entity;
    }
}
