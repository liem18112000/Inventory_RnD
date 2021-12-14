package com.fromlabs.inventory.notificationservice.common.helper;

import org.springframework.data.domain.Pageable;

public interface CustomizePageRequest {
    int MIN_PAGE_VALUE = 0;
    int MIN_SIZE_VALUE = 1;
    Pageable getPageable();
    default int filterSize(int size){
        return size <= 0 ? Integer.MAX_VALUE : size;
    }
    default int filterPage(int page){
        return Math.max(page, MIN_PAGE_VALUE);
    }
}
