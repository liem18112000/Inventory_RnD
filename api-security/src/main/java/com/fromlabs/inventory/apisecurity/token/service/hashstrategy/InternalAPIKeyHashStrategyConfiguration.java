package com.fromlabs.inventory.apisecurity.token.service.hashstrategy;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class InternalAPIKeyHashStrategyConfiguration {

    @Primary
    @Bean("BcryptHashStrategy")
    InternalAPIKeyHashStrategy getBcryptStrategy() {
        return new BcryptHashStrategy();
    }
}
