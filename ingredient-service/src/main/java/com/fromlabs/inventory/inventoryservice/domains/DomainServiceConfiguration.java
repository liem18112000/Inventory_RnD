/*
 * Copyright (c) 2021. Liem Doan
 */

package com.fromlabs.inventory.inventoryservice.domains;

import com.fromlabs.inventory.inventoryservice.domains.fast.services.FastInventoryDomainService;
import com.fromlabs.inventory.inventoryservice.domains.fast.services.FastInventoryDomainServiceImpl;
import com.fromlabs.inventory.inventoryservice.domains.jakten.services.JaktenInventoryDomainService;
import com.fromlabs.inventory.inventoryservice.domains.jakten.services.JaktenInventoryDomainServiceImpl;
import com.fromlabs.inventory.inventoryservice.domains.restaurant.services.RestaurantInventoryDomainService;
import com.fromlabs.inventory.inventoryservice.domains.restaurant.services.RestaurantInventoryDomainServiceImpl;
import com.fromlabs.inventory.inventoryservice.ingredient.IngredientService;
import com.fromlabs.inventory.inventoryservice.inventory.InventoryService;
import com.fromlabs.inventory.inventoryservice.item.ItemService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <h1>Domain service configuration</h1>
 *
 * <h2>Brief Information</h2>
 * <p>The main point of this configuration is to register the right beans of the domain-specific object</p>
 * <p>The related beans of the domain can be listed as follows</p>
 *
 * <h3>Domain Service Beans</h3>
 * <ul>
 *     <li>
 *         Domain Service Layers (Beans) - domain service is isolated from the application service layers and
 *         infrastructure service layers.
 *     </li>
 *     <li>It is register based on the config (will be demonstrated below)</li>
 *     <li>
 *         In term of the current project (RnD 2021), there are three primary type of domain services
 *         are available to be applied :
 *         <ul>
 *             <li>Restaurant Domain Service : specially designed for the business logic of FromLabs Restaurant</li>
 *             <li>F.A.S.T Domain Service : specially designed for business logic of FromLabs F.A.S.T </li>
 *             <li>Jakten Domain Service : specially designed for business logic of FromLabs Jakten</li>
 *         </ul>
 *     </li>
 * </ul>
 *
 * <h3>Domain Service Controller Beans</h3>
 * <ul>
 *     <li>It is the place where the API pf the specific domain will be exposed to public</li>
 *     <li>It is register based on the config (will be demonstrated below)</li>
 * </ul>
 *
 * <h2>Usages and Mechanism</h2>
 *
 * <h3>How to choose a domain base on config</h3>
 *
 * <p>As it has been mentioned, the domain service beans would be selected base on a simple configuration</p>
 * <p>In term of Spring Boot application, the configuration is recommend to be place the application.yml or
 * application.properties or any type of configuration file to activate the mechanism of registering th domain service beans</p>
 *
 * <p>Example</p>
 * <pre>
 * domain:
 *   service-name: FromLabsRestaurantDomainService
 *   service-path: restaurant
 * </pre>
 *
 * <b>Note</b>
 * <ul>
 *     <li>service-name (required) : represents the registered name of the domain service beans
 *     , see the annotation Bean's name (FromLabsRestaurantDomainService, FromLabsFastDomainService,
 *     FromLabsJaktenDomainService)</li>
 *     <li>service-path (required) : represent the domain controller sub path which would be add to suffix of the base API url
 *     , see the annotation RequestMapping of class RestaurantInventoryDomainController</li>
 *     <li>Other properties could be add to serve advance purpose of the domain service configuration</li>
 *     <li style="coler:red"><b>Only once domain service could be assigned at once</b></li>
 * </ul>
 *
 * <h3>How domain service configuration run</h3>
 *
 * <p>Firstly, the main mechanism is the condition on the configuration. In short, it will work as the navigation
 * for selecting the right bean base on service-name of domain configuration</p>
 *
 * @see <a href="https://www.baeldung.com/spring-conditional-annotations">Spring Conditional Annotations</a>
 */
@Slf4j
@Configuration
@ConfigurationProperties(prefix = "domain")
public class DomainServiceConfiguration {

    public String serviceName;
    public String servicePath;

    public static final String DOMAIN_SERVICE_NAME_CONFIG   = "domain.service-name";
    public static final String RESTAURANT_DOMAIN            = "FromLabsRestaurantDomainService";
    public static final String FAST_DOMAIN                  = "FromLabsFastDomainService";
    public static final String JAKTEN_DOMAIN                = "FromLabsJaktenDomainService";

    /**
     * Get restaurant domain service beans
     * @param ingredientService IngredientService
     * @param inventoryService  InventoryService
     * @param itemService       ItemService
     * @return                  RestaurantInventoryDomainService
     */
    @Bean(name = RESTAURANT_DOMAIN)
    @ConditionalOnProperty(
        value       = DOMAIN_SERVICE_NAME_CONFIG,
        havingValue = RESTAURANT_DOMAIN
    )
    public RestaurantInventoryDomainService getResDomainService(
            IngredientService ingredientService,
            InventoryService inventoryService,
            ItemService itemService
    ) {
        log.info("Domain service : {}", RestaurantInventoryDomainServiceImpl.class.getName());
        return new RestaurantInventoryDomainServiceImpl(
                ingredientService,
                inventoryService,
                itemService
        );
    }

    /**
     * Get F.A.S.T domain service beans
     * @return  FastInventoryDomainService
     */
    @Bean(name = FAST_DOMAIN)
    @ConditionalOnProperty(
            value       = DOMAIN_SERVICE_NAME_CONFIG,
            havingValue = FAST_DOMAIN
    )
    public FastInventoryDomainService getFastDomainService() {
        log.info("Domain service : {}", FastInventoryDomainServiceImpl.class.getName());
        return new FastInventoryDomainServiceImpl();
    }

    /**
     * Get Jakten domain service beans
     * @return  JaktenInventoryDomainService
     */
    @Bean(name = JAKTEN_DOMAIN)
    @ConditionalOnProperty(
            value       = DOMAIN_SERVICE_NAME_CONFIG,
            havingValue = JAKTEN_DOMAIN
    )
    public JaktenInventoryDomainService getJaktenDomainService() {
        log.info("Domain service : {}", JaktenInventoryDomainServiceImpl.class.getName());
        return new JaktenInventoryDomainServiceImpl();
    }


}
