package com.fromlabs.inventory.notificationservice.notification.messages;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Builder(toBuilder = true)
@Data
public class NotificationMessage<V extends Serializable> implements Serializable {

    @NotNull
    protected final UUID id = UUID.randomUUID();

    protected final V content;

    protected final LocalDateTime timestamp = LocalDateTime.now();

    protected final String messageOrigin;

    protected final long priority = 0;


}
