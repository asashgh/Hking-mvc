package com.hgh.mvc.aop.advice;

import com.hgh.mvc.aop.Aspect;

import java.lang.reflect.Method;

/**
 * 后置通知接口
 */
public interface AfterReturningAdvice extends Advice {
    /**
     * 后置通知
     * @param clz
     * @param method
     * @param args
     * @throws Throwable
     */
    void afterReturning(Class<?> clz, Object returnValue,Method method,Object[] args) throws Throwable;
}
