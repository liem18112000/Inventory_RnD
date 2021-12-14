/*
 * Copyright (c) 2021. Liem Doan
 */

package com.fromlabs.inventory.notificationservice.common.template;

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
 *  In this simple design, there are four three operator (steps) will be defined:
 *  <p>
 *  <ul>
 *      <li>First step - Validate input : call at the start of the process.
 *      Continue to next step (process) if it is true and return an Object when it is false</li>
 *      <li>Second step - Process for test logic : call after validation step.
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
public interface TestTemplateProcess extends TemplateProcess {
    Object asserts(Object obj);
}
