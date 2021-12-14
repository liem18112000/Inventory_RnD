/*
 * Copyright (c) 2021. Liem Doan
 */

package com.fromlabs.inventory.notificationservice.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@Profile("cors")
public class CorsGlobalConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("OPTIONS", "GET", "POST", "PUT", "DELETE")
                .allowedHeaders("Access-Control-Allow-Origin", "Origin", "X-Requested-With", "Content-Type", "Accept",
                        "X-XSRF-TOKEN", "Authentication", "Authorization", "tenantId", "userId", "username");
        WebMvcConfigurer.super.addCorsMappings(registry);
    }
}
