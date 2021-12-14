package com.fromlabs.inventory.notificationservice.utility;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;

import java.net.InetAddress;

/**
 * Transaction Logic Layer
 */
@Slf4j
@UtilityClass
public class TransactionLogic {

    /**
     * Log out Http path with extra information of service
     * @param method        HTTP method
     * @param servicePath   Service path
     * @param subPath       Sub path
     * @return              String
     */
    public String path(HttpMethod method, String servicePath, String subPath, String apiVersion) {
        return method.name().concat(" ")
                .concat(InetAddress.getLoopbackAddress().getCanonicalHostName().trim())
                .concat(servicePath.trim()).concat(subPath.trim()).concat(" ")
                .concat(apiVersion.trim()).trim();
    }
}
