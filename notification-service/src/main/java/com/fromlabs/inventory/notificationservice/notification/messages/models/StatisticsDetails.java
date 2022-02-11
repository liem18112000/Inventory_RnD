package com.fromlabs.inventory.notificationservice.notification.messages.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class StatisticsDetails {
    private String ingredientName;
    private String ingredientCode;
    private Float quantity;
    private String unit;
    private String unitType;
    private String updateAt;
}
