package com.fromlabs.inventory.notificationservice.notification.messages.utils;

import com.fromlabs.inventory.notificationservice.notification.messages.MessageValueObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Map;

@Component
public class EmailTemplateEngineGenerator {

    @Autowired
    private SpringTemplateEngine templateEngine;

    public String generateHTMLBody(
            final @NotNull @NotBlank String templateFile,
            final @NotNull Map<String, Object> model) {
        var context = new Context();
        context.setVariables(model);
        return templateEngine.process(templateFile, context);
    }
}
