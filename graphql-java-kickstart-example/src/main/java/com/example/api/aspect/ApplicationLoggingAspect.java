package com.example.api.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Arrays;

//@Aspect
@Slf4j
@Component
public class ApplicationLoggingAspect {

    @Pointcut("execution(* com.example.api.client.ProductClient.*(..))")
    private void usingFeignClients() {
    }

    @Before("usingFeignClients()")
    public void logBeforeFeignExecution(JoinPoint joinPoint) {
        log.info("[FEIGN] -> called: {}", joinPoint.getSignature().toShortString());
    }

    @After("usingFeignClients()")
    public void logAfterFeignExecution(JoinPoint joinPoint) {
        log.info("[FEIGN] -> finished: {}", joinPoint.getSignature().toShortString());
    }
}
