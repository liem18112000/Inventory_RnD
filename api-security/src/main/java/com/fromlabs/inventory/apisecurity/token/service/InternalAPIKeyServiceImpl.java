package com.fromlabs.inventory.apisecurity.token.service;

import com.fromlabs.inventory.apisecurity.token.InternalAPIKeyDTO;
import com.fromlabs.inventory.apisecurity.token.InternalAPIKeyEntityRepository;
import com.fromlabs.inventory.apisecurity.token.InternalAPIKeyMapper;
import com.fromlabs.inventory.apisecurity.token.service.hashstrategy.InternalAPIKeyHashStrategy;
import com.fromlabs.inventory.apisecurity.token.service.matchstrategy.InternalAPIKeyMatchStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Stream;

/**
 * {@inheritDoc}
 */
@Slf4j
@Service
public class InternalAPIKeyServiceImpl extends AbstractInternalAPIKeyService {

    protected final InternalAPIKeyMatchStrategy matchStrategy;

    protected final InternalAPIKeyHashStrategy hashStrategy;

    @Autowired
    public InternalAPIKeyServiceImpl(
            InternalAPIKeyEntityRepository repository,
            InternalAPIKeyMapper mapper,
            InternalAPIKeyMatchStrategy matchStrategy,
            InternalAPIKeyHashStrategy hashStrategy) {
        super(repository, mapper);
        this.matchStrategy = matchStrategy;
        this.hashStrategy = hashStrategy;
    }

    /**
     * Use bcrypt to compare apikey value and hashed key in Database.
     * {@inheritDoc}
     */
    @Override
    public boolean authorize(@NotNull final String apiKey) {

        if (isAPIKeyInvalid(apiKey)) {
            return false;
        }

        Stream<String> allKeys;
        try{
            allKeys = this.getAll().stream().map(InternalAPIKeyDTO::getKeyHashed);
        } catch (Exception exception) {
            log.error(exception.getMessage());
            return false;
        }

        return allKeys.anyMatch(hashedKey -> matchStrategy.isMatch(apiKey, hashedKey));
    }

    protected boolean isAPIKeyInvalid(String apiKey) {
        return !StringUtils.hasText(apiKey);
    }

    protected boolean isPrincipalInvalid(String principal) {
        return !StringUtils.hasText(principal);
    }

    /**
     * Use bcrypt to compare apikey value and hashed key in Database.
     * {@inheritDoc}
     */
    @Override
    public InternalAPIKeyDTO authenticate(String apiKey, String principal) {
        if (isAPIKeyInvalid(apiKey) || isPrincipalInvalid(principal)) {
            return null;
        }

        List<InternalAPIKeyDTO> allKeys;
        try{
            allKeys = this.getAll();
        } catch (Exception exception) {
            log.error(exception.getMessage());
            return null;
        }

        return allKeys.stream()
                .filter(keyDTO -> isAuthenticateMatch(apiKey, principal, keyDTO))
                .findFirst().orElse(null);
    }

    private boolean isAuthenticateMatch(
            @NotNull final String apiKey, @NotNull final String principal,
            @NotNull final InternalAPIKeyDTO keyDTO) {
        return keyDTO.getPrincipal().equals(principal) &&
                matchStrategy.isMatch(apiKey, keyDTO.getKeyHashed());
    }

    /**
     * Register api key
     * @param apiKeyDTO Raw api key information
     * @return InternalAPIKeyDTO
     */
    @Override
    @Transactional
    public InternalAPIKeyDTO registerKey(@NotNull final InternalAPIKeyDTO apiKeyDTO) {
        var entity = this.mapper.toEntity(apiKeyDTO);
        entity.setHashedApiKey(this.hashStrategy.hash(apiKeyDTO.getKeyHashed()));
        entity = this.repository.save(entity);
        return this.mapper.toDto(entity);
    }
}
