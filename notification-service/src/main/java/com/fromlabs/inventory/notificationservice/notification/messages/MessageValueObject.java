package com.fromlabs.inventory.notificationservice.notification.messages;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Map;
import java.util.Objects;

/**
 * Base message value object
 * @author Liem
 */
@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class MessageValueObject implements Serializable {
    private static final long serialVersionUID = -7759702696942353240L;

    protected String subject;

    protected String body;

    protected String sendAt;

    protected String from;

    protected String to;

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

    public Map<String, Object> toMap() {
        return Map.of(
                "subject", this.getSubject(),
                "body", this.getBody(),
                "from", this.getFrom(),
                "to", this.getTo()
        );
    }
}
