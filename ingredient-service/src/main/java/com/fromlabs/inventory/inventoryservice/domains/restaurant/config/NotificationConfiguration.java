package com.fromlabs.inventory.inventoryservice.domains.restaurant.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fromlabs.inventory.inventoryservice.config.config.service.ConfigurationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.time.Instant;
import java.time.LocalDate;
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
    public static final String NOTIFICATION_MAIL_STATISTICS_PERIOD = "notification.statistics.period";
    public static final String NOTIFICATION_MAIL_STATISTICS_TO = "notification.statistics.to";
    public static final String NOTIFICATION_MAIL_STATISTICS_LAST = "notification.statistics.last";

    private final ConfigurationService configurationService;

    private final List<String> emails = List.of("liem18112000@gmail.com");

    private final List<String> statisticsEmails = List.of("liemdev18112000@gmail.com");

    private final int DEFAULT_MAIL_STATISTICS_PERIOD = 30;

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

    public Integer getStatisticsPeriod(Long clientId) {
        final var config = this.configurationService
                .getByName(clientId, NOTIFICATION_MAIL_STATISTICS_PERIOD);
        if (Objects.isNull(config)) {
            log.warn("Dynamic config for '{}' is not found. Default config will be used : {}",
                    NOTIFICATION_MAIL_STATISTICS_PERIOD, this.DEFAULT_MAIL_STATISTICS_PERIOD);
            return this.DEFAULT_MAIL_STATISTICS_PERIOD;
        }
        return Integer.parseInt(config.getValue());
    }

    public LocalDate getStatisticsLastSentDate(Long clientId) {
        final var config = this.configurationService
                .getByName(clientId, NOTIFICATION_MAIL_STATISTICS_LAST);
        if (Objects.isNull(config)) {
            log.warn("Dynamic config for '{}' is not found. Default config will be used : {}",
                    NOTIFICATION_MAIL_STATISTICS_LAST, LocalDate.now());
            return LocalDate.now();
        }
        return LocalDate.parse(config.getValue());
    }

    public List<String> getStatisticsEmails(Long clientId) throws JsonProcessingException {
        final var mapper = new ObjectMapper();
        final var config = this.configurationService
                .getByName(clientId, NOTIFICATION_MAIL_STATISTICS_TO);
        if (Objects.isNull(config)) {
            log.warn("Dynamic config for '{}' is not found. Default config will be used : {}",
                    NOTIFICATION_MAIL_STATISTICS_TO, this.statisticsEmails);
            return statisticsEmails;
        }
        return mapper.readValue(config.getValue(), new TypeReference<>() {});
    }

    @Transactional
    public void updateStatisticsLastSentDateToNow(Long clientId) {
        final var config = this.configurationService
                .getByName(clientId, NOTIFICATION_MAIL_STATISTICS_LAST);
        if (Objects.isNull(config)) {
            throw new EntityNotFoundException("Dynamic config for '{}' is not found.");
        }
        config.setUpdateAt(Instant.now().toString());
        config.setValue(LocalDate.now().toString());
        this.configurationService.save(config);
    }
}
