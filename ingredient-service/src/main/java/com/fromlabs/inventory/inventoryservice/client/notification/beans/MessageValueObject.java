package com.fromlabs.inventory.inventoryservice.client.notification.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Base message value object
 * @author Liem
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MessageValueObject implements Serializable {
    private static final long serialVersionUID = -2098924596464123323L;

    protected String subject;

    protected String body;

    protected String sendAt;

    protected String from;

    protected String to;
}
