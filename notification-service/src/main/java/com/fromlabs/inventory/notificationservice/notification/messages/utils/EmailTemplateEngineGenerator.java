package com.fromlabs.inventory.notificationservice.notification.messages.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Map;
import java.util.Objects;

@Component
public class EmailTemplateEngineGenerator {

    @Autowired
    private SpringTemplateEngine templateEngine;

    public String generateHTMLBody(
            final String templateFile,
            final Map<String, Object> model) {
        if (!StringUtils.hasText(templateFile)) {
            throw new IllegalArgumentException("Template file is null or empty");
        }

        if (Objects.isNull(model)) {
            throw new IllegalArgumentException("Model is null");
        }

        var context = new Context();
        context.setVariables(model);
        return templateEngine.process(templateFile, context);
    }
}
