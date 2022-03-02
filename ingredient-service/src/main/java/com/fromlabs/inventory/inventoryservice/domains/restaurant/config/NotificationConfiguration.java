package com.fromlabs.inventory.inventoryservice.domains.restaurant.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fromlabs.inventory.inventoryservice.config.config.service.ConfigurationService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

import static com.fromlabs.inventory.inventoryservice.domains.DomainServiceConfiguration.DOMAIN_SERVICE_NAME_CONFIG;
import static com.fromlabs.inventory.inventoryservice.domains.DomainServiceConfiguration.RESTAURANT_DOMAIN;

@ConditionalOnProperty(
        value       = DOMAIN_SERVICE_NAME_CONFIG,
        havingValue = RESTAURANT_DOMAIN
)
@Component
@Slf4j
@RequiredArgsConstructor(onConstructor = @__({@Autowired}))
public class NotificationConfiguration {

    public static final String NOTIFICATION_MAIL_TO = "notification.mail.to";

    private final ConfigurationService configurationService;

    private final List<String> emails = List.of("liem18112000@gmail.com", "liemdev18112000@gmail.com");

    public List<String> getEmails(Long clientId) throws JsonProcessingException {
        final var mapper = new ObjectMapper();
        final var config = this.configurationService
                .getByName(clientId, NOTIFICATION_MAIL_TO);
        if (Objects.isNull(config)) {
            log.warn("Dynamic config for '{}' is not found. Default config will be used : {}",
                    NOTIFICATION_MAIL_TO, this.emails);
            return emails;
        }
        return mapper.readValue(config.getValue(), new TypeReference<>() {});
    }
}
