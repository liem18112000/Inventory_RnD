package com.fromlabs.inventory.notificationservice.notification.beans.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fromlabs.inventory.notificationservice.notification.messages.MessageValueObject;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * Message value object mapper
 * @author Liem
 */
@Component
public class MessageValueObjectMapper {
    private final ObjectMapper mapper = new ObjectMapper();

    /**
     * Convert message to JSON-like string
     * @param object MessageValueObject
     * @return JSON string
     * @throws JsonProcessingException when it failed to covert object to JSON string
     */
    public String toJSON(final @NotNull MessageValueObject object)
            throws JsonProcessingException {
        return this.mapper.writeValueAsString(object);
    }

    /**
     * Convert message to object
     * @param json JSON string
     * @return MessageValueObject
     * @throws JsonProcessingException when it failed to covert JSON string to object
     */
    public MessageValueObject toObject(final @NotBlank String json)
            throws JsonProcessingException {
        return this.mapper.readValue(json, MessageValueObject.class);
    }
}
