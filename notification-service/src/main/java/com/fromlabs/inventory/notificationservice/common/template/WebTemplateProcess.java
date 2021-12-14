/*
  Copyright (c) 2021.
  @author Liem Doan
 */

package com.fromlabs.inventory.notificationservice.common.template;

import com.fromlabs.inventory.notificationservice.common.exception.FailValidateException;
import com.fromlabs.inventory.notificationservice.common.validator.RequestValidator;
import com.fromlabs.inventory.notificationservice.common.exception.FailValidateException;
import com.fromlabs.inventory.notificationservice.common.validator.RequestValidator;
import lombok.Builder;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;

import java.util.Objects;
import java.util.concurrent.Callable;

import static com.fromlabs.inventory.notificationservice.common.exception.handler.RequestExceptionHandler.ServerExceptionHandler;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.ResponseEntity.ok;

/**
 * Template process helper for general process.
 * This helper is inspired to solve the issue of duplicate of a procedure
 * which each step is repeated with a little modification in some steps
 * <p>
 * The Behavioral pattern - Template Method is used to support the core idea to
 * structure a predefined procedure
 * <p>
 * The Behavioral pattern - Strategy is as a form of make function as an argument
 * (Callable, Supplier)
 * <p>
 * The Structural pattern - Builder is used for supporting the concept of customize
 * each step in the process of template method
 * <p>
 *  In this simple design, there are four three operator (steps) will be defined:
 *  <p>
 *  <ul>
 *      <li>First step - Validate web input (header, param, path variable and request body) :
 *      call at the start of the web controller process.
 *      Continue to next step (process) if it is true and return an Object when it is false</li>
 *      <li>Second step - Business Process Logic : call after validation step.
 *      Return an object after done processing (the process is customizable as
 *      users can insert their function in)</li>
 *      <li>Optional Step - Log the status of process : can be called any where (on the customization
 *      of the template method)</li>
 *      <li>Optional Step - Bootstrap : is called before all steps and process (on the customization
 *      of the template method)</li>
 *  </ul>
 *  <p>
 * Example: Get page of Ingredient object
 * <p>
 * <pre>
 *     public ResponseEntity getPageIngredientCategory(
 *             Long tenantId,
 *             IngredientPageRequest request
 *     ){
 *         request.setClientId(tenantId);
 *         return (ResponseEntity) WebTemplateProcess.builder()
 *                 .validate(() -&gt; this.validateTenant(request.getClientId()))
 *                 .process(() -&gt; ok(IngredientDto.from(this.ingredientService.getPage(request.getClientId(), request.getPageable()))))
 *                 .build().run();
 *     }
 * </pre>
 * <p>
 * Example: Get list of Ingredient object
 * <p>
 * <pre>
 *     public ResponseEntity getAllIngredientCategory(
 *             Long tenantId
 *     ){
 *         return (ResponseEntity) WebTemplateProcess.builder()
 *                 .validate(() -&gt; this.validateTenant(tenantId))
 *                 .process(() -&gt; ok(IngredientDto.from(this.ingredientService.getAll(tenantId))))
 *                 .build().run();
 *     }
 * </pre>
 * <p>
 * @see com.fromlabs.inventory.notificationservice.common.template.TemplateProcess
 * @see <a href="https://gpcoder.com/4810-huong-dan-java-design-pattern-template-method/">
 *     Java Design Pattern Template Method in Java</a>
 * @see <a href="https://refactoring.guru/design-patterns/strategy/java/example">
 *     Java Design Pattern Strategy in Java</a>
 * @see <a href="https://howtodoinjava.com/design-patterns/creational/builder-pattern-in-java/">
 *     Java Design Pattern Builder in Java</a>
 * @see Callable
 * @see java.util.function.Supplier
 * @see Builder
 */
@Data
@Builder(toBuilder = true)
public class WebTemplateProcess implements TemplateProcess {
    protected Callable<?> validate;
    protected Callable<?> process;
    protected Callable<?> bootstrap;
    protected final Boolean log = true;
    protected final Logger logger = LoggerFactory.getLogger(WebTemplateProcess.class);

    /**
     * Bootstrap occur before all process
     * @return Object
     */
    public Object bootstrap() {
        try {return this.bootstrap.call();
        } catch (Exception e) {
            logger.error("Bootstrap exception", e);
            return ServerExceptionHandler().getResponse(e, BAD_REQUEST);
        }
    }

    /**
     * Validate web input and return of RequestValidator if validation is passed or else ResponseEntity
     * @return Object
     * @see RequestValidator
     * @see ResponseEntity
     */
    public Object validate() {
        try {return this.validate.call();
        } catch (Exception e) {
            logger.error("Validate exception", e);
            return ServerExceptionHandler().getResponse(e, BAD_REQUEST);
        }
    }

    /**
     * Handle on validation is not passed and return a ResponseEntity object with
     * FailValidateException and code 400
     * @param validator the provided RequestValidator
     * @return Object
     * @see FailValidateException
     * @see RequestValidator
     */
    public Object onValidateFail(RequestValidator validator) {
        return new ResponseEntity<>(validator.getErrors(new FailValidateException()), BAD_REQUEST);
    }

    /**
     * Return a ResponseEntity object on process result and may produce Exception
     * if there is something wrong
     * @return ResponseEntity
     * @see ResponseEntity
     * @see Exception
     * @see com.fromlabs.inventory.notificationservice.common.exception.handler.RequestExceptionHandler
     */
    public ResponseEntity<?> process() {
        try{ return (ResponseEntity<?>) this.process.call();
        } catch (Exception e){return ServerExceptionHandler().getResponse(e, BAD_REQUEST);}
    }

    /**
     * Log everything where it is set up
     * @param obj Log object
     */
    public void log(Object obj) {
        if(this.log) logger.info(String.valueOf(obj));
    }

    /**
     * Templated method
     * @return ResponseEntity
     * @see ResponseEntity
     * @see RequestValidator
     * @see com.fromlabs.inventory.notificationservice.common.exception.handler.RequestExceptionHandler
     */
    public ResponseEntity<?> templateMethod() {

        this.log("Template Process Starting...");
        final var boostrap = this.bootstrap;
        final var validate = this.validate;
        final var process = this.process;

        this.log("Check setup...");
        boolean isBootstrapSetup = Objects.nonNull(boostrap);
        boolean isValidateSetup = Objects.nonNull(validate);
        boolean isProcessSetup = Objects.nonNull(process);

        Object boostrapObj = null;
        if(isBootstrapSetup) {
            this.log("Bootstrap...");
            boostrapObj = this.bootstrap();
        }

        if(!isValidateSetup) logger.warn("Validate is not setup");
        else{
            this.log("Validating...");
            final var validator = this.validate();
            if(validator instanceof ResponseEntity) return (ResponseEntity<?>)validator;
            else if(((RequestValidator)validator).isInvalid()) return (ResponseEntity<?>) this.onValidateFail(((RequestValidator)validator));
        }

        if(!isProcessSetup) logger.warn("Process is not setup");
        else {
            this.log("Processing...");
            final var result = this.process();

            this.log("Get Result...");
            return result;
        }

        this.log("Get Result...");
        return ok().build();
    }
}
