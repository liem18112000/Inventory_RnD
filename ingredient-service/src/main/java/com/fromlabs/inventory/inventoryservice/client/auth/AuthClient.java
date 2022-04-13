package com.fromlabs.inventory.inventoryservice.client.auth;

import com.fromlabs.inventory.inventoryservice.client.auth.beans.AuthDTO;
import com.fromlabs.inventory.inventoryservice.config.ApiV1;
import io.sentry.spring.tracing.SentryTransaction;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

import static com.fromlabs.inventory.inventoryservice.config.AppConfig.X_API_KEY_HEADER;
import static com.fromlabs.inventory.inventoryservice.config.AppConfig.X_PRINCIPAL_HEADER;

@SentryTransaction(operation = "ingredient-auth-client")
@FeignClient(value = "${services.security-service.name}")
@RequestMapping(value = "endpoint/security/" + ApiV1.URI_API)
public interface AuthClient {

    @PostMapping("authorize")
    AuthDTO authorize(
            @RequestHeader(X_API_KEY_HEADER) String apiKey
    );

    @PostMapping("authenticate")
    AuthDTO authenticate(
            @RequestHeader(X_API_KEY_HEADER) String apiKey,
            @RequestHeader(X_PRINCIPAL_HEADER) String principal
    );
}
