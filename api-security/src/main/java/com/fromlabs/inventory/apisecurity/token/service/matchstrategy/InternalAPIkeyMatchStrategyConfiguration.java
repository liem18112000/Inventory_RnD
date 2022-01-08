package com.fromlabs.inventory.apisecurity.token.service.matchstrategy;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * Config for register all bean for Internal API key match strategy
 * @author Liem
 */
@Configuration
public class InternalAPIkeyMatchStrategyConfiguration {

    @Primary
    @Bean(name = "BcryptMatchStrategy")
    InternalAPIKeyMatchStrategy getBcryptStrategy() {
        return new BcryptBasedMatchStrategy();
    }
}
