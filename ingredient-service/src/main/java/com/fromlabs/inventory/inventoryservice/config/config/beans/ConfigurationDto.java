package com.fromlabs.inventory.inventoryservice.config.config.beans;

import com.fromlabs.inventory.inventoryservice.common.dto.BaseDto;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Data
public class ConfigurationDto extends BaseDto<Long> {

    private String value;

    @Builder
    public ConfigurationDto(
            Long id, Long clientId, String name, String description,
            String updateAt, String createAt, boolean isActive, String value) {
        super(id, clientId, name, description, updateAt, createAt, isActive);
        this.value = value;
    }
}
