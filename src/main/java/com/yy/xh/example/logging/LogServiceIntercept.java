package com.yy.xh.example.logging;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.AfterAdvice;
import org.springframework.aop.BeforeAdvice;

import java.lang.reflect.Method;

/**
 * LogServiceIntercept
 *
 * @Date 2021/3/12
 * @Author Dandy
 */
public class LogServiceIntercept implements MethodInterceptor,BeforeAdvice, AfterAdvice {
    @Override
    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        Method method = methodInvocation.getMethod();
        return methodInvocation.proceed();
    }
}
