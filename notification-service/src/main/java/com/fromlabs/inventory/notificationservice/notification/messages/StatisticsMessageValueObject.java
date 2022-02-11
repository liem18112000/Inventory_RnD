package com.fromlabs.inventory.notificationservice.notification.messages;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fromlabs.inventory.notificationservice.notification.messages.models.LowStockDetails;
import com.fromlabs.inventory.notificationservice.notification.messages.models.StatisticsDetails;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class StatisticsMessageValueObject extends MessageValueObject {
    private static final long serialVersionUID = 7233818492237317710L;

    private String dashboardLink;

    private List<StatisticsDetails> details;

    @Builder(builderMethodName = "statisticsMessageBuilder")
    public StatisticsMessageValueObject(
            String subject, String body, String link, String sendAt,
            String from, String to, List<StatisticsDetails> details) {
        super(subject, body, sendAt, from, to);
        this.dashboardLink = link;
        this.details = details;
    }

    @Override
    public Map<String, Object> toMap() {
        var map = new HashMap<>(super.toMap());
        map.put("link", Optional.ofNullable(this.getDashboardLink()).orElse(""));
        map.put("details", Optional.ofNullable(this.getDetails()).orElse(List.of()));
        return map;
    }
}
