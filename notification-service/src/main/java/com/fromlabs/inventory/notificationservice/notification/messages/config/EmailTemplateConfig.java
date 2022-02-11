package com.fromlabs.inventory.notificationservice.notification.messages.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.thymeleaf.templateresolver.ITemplateResolver;

@Configuration
@RequiredArgsConstructor(onConstructor = @__({@Autowired}))
public class EmailTemplateConfig {

    public static final String THYMELEAF_TEMPLATE_RESOLVER = "thymeleafTemplateResolver";

    final Environment environment;

    @Bean(name = THYMELEAF_TEMPLATE_RESOLVER)
    public ITemplateResolver thymeleafTemplateResolver() {
        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setPrefix(environment.getProperty(
                "template.email.path", "/templates/"));
        templateResolver.setSuffix(environment.getProperty(
                "template.email.format", ".html"));
        templateResolver.setTemplateMode(environment.getProperty(
                "template.email.mode", "HTML"));
        templateResolver.setCharacterEncoding("UTF-8");
        return templateResolver;
    }

    @Bean
    public SpringTemplateEngine thymeleafTemplateEngine(
            @Qualifier(THYMELEAF_TEMPLATE_RESOLVER) ITemplateResolver templateResolver) {
        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        templateEngine.setTemplateResolver(templateResolver);
        return templateEngine;
    }
}
