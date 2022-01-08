package com.fromlabs.inventory.apisecurity.controller;

import com.fromlabs.inventory.apisecurity.config.ApiV1;
import com.fromlabs.inventory.apisecurity.token.InternalAPIKeyDTO;
import com.fromlabs.inventory.apisecurity.token.auth.AuthDTO;
import com.fromlabs.inventory.apisecurity.token.auth.RegisterKeyDTO;
import com.fromlabs.inventory.apisecurity.token.service.InternalAPIKeyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

import static com.fromlabs.inventory.apisecurity.config.AppConfig.*;

@Slf4j
@RestController
@RequestMapping(value = "${application.base-url}/" + ApiV1.URI_API, produces = ApiV1.MIME_API)
public class InternalAPIKeyController {

    private final InternalAPIKeyService apiKeyService;

    public InternalAPIKeyController(InternalAPIKeyService apiKeyService) {
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
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(authResponse);
        }

        authResponse.setSuccess(this.apiKeyService.authorize(apiKey));
        if(authResponse.isSuccess()) {
            return ResponseEntity.ok(authResponse);
        }

        authResponse.setMessage("Authorize failed with API key : ".concat(apiKey));
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(authResponse);
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
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(authResponse);
        }

        if (Objects.isNull(principal)) {
            authResponse.setSuccess(false);
            authResponse.setMessage("API key principal is required");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(authResponse);
        }

        final var authenticate = this.apiKeyService.authenticate(apiKey, principal);
        authResponse.setSuccess(Objects.nonNull(authenticate));

        if(authResponse.isSuccess()) {
            authResponse.setPrincipal(authenticate.getPrincipal());
            authResponse.setAuthority(authenticate.getRole());
            return ResponseEntity.ok(authResponse);
        }

        authResponse.setMessage("Authenticate failed with API key : ".concat(apiKey)
                .concat(" and principal: ".concat(principal)));
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(authResponse);
    }

    @PostMapping("register")
    public ResponseEntity<AuthDTO> register(
            @RequestHeader(value = X_API_KEY_HEADER, required = false) String apiKey,
            @RequestHeader(value = X_PRINCIPAL_HEADER, required = false) String principal,
            @RequestBody InternalAPIKeyDTO request
    ) {
        var authResponse = new RegisterKeyDTO();

        if(Objects.isNull(apiKey)) {
            authResponse.setSuccess(false);
            authResponse.setMessage("API key credential is required");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(authResponse);
        }

        if (Objects.isNull(principal)) {
            authResponse.setSuccess(false);
            authResponse.setMessage("API key principal is required");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(authResponse);
        }

        final var authenticate = this.apiKeyService.authenticate(apiKey, principal);

        if(Objects.isNull(authenticate)) {
            authResponse.setMessage("Register key failed due to false authentication");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(authResponse);
        }

        if(!ADMIN_AUTHORITY.equals(authenticate.getRole())) {
            authResponse.setSuccess(false);
            authResponse.setMessage("You are not allowed to register a API key");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(authResponse);
        }

        authResponse.setSuccess(true);
        authResponse.setPrincipal(authenticate.getPrincipal());
        authResponse.setAuthority(authenticate.getRole());
        authResponse.setRegisteredKey(this.apiKeyService.registerKey(request));
        return ResponseEntity.ok(authResponse);

    }
}
