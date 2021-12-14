/*
 * Copyright (c) 2021. Liem Doan
 */

package com.fromlabs.inventory.recipeservice.domains;

import com.fromlabs.inventory.recipeservice.detail.RecipeDetailService;
import com.fromlabs.inventory.recipeservice.domains.restaurant.services.RestaurantInventoryDomainService;
import com.fromlabs.inventory.recipeservice.domains.restaurant.services.RestaurantInventoryDomainServiceImpl;
import com.fromlabs.inventory.recipeservice.recipe.RecipeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@ConfigurationProperties(prefix = "domain")
public class DomainServiceConfiguration {

    public String serviceName;
    public String servicePath;

    public static final String DOMAIN_SERVICE_NAME_CONFIG   = "domain.service-name";
    public static final String RESTAURANT_DOMAIN            = "FromLabsRestaurantDomainService";

    @Bean(name = RESTAURANT_DOMAIN)
    @ConditionalOnProperty(
        value       = DOMAIN_SERVICE_NAME_CONFIG,
        havingValue = RESTAURANT_DOMAIN
    )
    public RestaurantInventoryDomainService getResDomainService(
            RecipeService recipeService,
            RecipeDetailService recipeDetailService
    ) {
        log.info("Domain service : {}", RestaurantInventoryDomainServiceImpl.class.getName());
        return new RestaurantInventoryDomainServiceImpl(
                recipeService,
                recipeDetailService
        );
    }
}
