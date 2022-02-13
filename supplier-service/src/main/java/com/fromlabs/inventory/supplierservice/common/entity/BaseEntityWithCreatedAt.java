package com.fromlabs.inventory.supplierservice.common.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;

@MappedSuperclass
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class BaseEntityWithCreatedAt<ID extends Serializable> extends BaseEntity<ID> {

    @Column(name="create_at")
    protected String createdAt;
}
