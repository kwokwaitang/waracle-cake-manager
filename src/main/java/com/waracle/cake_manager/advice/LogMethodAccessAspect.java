package com.waracle.cake_manager.advice;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Aspect
@Component
public class LogMethodAccessAspect {
    private static final Logger LOGGER = Logger.getGlobal();

    /**
     * Set up a named pointcut and will help narrow down the joint points advised by an aspect
     *
     * <p>This pointcut will match exactly the execution of whatever method with any number of parameters that are
     * annotated with LogMethodAccess
     */
    @Pointcut("execution(@com.waracle.cake_manager.advice.LogMethodAccess * *(..))")
    public void methodAnnotatedWithLogMethodAccess() {
    }

    @Around("methodAnnotatedWithLogMethodAccess()")
    public Object logMethodCalls(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        // Set-up the advise...
        MethodSignature methodSignature = (MethodSignature) proceedingJoinPoint.getSignature();
        Method method = ((MethodSignature) proceedingJoinPoint.getSignature()).getMethod();

        // Get intercepted method details
        String className = methodSignature.getDeclaringType().getSimpleName();
        String methodName = methodSignature.getName();

        Object[] args = proceedingJoinPoint.getArgs();
        Parameter[] parameters = method.getParameters();

        if (parameters.length > 0) {
            /*
             * Using an IntStream to iterate over two arrays to generate a Map
             * https://stackoverflow.com/questions/30339679/how-to-map-two-arrays-to-one-hashmap-in-java
             */
            Map<String, String> params = IntStream
                    .range(0, parameters.length)
                    .boxed()
                    .collect(Collectors.toMap(key -> parameters[key].getName(), value -> args[value].toString()));

            boolean foundHttpServletRequest = false;
            for (int i = 0; i < parameters.length; i++) {
                if (parameters[i].getName().equals("httpServletRequest")) {
                    foundHttpServletRequest = true;
                    break;
                }
            }

            if (foundHttpServletRequest) {
                LOGGER.info(() -> String.format(">> [Found HttpServletRequest] Running %s with %s", methodName, params));
            } else {
                LOGGER.info(() -> String.format(">> Running %s with %s", methodName, params));
            }
        } else {
            LOGGER.info(() -> String.format(">> Running %s with no parameters", methodName));
        }

        final StopWatch stopWatch = new StopWatch();

        // Measure method execution time
        stopWatch.start();
        Object result = proceedingJoinPoint.proceed();
        stopWatch.stop();

        // Log method execution time
        LOGGER.info(() -> String.format("\tExecution time of %s.%s is %dms", className, methodName,
                stopWatch.getTotalTimeMillis()));
        LOGGER.info(() -> String.format("\tResult is [%s]", result));

        return result;
    }
}
