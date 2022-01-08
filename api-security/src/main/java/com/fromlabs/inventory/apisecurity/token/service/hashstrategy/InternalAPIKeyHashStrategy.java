package com.fromlabs.inventory.apisecurity.token.service.hashstrategy;

public interface InternalAPIKeyHashStrategy {

    String hash(String rawValue) throws IllegalArgumentException;
}
