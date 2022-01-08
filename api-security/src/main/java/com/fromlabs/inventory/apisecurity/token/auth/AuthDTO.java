package com.fromlabs.inventory.apisecurity.token.auth;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AuthDTO implements Serializable {

    public static final long serialVersionUID = 6636621521277912356L;

    protected boolean success;

    protected String message;

    protected String principal;

    protected String authority;
}
