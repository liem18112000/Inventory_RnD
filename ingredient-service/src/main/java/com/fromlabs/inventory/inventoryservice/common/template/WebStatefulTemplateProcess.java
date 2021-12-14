/*
 * Copyright (c) 2021. Liem Doan
 */

package com.fromlabs.inventory.inventoryservice.common.template;

import com.fromlabs.inventory.inventoryservice.common.template.manager.TemplateProcessCacheManger;
import com.fromlabs.inventory.inventoryservice.common.validator.RequestValidator;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;

import java.io.Serializable;
import java.util.Objects;
import java.util.concurrent.Callable;

import static org.springframework.http.ResponseEntity.ok;

/**
 * Template process helper for general process.
 * <p>
 *     This helper is inspired to solve the issue of duplicate of a procedure
 *     which each step is repeated with a little modification in some steps
 * </p>
 * <p>
 *     The new template process with the statefull implemetation as the request data
 *     will be injected to a variable. The bootstrap stage will be the placve where request state
 *     data is initialized
 * </p>
 * The Behavioral pattern - Template Method is used to support the core idea to
 * structure a predefined procedure
 * <p>
 * The Behavioral pattern - Strategy is as a form of make function as an argument
 * (Callable, Supplier)
 * <p>
 * The Structural pattern - Builder is used for supporting the concept of customize
 * each step in the process of template method
 * <p>
 *  In this design, there are four three operator (steps) will be defined:
 *  <p>
 *  <ul>
 *      <li>First step - Validate web input (header, param, path variable and request body) :
 *      call at the start of the web controller process.
 *      Continue to next step (process) if it is true and return an Object when it is false</li>
 *      <li>Second step - Before Process : this step indicate some things must be done before
 *      the process step come in. For example, it can be a method for check some constraint that
 *      validate could not or too expensive to do.</li>
 *      <li>Third step - Business Process Logic : call after validation step.
 *      Return an object after done processing (the process is customizable as
 *      users can insert their function in)</li>
 *      <li>Forth step - After Process : this step indicate some things must be done after
 *      the process step come in. For example, it can be a method for check some constraint
 *      which check a condition when process is applied</li>
 *      <li>Optional Step - Log the status of process : can be called any where (on the customization
 *      of the template method)</li>
 *      <li>Optional Step - Bootstrap : is called before all steps and process (on the customization
 *      of the template method)</li>
 *  </ul>
 *  <p>
 *  <p>Moreover, the request data state of the process may be kept in the state properties so that
 *  the template process manager can track the change in data and rebuild the process</p>
 * Example: Save Ingredient object
 * <p>
 * <pre>
 *     public ResponseEntity saveIngredient(
 *             Long tenantId,
 *             IngredientRequest request
 *     ){
 *         request.setClientId(tenantId);
 *         return (ResponseEntity) WebTemplateProcessWithCheckBeforeAfter.WebCheckBuilder()
 *                 .state(request)
 *                 .validate(() -&gt; this.validateIngredient(request, false))
 *                 .before(() -&gt; Objects.isNull(this.ingredientService.get(request.getCode())))
 *                 .process(() -&gt; new ResponseEntity(this.ingredientService.save(IngredientEntity.from(request, false).createAtNow()), CREATED))
 *                 .after(() -&gt; Objects.nonNull(this.ingredientService.get(request.getCode()))).build().run();
 *     }
 * </pre>
 * <p>
 * Example: Get Ingredient object by ID
 * <p>
 * <pre>
 *     public ResponseEntity getIngredient(
 *             Long tenantId,
 *             Long id
 *     ){
 *         return (ResponseEntity) WebTemplateProcessWithCheckBeforeAfter.WebCheckBuilder()
 *                 .validate(() -&gt; this.validateTenantAndId(tenantId, id))
 *                 .before(() -&gt; Objects.isNull(this.ingredientService.get(id)))
 *                 .process(() -&gt; ok(this.ingredientService.get(id)))
 *                 .after(() -&gt; Boolean.TRUE).build().run();
 *     }
 * </pre>
 * <p>
 * @see TemplateProcess
 * @see TemplateProcessCacheManger
 * @see <a href="https://gpcoder.com/4810-huong-dan-java-design-pattern-template-method/">
 *     Java Design Pattern Template Method in Java</a>
 * @see <a href="https://refactoring.guru/design-patterns/strategy/java/example">
 *     Java Design Pattern Strategy in Java</a>
 * @see <a href="https://howtodoinjava.com/design-patterns/creational/builder-pattern-in-java/">
 *     Java Design Pattern Builder in Java</a>
 * @see Builder
 */
@Slf4j
public class WebStatefulTemplateProcess extends WebTemplateProcessWithCheckBeforeAfter implements Serializable {

    protected Object templateProcessDataState;

    /**
     * Constructor as a builder for web template process
     * @param validate  Validate function for validate step (optional)
     * @param process   Business Process function for main operation (required)
     * @param bootstrap Transact function for bootstrap operation (optional)
     * @param before    Transact function before main operation (optional)
     * @param after     Transact function after main operation (optional)
     */
    @Builder(builderMethodName = "statefulWebCheckBuilder")
    WebStatefulTemplateProcess(Callable<?> validate, Callable<?> process, Callable<?> bootstrap,
                               Callable<Boolean> before, Callable<Boolean> after) {
        super(validate, process, bootstrap, before, after);
    }

    /**
     * Check template process state whether change or not
     * @param newState  Template process data state
     * @return          true if state change otherwise false
     */
    public boolean isStateChanged(Object newState) {
        assert Objects.nonNull(this.templateProcessDataState);
        final var isChanged = !this.templateProcessDataState.equals(newState);
        log.info("Template process data changed check: {}", isChanged);
        return isChanged;
    }

    /**
     * Override template method for this special case with check before
     * and after a process
     * @return ResponseEntity
     */
    public ResponseEntity<?> templateMethod() {

        this.log("Template Process Starting...");
        final var validate = this.validate;
        final var process = this.process;
        final var beforeSave = this.before;
        final var afterSave = this.after;
        final var boostrap = this.bootstrap;

        this.log("Check setup...");
        boolean isBootstrapSetup = Objects.nonNull(boostrap);
        boolean isValidateSetup = Objects.nonNull(validate);
        boolean isProcessSetup = Objects.nonNull(process);
        boolean isAfterSaveSetup = Objects.nonNull(afterSave);
        boolean isBeforeSaveSetup = Objects.nonNull(beforeSave);

        Object boostrapObj = null;
        if(isBootstrapSetup) {
            this.log("Bootstrap...");
            boostrapObj = this.bootstrap();
            this.templateProcessDataState = boostrapObj;
            log.info("Template process state initialized : {}", this.templateProcessDataState);
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
            if(!isBeforeSaveSetup) logger.warn("Check before process is not setup");
            else{
                this.log("Before process...");
                final var checkBeforeSave = this.before();
                if(!(checkBeforeSave instanceof Boolean)) return (ResponseEntity<?>) checkBeforeSave;
            }

            this.log("Processing...");
            final var result = this.process();

            this.log("After process...");
            if(!isAfterSaveSetup) logger.warn("Check after process is not setup");
            else{
                final var checkAfterSave = this.after();
                if(!(checkAfterSave instanceof Boolean)) return (ResponseEntity<?>) checkAfterSave;
            }

            this.log("Get Result...");
            return result;
        }

        this.log("Get Result...");
        return ok().build();
    }
}
