package com.fromlabs.inventory.recipeservice.common.validator;

import com.fromlabs.inventory.recipeservice.common.exception.FailValidateException;
import lombok.Builder;
import lombok.Getter;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.time.Instant;
import java.util.Map;
import java.util.Set;

/**
 * Validation wrapper of information
 */
@Getter
@Builder
public class ValidationInfo {

    static public final String DEFAULT_MESSAGE = "Validation was failed due to some errors, see validator message to learn more";
    static public final Class<?> DEFAULT_EXCEPTION = FailValidateException.class;

    protected String exception;
    protected Map<String, Set<String>> validatorMessage;
    protected String exceptionMessage;
    protected String[] stackFrame;
    protected Instant timestamp;

    /**
     * Get information
     * @param throwable         an Exception
     * @param validatorMessage  validate message
     * @return ValidationInfo
     */
    static public ValidationInfo getInfo(Throwable throwable, Map<String, Set<String>> validatorMessage){
        return ValidationInfo.builder()
                .exception(throwable.getClass().getSimpleName())
                .validatorMessage(validatorMessage)
                .exceptionMessage(ExceptionUtils.getMessage(throwable))
                .stackFrame(ExceptionUtils.getStackFrames(throwable))
                .timestamp(Instant.now())
                .build();
    }

    /**
     * Get information
     * @param validatorMessage  validate message
     * @param customMessage     custom message
     * @return ValidationInfo
     */
    static public ValidationInfo getInfo(Map<String, Set<String>> validatorMessage,String customMessage){
        return ValidationInfo.builder()
                .exception(DEFAULT_EXCEPTION.getSimpleName())
                .validatorMessage(validatorMessage)
                .exceptionMessage(customMessage)
                .timestamp(Instant.now())
                .build();
    }

    /**
     *
     * @param validatorMessage validate message
     * @return ValidationInfo
     */
    static public ValidationInfo getInfo(Map<String, Set<String>> validatorMessage){
        return ValidationInfo.builder()
                .exception(DEFAULT_EXCEPTION.getSimpleName())
                .validatorMessage(validatorMessage)
                .exceptionMessage(DEFAULT_MESSAGE)
                .timestamp(Instant.now())
                .build();
    }
}
