package com.fromlabs.inventory.inventoryservice.common.template;

import java.util.concurrent.Callable;

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
 * @see TemplateProcess
 * @see com.fromlabs.inventory.inventoryservice.common.template.TemplateProcess
 * @see <a href="https://gpcoder.com/4810-huong-dan-java-design-pattern-template-method/">
 *     Java Design Pattern Template Method in Java</a>
 * @see <a href="https://refactoring.guru/design-patterns/strategy/java/example">
 *     Java Design Pattern Strategy in Java</a>
 * @see <a href="https://howtodoinjava.com/design-patterns/creational/builder-pattern-in-java/">
 *     Java Design Pattern Builder in Java</a>
 * @see Callable
 * @see java.util.function.Supplier
 */
public interface WebCheckBeforeAfterTemplateProcess extends TemplateProcess{
    /**
     * Transaction on before process
     * @return Object
     */
    Object before();

    /**
     * Transaction on after process
     * @return Object
     */
    Object after();
}
