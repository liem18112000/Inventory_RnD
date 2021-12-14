package com.fromlabs.inventory.inventoryservice.common.exception.handler;

import com.fromlabs.inventory.inventoryservice.common.validator.CriteriaImpl;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Customize Configuration for request exception handler
 * @see RequestExceptionHandler
 * @see HttpStatus
 */
public class RequestExceptionHandlerConfiguration implements ExceptionHandlerConfiguration {

    protected Map<String, CriteriaImpl.Pair<HttpStatus, Set<String>>> container = new HashMap<>();

    /**
     * Get status with Http status and exception name
     * @param sts           HttpStatus
     * @param exceptionName String of Exception name
     * @return ExceptionHandlerConfiguration
     * @see ExceptionHandlerConfiguration
     */
    public ExceptionHandlerConfiguration status(HttpStatus sts, String exceptionName) {
        if(this.container.containsKey(exceptionName))
            this.container.get(exceptionName).setKey(sts);
        else this.container.put(exceptionName, new CriteriaImpl.Pair<>(sts, Set.of()));
        return this;
    }

    /**
     * Get instruction with exception name
     * @param instruction   String of extra instruction
     * @param exceptionName String of Exception name
     * @return ExceptionHandlerConfiguration
     * @see ExceptionHandlerConfiguration
     */
    public ExceptionHandlerConfiguration instruction(String instruction, String exceptionName) {
        if(this.container.containsKey(exceptionName)){
            if(!this.container.get(exceptionName).getValue().isEmpty())
                this.container.get(exceptionName).getValue().add(instruction);
            else this.container.get(exceptionName).setValue(Set.of(instruction));
        } else this.container.put(exceptionName, new CriteriaImpl.Pair<>(null, Set.of(instruction)));
        return this;
    }

    /**
     * Get status with http status and throwable
     * @param sts           HttpStatus
     * @param throwable     an Exception
     * @return ExceptionHandlerConfiguration
     * @see HttpStatus
     * @see Throwable
     */
    public ExceptionHandlerConfiguration status(HttpStatus sts, Throwable throwable) {
        return this.status(sts, throwable.getClass().getName());
    }

    /**
     * Get instruction with throwable
     * @param instruction   String of extra instruction
     * @param throwable     an Exception
     * @return ExceptionHandlerConfiguration
     * @see ExceptionHandlerConfiguration
     * @see Throwable
     */
    public ExceptionHandlerConfiguration instruction(String instruction, Throwable throwable) {
        return this.instruction(instruction, throwable.getClass().getName());
    }

    /**
     * Get status with Exception name
     * @param exceptionName String of Exception name
     * @return HttpStatus
     * @see HttpStatus
     */
    public HttpStatus getStatus(String exceptionName){
        if(this.container.containsKey(exceptionName)){
            if(this.container.get(exceptionName).getKey() == null){
                this.status(HttpStatus.BAD_REQUEST, exceptionName);
            }else{
                return this.container.get(exceptionName).getKey();
            }
        }
        return HttpStatus.BAD_REQUEST;
    }

    /**
     * Get instruction with exception
     * @param exceptionName String of Exception name
     * @return Set\&lt;String&gt;
     */
    public Set<String> getInstruction(String exceptionName){
        if(this.container.containsKey(exceptionName)){
            return this.container.get(exceptionName).getValue();
        }
        return Set.of("NA");
    }

    /**
     * Get status with throwable
     * @param throwable     an Exception
     * @return HttpStatus
     * @see HttpStatus
     * @see Throwable
     */
    public HttpStatus getStatus(Throwable throwable){
        return this.getStatus(throwable.getClass().getName());
    }

    /**
     * Get instruction with throwable
     * @param throwable     an Exception
     * @return Set\&lt;String&gt;
     */
    public Set<String> getInstruction(Throwable throwable){
        return this.getInstruction(throwable.getClass().getName());
    }

}
