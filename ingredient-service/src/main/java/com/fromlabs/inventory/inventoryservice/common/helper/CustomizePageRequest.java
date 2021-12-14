package com.fromlabs.inventory.inventoryservice.common.helper;

import org.springframework.data.domain.Pageable;

public interface CustomizePageRequest {
    int MIN_PAGE_VALUE = 0;
    int MAX_SIZE_VALUE = Integer.MAX_VALUE;
    Pageable getPageable();
    default int filterSize(int size){
        return size <= 0 ? MAX_SIZE_VALUE : size;
    }
    default int filterPage(int page){
        return Math.max(page, MIN_PAGE_VALUE);
    }
}
