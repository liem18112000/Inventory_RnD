package com.fromlabs.inventory.apisecurity.token.service;

import com.fromlabs.inventory.apisecurity.ApiSecurityApplication;
import com.fromlabs.inventory.apisecurity.token.InternalAPIKeyEntity;
import com.fromlabs.inventory.apisecurity.token.InternalAPIKeyEntityRepository;
import com.fromlabs.inventory.apisecurity.token.mapper.InternalAPIKeyMapper;
import com.fromlabs.inventory.apisecurity.token.service.hashstrategy.InternalAPIKeyHashStrategy;
import com.fromlabs.inventory.apisecurity.token.service.matchstrategy.InternalAPIKeyMatchStrategy;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@ActiveProfiles("dev")
@SpringBootTest(webEnvironment = RANDOM_PORT, classes = ApiSecurityApplication.class)
class InternalAPIKeyServiceImplTest {

    @Autowired
    private InternalAPIKeyMatchStrategy matchStrategy;

    @Mock
    private InternalAPIKeyMatchStrategy mockMatchStrategy;

    @Autowired
    private InternalAPIKeyHashStrategy hashStrategy;

    @Autowired
    private InternalAPIKeyMapper mapper;

    @Mock
    private InternalAPIKeyEntityRepository mockRepo;

    @Autowired
    private InternalAPIKeyService service;

    @Test
    void given_authorize_when_apiKeyIsNull_authorizeFailed() {
        assertFalse(this.service.authorize(null));
    }

    @Test
    void given_authorize_when_apiKeyIsBlank_authorizeFailed() {
        assertFalse(this.service.authorize(""));
    }

    @Test
    void given_authorize_when_apiKeyIsNotExist_authorizeFailed() {
        when(this.mockRepo.findAll()).thenReturn(List.of());
        var mockService = new InternalAPIKeyServiceImpl(mockRepo, mapper,
                matchStrategy, hashStrategy);
        assertFalse(mockService.authorize("NotExist"));
    }

    @Test
    void given_authorize_when_getApiKeyWithException_authorizeFailed() {
        when(this.mockRepo.findAll()).thenThrow(RuntimeException.class);
        var mockService = new InternalAPIKeyServiceImpl(mockRepo, mapper,
                matchStrategy, hashStrategy);
        assertFalse(mockService.authorize("NotExist"));
    }

    @Test
    void given_authorize_when_apiKeyIsExistAndNotMatchHashCode_authorizeFailed() {
        var key = new InternalAPIKeyEntity();
        key.setHashedApiKey("hashedAPI");
        when(this.mockRepo.findAll()).thenReturn(List.of(key));
        when(this.mockMatchStrategy.isMatch("apiKey", key.getHashedApiKey()))
                .thenReturn(false);
        var mockService = new InternalAPIKeyServiceImpl(mockRepo, mapper,
                mockMatchStrategy, hashStrategy);
        assertFalse(mockService.authorize("apiKey"));
    }

    @Test
    void given_authorize_when_apiKeyIsExistAndMatchHashCode_authorizeSuccess() {
        var key = new InternalAPIKeyEntity();
        key.setHashedApiKey("hashedAPI");
        when(this.mockRepo.findAll()).thenReturn(List.of(key));
        when(this.mockMatchStrategy.isMatch("apiKey", key.getHashedApiKey()))
                .thenReturn(true);
        var mockService = new InternalAPIKeyServiceImpl(mockRepo, mapper,
                mockMatchStrategy, hashStrategy);
        assertTrue(mockService.authorize("apiKey"));
    }

    @Test
    void given_authenticate_when_apiKeyIsNull_authenticateFailed() {
        assertNull(this.service.authenticate(null, "Principal"));
    }

    @Test
    void given_authenticate_when_apiKeyIsBlank_authenticateFailed() {
        assertNull(this.service.authenticate("", "Principal"));
    }

    @Test
    void given_authenticate_when_principalIsNull_authenticateFailed() {
        assertNull(this.service.authenticate("Key", null));
    }

    @Test
    void given_authenticate_when_principalIsBlank_authenticateFailed() {
        assertNull(this.service.authenticate("Key", ""));
    }

    @Test
    void given_authenticate_when_apiKeyIsNotExist_authenticateFailed() {
        when(this.mockRepo.findAll()).thenReturn(List.of());
        var mockService = new InternalAPIKeyServiceImpl(mockRepo, mapper,
                matchStrategy, hashStrategy);
        assertNull(mockService.authenticate("NotExist", "Principal"));
    }

    @Test
    void given_authenticate_when_getApiKeyWithException_authenticateFailed() {
        when(this.mockRepo.findAll()).thenThrow(RuntimeException.class);
        var mockService = new InternalAPIKeyServiceImpl(mockRepo, mapper,
                matchStrategy, hashStrategy);
        assertNull(mockService.authenticate("NotExist", "Principal"));
    }

    @Test
    void given_authenticate_when_apiKeyIsExistAndNotMatchHashCode_authenticateFailed() {
        var key = new InternalAPIKeyEntity();
        key.setHashedApiKey("hashedAPI");
        key.setAuthorityPrincipal("Principal");
        when(this.mockRepo.findAll()).thenReturn(List.of(key));
        when(this.mockMatchStrategy.isMatch("apiKey", key.getHashedApiKey()))
                .thenReturn(false);
        var mockService = new InternalAPIKeyServiceImpl(mockRepo, mapper,
                mockMatchStrategy, hashStrategy);
        assertNull(mockService.authenticate("apiKey", "Principal"));
    }

    @Test
    void given_authenticate_when_apiKeyIsExistAndMatchHashCode_authenticateSuccess() {
        var key = new InternalAPIKeyEntity();
        key.setHashedApiKey("hashedAPI");
        key.setAuthorityPrincipal("Principal");
        when(this.mockRepo.findAll()).thenReturn(List.of(key));
        when(this.mockMatchStrategy.isMatch("apiKey", key.getHashedApiKey()))
                .thenReturn(true);
        var mockService = new InternalAPIKeyServiceImpl(mockRepo, mapper,
                mockMatchStrategy, hashStrategy);
        final var result = mockService.authenticate(
                "apiKey", "Principal");
        assertNotNull(result);;
    }

    @Test
    void registerKey() {
    }
}