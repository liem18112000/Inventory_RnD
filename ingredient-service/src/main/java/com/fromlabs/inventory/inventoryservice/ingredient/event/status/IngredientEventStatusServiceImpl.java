/*
 * Copyright (c) 2021. Liem Doan
 */

package com.fromlabs.inventory.inventoryservice.ingredient.event.status;

import com.fromlabs.inventory.inventoryservice.common.exception.ObjectNotFoundException;
import com.fromlabs.inventory.inventoryservice.ingredient.event.status.dto.IngredientEventStatusDto;
import com.fromlabs.inventory.inventoryservice.ingredient.event.status.mapper.IngredientEventStatusMapper;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * {@inheritDoc}
 */
@Service
public class IngredientEventStatusServiceImpl implements IngredientEventStatusService {

    /**
     * Get event status by name
     *
     * @param name status name
     * @return IngredientEventStatus
     */
    @SneakyThrows
    @Override
    public IngredientEventStatusDto getByName(
            @NotNull @NotBlank final String name
    ) {
        return  Arrays.stream(IngredientEventStatus.values())
                .filter(s -> s.getName().equals(name)).findFirst()
                .map(IngredientEventStatusMapper::toDto)
                .orElseThrow(() -> new ObjectNotFoundException("status is not found with name : ".concat(name)));
    }

    /**
     * Gte all statuses
     *
     * @return list of event statuses
     */
    @Override
    public List<IngredientEventStatusDto> getAll() {
        return  Arrays.stream(IngredientEventStatus.values())
                .map(IngredientEventStatusMapper::toDto)
                .collect(Collectors.toList());
    }
}
