package com.fromlabs.inventory.apisecurity.token.service;

import com.fromlabs.inventory.apisecurity.token.InternalAPIKeyDTO;

import java.util.List;

/**
 * Internal API key service
 * @author Liem
 */
public interface InternalAPIKeyService {

    /**
     * Authorization processing
     * @param apiKey Provided API key
     * @return true if api key match. Otherwise, false
     */
    boolean authorize(String apiKey);

    /**
     * Authenticate with specific role and principal
     * @param apiKey Provided API key
     * @param principal Provided principal
     * @return InternalAPIKeyDTO
     */
    InternalAPIKeyDTO authenticate(String apiKey, String principal);

    /**
     * Get all internal api key as DTO
     * @return list of internal API key DTO
     */
    List<? extends InternalAPIKeyDTO> getAll();

    InternalAPIKeyDTO registerKey(InternalAPIKeyDTO apiKeyDTO);
}
