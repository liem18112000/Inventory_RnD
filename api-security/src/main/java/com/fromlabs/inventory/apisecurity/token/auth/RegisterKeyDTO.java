package com.fromlabs.inventory.apisecurity.token.auth;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fromlabs.inventory.apisecurity.token.InternalAPIKeyDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RegisterKeyDTO extends AuthDTO {

    public static final long serialVersionUID = 6742697356111528912L;

    @JsonProperty("registered_key")
    private InternalAPIKeyDTO registeredKey;
}
