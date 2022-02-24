package com.fromlabs.inventory.inventoryservice.client.notification.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Base message value object
 * @author Liem
 */
@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class MessageValueObject implements Serializable {
    private static final long serialVersionUID = 8648686753621439851L;

    protected String subject;

    protected String body;

    protected String sendAt;

    protected String from;

    protected String to;
}
