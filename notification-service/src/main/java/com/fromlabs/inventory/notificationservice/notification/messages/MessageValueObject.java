package com.fromlabs.inventory.notificationservice.notification.messages;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Builder;
import lombok.Data;
import lombok.SneakyThrows;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * Base message value object
 * @author Liem
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MessageValueObject implements Serializable {
    private static final long serialVersionUID = -7759702696942353240L;

    protected final String subject;

    protected final String body;

    protected final String sendAt;

    protected final String from;

    protected final String to;

    @Builder(builderMethodName = "messageBuilder")
    public MessageValueObject(
            final @NotBlank String subject,
            final @NotBlank String body,
            final @NotBlank String sendAt,
            final @NotBlank String from,
            final @NotBlank String to
    ) {
        this.subject = subject;
        this.body = body;
        this.sendAt = sendAt;
        this.from = from;
        this.to = to;
    }
}
