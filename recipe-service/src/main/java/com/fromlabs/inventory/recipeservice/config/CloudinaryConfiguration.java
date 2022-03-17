package com.fromlabs.inventory.recipeservice.config;

import com.cloudinary.Cloudinary;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class CloudinaryConfiguration {

    public static final String CLOUD_NAME = "cloud_name";
    public static final String API_KEY = "api_key";
    public static final String API_SECRET = "api_secret";

    @Value("${cloudinary.cloud-name}")
    private String cloudName;

    @Value("${cloudinary.api-key}")
    private String apiKey;

    @Value("${cloudinary.api-secret}")
    private String apiSecret;

    @Bean
    public Cloudinary cloudinaryConfig() {
        return new Cloudinary(Map.of(
            CLOUD_NAME, this.cloudName,
            API_KEY, this.apiKey,
            API_SECRET, this.apiSecret
        ));
    }
}
