package com.fromlabs.inventory.inventoryservice.domains.restaurant.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
public class StatisticsDetails {
    private String ingredientName;
    private String ingredientCode;
    private Float quantity;
    private String unit;
    private String unitType;
    private String updateAt;
}
