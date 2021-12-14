package com.fromlabs.inventory.recipeservice.config;

import com.fromlabs.inventory.recipeservice.common.factory.BaseEntityFactory;
import com.fromlabs.inventory.recipeservice.recipe.factory.RecipeEntityFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EntityFactoryConfiguration {

    @Bean
    public BaseEntityFactory<Long> createEntityFactory() {
        return new RecipeEntityFactory();
    }
}
