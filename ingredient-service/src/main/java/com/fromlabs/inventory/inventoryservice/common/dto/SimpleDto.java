package com.fromlabs.inventory.inventoryservice.common.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder(toBuilder = true)
@Data
@NoArgsConstructor
@EqualsAndHashCode
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SimpleDto {
    public SimpleDto(Object value, Object label){
        this.value = value;
        this.label = label;
    }
    public SimpleDto(Object value, Object label, List<SimpleDto> options){
        this.value = value;
        this.label = label;
        this.options = options;
    }
    private Object value;
    private Object label;
    private List<SimpleDto> options;
}
