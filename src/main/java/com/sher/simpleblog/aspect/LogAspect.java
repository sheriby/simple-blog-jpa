package com.sher.simpleblog.aspect;

import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Aspect
@Slf4j
@Component
public class LogAspect {

    @Pointcut("execution(* com.sher.simpleblog.controller.*.*(..))")
    public void log() {
    }

    @Before("log()")
    public void before(JoinPoint joinPoint) {
        ServletRequestAttributes attributes =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        String url = request.getRequestURL().toString();
        String ip = request.getRemoteAddr();

        String classMethod = joinPoint.getSignature().getDeclaringTypeName() +
                "." + joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();

        RequestLog requestLog = new RequestLog(url, ip, classMethod, args);

        log.info("Request: {}", requestLog);
    }

    @AfterReturning(pointcut = "log()", returning = "result")
    public void afterReturn(Object result) {
        log.info("Result: {}", result);
    }

    @After("log()")
    public void after() {
    }

    @ToString
    @RequiredArgsConstructor
    private class RequestLog {
        private final String url;
        private final String ip;
        private final String classMethod;
        private final Object[] args;
    }
}
