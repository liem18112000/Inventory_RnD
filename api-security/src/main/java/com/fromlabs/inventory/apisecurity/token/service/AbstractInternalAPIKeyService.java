package com.fromlabs.inventory.apisecurity.token.service;

import com.fromlabs.inventory.apisecurity.token.InternalAPIKeyDTO;
import com.fromlabs.inventory.apisecurity.token.InternalAPIKeyEntityRepository;
import com.fromlabs.inventory.apisecurity.token.InternalAPIKeyMapper;

import java.util.List;
import java.util.stream.Collectors;

public abstract class AbstractInternalAPIKeyService implements InternalAPIKeyService {

    protected final InternalAPIKeyEntityRepository repository;

    protected final InternalAPIKeyMapper mapper;

    protected AbstractInternalAPIKeyService(
            InternalAPIKeyEntityRepository repository,
            InternalAPIKeyMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    /**
     * {@inheritDoc}
     */
    public List<InternalAPIKeyDTO> getAll() {
        return repository.findAll()
                .stream().map(this.mapper::toDto)
                .collect(Collectors.toList());
    }
}
