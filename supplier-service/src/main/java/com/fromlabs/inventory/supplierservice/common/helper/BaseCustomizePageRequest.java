package com.fromlabs.inventory.supplierservice.common.helper;

import lombok.Data;
import lombok.SneakyThrows;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Arrays;

@Data
public class BaseCustomizePageRequest implements CustomizePageRequest{

    // Sort constant
    static public final String ASC = "asc";
    static public final String DESC = "desc";

    // Pageable attributes
    protected int page = 0;
    protected int size = Integer.MAX_VALUE;
    protected String sort;

    @SneakyThrows
    public Pageable getPageable() {
        try{
            if(!FLStringUtils.isNullOrEmpty(this.sort))
                return PageRequest.of(filterPage(page), filterSize(size), this.getOrders());
            return PageRequest.of(filterPage(page), filterSize(size));
        } catch (Exception exception) { throw new Exception(exception.getMessage());}
    }

    protected Sort getOrders() {
        this.sort = this.sort.replaceAll("\\s+","");
        final var arr = Arrays.asList(this.sort.split(","));
        final var sortOrder = arr.get(arr.size() - 1);
        final var sortField = arr.get(0);
        Sort sorts = Sort.by(sortField);
        if(sortOrder.equals(ASC)) sorts = sorts.ascending();
        else if(sortOrder.equals(DESC)) sorts = sorts.descending();
        return sorts;
    }
}
