package com.fromlabs.inventory.notificationservice.common.logging;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StopWatch;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

@Aspect
@Component
@Slf4j
public class ProcessLoggingHandler {

	private static final String LOG_SERVICE = "execution(* com.fromlabs..*Controller.*(..))";

	/**
	 * Advise. Logs the advised method.
	 * @param joinPoint 	represents advised method
	 * @return 				method execution result
	 * @throws Throwable	in case of exception
	 */
	@Around(LOG_SERVICE)
	public Object logProcess(ProceedingJoinPoint joinPoint) throws Throwable {
		return process(joinPoint, new StopWatch(), getPackageName(joinPoint, extractMethod(joinPoint)));
	}

	/**
	 * Extract name of package name
	 * @param joinPoint		represents advised method
	 * @param method		process method
	 * @return				String
	 */
	private String getPackageName(ProceedingJoinPoint joinPoint, Method method) {
		return joinPoint.getTarget().getClass().getSimpleName().concat(".").concat(method.getName());
	}

	/**
	 *
	 * @param joinPoint		represents advised method
	 * @param stopWatch		process logging stop watch
	 * @param packageName	process package name
	 * @return				process result objects
	 * @throws Throwable	in case of exception
	 */
	private Object process(ProceedingJoinPoint joinPoint, StopWatch stopWatch, String packageName)
			throws Throwable {
		log.info("Start API --- {} ---", packageName);
		stopWatch.start();
		final Object retVal = joinPoint.proceed();
		stopWatch.stop();
		log.info("End API --- {} --- in [{}] ms", packageName, stopWatch.getTotalTimeMillis());
		return retVal;
	}

	/**
	 * Extract method from join point
	 * @param joinPoint		represents advised method
	 * @return				Method
	 * @throws NoSuchMethodException in case of exception
	 */
	private Method extractMethod(ProceedingJoinPoint joinPoint) throws NoSuchMethodException {
		final MethodSignature signature = (MethodSignature) joinPoint.getSignature();
		final Class<?> targetClass = joinPoint.getTarget().getClass();
		if (Modifier.isPublic(signature.getMethod().getModifiers()))
			return targetClass.getMethod(signature.getName(), signature.getParameterTypes());
		else return ReflectionUtils.findMethod(targetClass, signature.getName(), signature.getParameterTypes());
	}

}