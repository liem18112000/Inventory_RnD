package com.fromlabs.inventory.apisecurity.token.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@Getter
public class InternalAPIKeyDTO implements Serializable {

    public static final long serialVersionUID = -1189116327846468768L;

    @Builder(toBuilder = true)
    public InternalAPIKeyDTO(Long id, String name, String principal, String role, String keyHashed) {
        this.id = id;
        this.name = name;
        this.principal = principal;
        this.role = role;
        this.keyHashed = keyHashed;
    }

    protected Long id;

    protected String name;

    protected String principal;

    @JsonProperty("authority")
    protected String role;

    @JsonProperty("key")
    protected String keyHashed;

}
