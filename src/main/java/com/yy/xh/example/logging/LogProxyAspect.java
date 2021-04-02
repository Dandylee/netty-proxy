package com.yy.xh.example.logging;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * LogProxyAspect
 *
 * @Date 2021/3/12
 * @Author Dandy
 */
@Slf4j
@Aspect
@Component
public class LogProxyAspect {

    @Around("execution(* com.yy.xh.example.logging.LogService.*(..))")
    public Object proxy1(ProceedingJoinPoint pjp){
        Object[] args = pjp.getArgs();
        try {
            log.info("print log:{}", args);
            return pjp.proceed(args);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            return null;
        }
    }


    @Pointcut("@annotation(com.yy.xh.example.logging.PrettyPrint)")
    public void proxyAnnotation(){

    }

    @Around("proxyAnnotation()")
    public Object before(ProceedingJoinPoint pjp){
        Object[] args = pjp.getArgs();
        try {
            log.info("print json:{}", args);
            return pjp.proceed(args);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            return null;
        }
    }
}
