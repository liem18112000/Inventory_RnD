/*
 * Copyright (c) 2021. Liem Doan
 */

package com.fromlabs.inventory.recipeservice.common.template;

import com.fromlabs.inventory.recipeservice.common.exception.FailValidateException;
import com.fromlabs.inventory.recipeservice.common.exception.ObjectNotFoundException;
import com.fromlabs.inventory.recipeservice.common.exception.handler.ExceptionInfo;
import com.fromlabs.inventory.recipeservice.common.exception.handler.RequestExceptionHandler;
import com.fromlabs.inventory.recipeservice.common.validator.RequestValidator;
import lombok.Builder;
import lombok.Data;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.function.Consumer;

import static com.fromlabs.inventory.recipeservice.common.exception.handler.RequestExceptionHandler.ServerExceptionHandler;

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
 *      <li>First step - Validate input : call at the start of the process.
 *      Continue to next step (process) if it is true and return an Object when it is false</li>
 *      <li>Second step - Process for test logic : call after utility step.
 *      Return an object after done processing (the process is customizable as
 *      users can insert their function in)</li>
 *      <li>Third step - Assertion : call after test process step.
 *      Return an object after done assert (the process is customizable as
 *      users can insert their function in)</li>
 *      <li>Optional Step - Log the status of process : can be called any where (on the customization
 *      of the template method)</li>
 *      <li>Optional Step - Bootstrap : is called before all steps and process (on the customization
 *      of the template method)</li>
 *  </ul>
 *  <p>
 * @see <a href="https://www.guru99.com/unit-testing-guide.html">
 *     Unit test overview</a>
 * @see <a href="https://gpcoder.com/4810-huong-dan-java-design-pattern-template-method/">
 *     Java Design Pattern Template Method in Java</a>
 * @see <a href="https://refactoring.guru/design-patterns/strategy/java/example">
 *     Java Design Pattern Strategy in Java</a>
 * @see <a href="https://howtodoinjava.com/design-patterns/creational/builder-pattern-in-java/">
 *     Java Design Pattern Builder in Java</a>
 * @see Callable
 * @see java.util.function.Supplier
 * @see TemplateProcess
 */
@Data
@Builder(toBuilder = true)
public class UnitTestTemplateProcess implements TestTemplateProcess {

    protected Callable<?> validate;
    protected Callable<?> process;
    protected Callable<?> bootstrap;
    protected Consumer<Object> asserts;
    protected final Boolean log = true;
    protected final Logger logger = LoggerFactory.getLogger(UnitTestTemplateProcess.class);
    static protected Map<String, Object> inputMap = new HashMap<>();

    /**
     * Bootstrap occur before all process
     * @return Object
     */
    public Object bootstrap() {
        try {return this.bootstrap.call();
        } catch (Exception e) {
            logger.error("Bootstrap exception", e);
            return ServerExceptionHandler().getRawResponse(e);
        }
    }

    /**
     * Validate web input and return of RequestValidator if utility is passed or else ResponseEntity
     * @return Object
     * @see com.fromlabs.inventory.recipeservice.common.validator.RequestValidator
     * @see ResponseEntity
     */
    public Object validate() {
        try {return this.validate.call();
        } catch (Exception e) {
            logger.error("Validate exception", e);
            return ServerExceptionHandler().getRawResponse(e);
        }
    }

    /**
     * Handle on utility is not passed and return a ResponseEntity object with
     * FailValidateException and code 400
     * @param validator the provided RequestValidator
     * @return Object
     * @see FailValidateException
     * @see RequestValidator
     */
    public Object onValidateFail(RequestValidator validator) {
        return validator.getErrors(new FailValidateException());
    }

    /**
     * Return a ResponseEntity object on process result and may produce Exception
     * if there is something wrong
     * @return Object
     * @see Exception
     * @see RequestExceptionHandler
     */
    public Object process() {
        try{ return this.process.call();
        } catch (Exception e){
            var error = ServerExceptionHandler().getRawResponse(e);
            logger.error("Exception in process", error);
            return error;
        }
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
     * @return Object
     * @see RequestValidator
     * @see RequestExceptionHandler
     * @see ExceptionInfo
     */
    public Object templateMethod() {

        this.log("Template Process Starting...");
        final var boostrap = this.bootstrap;
        final var validate = this.validate;
        final var process = this.process;
        final var asserts = this.asserts;

        this.log("Check setup...");
        boolean isBootstrapSetup = Objects.nonNull(boostrap);
        boolean isValidateSetup = Objects.nonNull(validate);
        boolean isProcessSetup = Objects.nonNull(process);
        boolean isAssertSetup = Objects.nonNull(asserts);

        Object boostrapObj = null;
        if(isBootstrapSetup) {
            this.log("Bootstrap...");
            boostrapObj = this.bootstrap();
        }

        if(isValidateSetup){
            this.log("Validating...");
            final var validator = this.validate();
            if(validator instanceof ExceptionInfo) return validator;
            else if(((RequestValidator)validator).isInvalid()) return this.onValidateFail(((RequestValidator)validator));
        }

        Object assertResult = null;
        if(!isProcessSetup) logger.warn("Process is not setup");
        else {
            this.log("Processing...");
            final var result = this.process();

            this.log("Asserts...");
            if(!isAssertSetup) logger.warn("Asserts are not setup");
            else assertResult = this.asserts(result);
        }

        this.log("Asserts done...");
        return assertResult;
    }

    public Object asserts(Object obj) {
        try {
            if(obj instanceof ExceptionInfo) return false;
            this.asserts.accept(obj);
            return true;
        } catch (Exception e) {return ServerExceptionHandler().getRawResponse(e);}
    }

    public UnitTestTemplateProcess input(String key, Object value) {
        if(inputMap.containsKey(key)) inputMap.replace(key, value);
        else inputMap.put(key, value);
        return this;
    }

    @SneakyThrows
    static public Object getInput(String key) {
        if(!inputMap.containsKey(key)) throw new ObjectNotFoundException("Input is not found with key");
        return inputMap.get(key);
    }
}
