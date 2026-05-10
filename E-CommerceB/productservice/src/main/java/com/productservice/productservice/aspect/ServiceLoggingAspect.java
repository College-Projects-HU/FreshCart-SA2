package com.productservice.productservice.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ServiceLoggingAspect {

    private static final Logger log = LoggerFactory.getLogger(ServiceLoggingAspect.class);

    @Pointcut("execution(* com.productservice.productservice.services.*.*(..))")
    public void serviceMethods() {
    }

    @Before("serviceMethods()")
    public void logBefore(JoinPoint joinPoint) {
        log.info("Starting {}.{}()", joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName());
    }

    @AfterReturning(pointcut = "serviceMethods()", returning = "result")
    public void logAfterReturning(JoinPoint joinPoint, Object result) {
        log.info("Completed {}.{}() successfully", joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName());
    }

    @AfterThrowing(pointcut = "serviceMethods()", throwing = "ex")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable ex) {
        log.error("Failed {}.{}() with exception: {}", joinPoint.getSignature().getDeclaringTypeName(),
                joinPoint.getSignature().getName(), ex.toString(), ex);
    }
}

