package com.fromlabs.inventory.apisecurity.controller;

import com.fromlabs.inventory.apisecurity.config.ApiV1;
import com.fromlabs.inventory.apisecurity.token.dto.AuthDTO;
import com.fromlabs.inventory.apisecurity.token.service.InternalAPIKeyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

import static com.fromlabs.inventory.apisecurity.config.AppConfig.X_API_KEY_HEADER;
import static com.fromlabs.inventory.apisecurity.config.AppConfig.X_PRINCIPAL_HEADER;

@Slf4j
@RestController
@RequestMapping(value = "endpoint/security/" + ApiV1.URI_API)
public class InternalAPIKeyEndpoint {

    private final InternalAPIKeyService apiKeyService;

    public InternalAPIKeyEndpoint(InternalAPIKeyService apiKeyService) {
        this.apiKeyService = apiKeyService;
    }

    @PostMapping("authorize")
    public ResponseEntity<AuthDTO> authorize(
            @RequestHeader(value = X_API_KEY_HEADER, required = false) String apiKey
    ) {
        var authResponse = new AuthDTO();
        if(Objects.isNull(apiKey)) {
            authResponse.setSuccess(false);
            authResponse.setMessage("API key credential is required");
        } else {
            authResponse.setSuccess(this.apiKeyService.authorize(apiKey));
            if(!authResponse.isSuccess()) {
                authResponse.setMessage("Authorize failed with API key : ".concat(apiKey));
            }
        }

        return ResponseEntity.ok(authResponse);
    }

    @PostMapping("authenticate")
    public ResponseEntity<?> authenticate(
            @RequestHeader(value = X_API_KEY_HEADER, required = false) String apiKey,
            @RequestHeader(value = X_PRINCIPAL_HEADER, required = false) String principal
    ) {
        var authResponse = new AuthDTO();
        if (Objects.isNull(apiKey)) {
            authResponse.setSuccess(false);
            authResponse.setMessage("API key credential is required");
        } else {
            if (Objects.isNull(principal)) {
                authResponse.setSuccess(false);
                authResponse.setMessage("API key principal is required");
            } else {
                final var authenticate = this.apiKeyService.authenticate(apiKey, principal);
                authResponse.setSuccess(Objects.nonNull(authenticate));
                if(authResponse.isSuccess()) {
                    authResponse.setPrincipal(authenticate.getPrincipal());
                    authResponse.setAuthority(authenticate.getRole());
                } else {
                    authResponse.setMessage("Authenticate failed with API key : ".concat(apiKey)
                            .concat(" and principal: ".concat(principal)));
                }
            }
        }

        return ResponseEntity.ok(authResponse);
    }
}
