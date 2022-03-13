package com.fromlabs.inventory.inventoryservice.domains.restaurant.beans;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Builder
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SendStatisticsResponse {

    @Builder.Default
    private boolean sendSuccess = true;

    @Builder.Default
    private List<StatisticsDetails> details = new ArrayList<>();

    @Builder.Default
    private String failedMessage = null;

    private final String sendAt = Instant.now().toString();

}
