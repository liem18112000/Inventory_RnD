package com.fromlabs.inventory.inventoryservice.domains.restaurant.config;

import lombok.Getter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.fromlabs.inventory.inventoryservice.domains.DomainServiceConfiguration.DOMAIN_SERVICE_NAME_CONFIG;
import static com.fromlabs.inventory.inventoryservice.domains.DomainServiceConfiguration.RESTAURANT_DOMAIN;

@ConditionalOnProperty(
        value       = DOMAIN_SERVICE_NAME_CONFIG,
        havingValue = RESTAURANT_DOMAIN
)
@Component
public class NotificationConfiguration {

    @Getter
    private String[] emails = {
            "liemdev18112000@gmail.com",
            "liem18112000@gmail.com"
    };
}
