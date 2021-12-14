/*
 * Copyright (c) 2021. Liem Doan
 */

package com.fromlabs.inventory.inventoryservice.common.wrapper;

import com.fromlabs.inventory.inventoryservice.common.exception.ConstraintViolateException;
import com.fromlabs.inventory.inventoryservice.common.exception.ObjectNotFoundException;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;
import java.util.concurrent.Callable;

@Slf4j
public class ConstraintWrapper {

    public ConstraintWrapper() {
    }

    @Builder
    public ConstraintWrapper(String name, Throwable exception, Callable<Boolean> check) {
        this.name = name;
        this.exception = exception;
        this.check = check;
    }

    protected String name = "Constraint".concat(String.valueOf(System.currentTimeMillis()));
    protected Throwable exception;
    protected Callable<Boolean> check;

    public Boolean constraintCheck() {
        try {
            if(Objects.isNull(check)) {
                this.exception = new ObjectNotFoundException("Constraint check strategy is not found");
                log.error("{} : false - {}", this.name, exception.getLocalizedMessage());
                return false;
            }
            final var result =  check.call();
            if(result) log.info("{} : true", this.name);
            else {
                if(Objects.isNull(exception)) this.exception = new ConstraintViolateException(this.name);
                log.error("{} : false - {}", this.name, exception.getLocalizedMessage());
            }
            return result;
        } catch (Exception e) {
            this.exception = e;
            log.error("{} : false - {}", this.name, exception.getLocalizedMessage());
            return false;
        }
    }
}
