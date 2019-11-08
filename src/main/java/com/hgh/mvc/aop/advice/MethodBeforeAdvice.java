package com.hgh.mvc.aop.advice;

import java.lang.reflect.Method;

/**
 * 前置通知接口
 */
public interface MethodBeforeAdvice extends Advice {

    /**
     * 前置方法
     * @param clz
     * @param method
     * @param args
     * @throws Throwable
     */
    void before(Class<?> clz, Method method,Object[] args) throws Throwable;
}
