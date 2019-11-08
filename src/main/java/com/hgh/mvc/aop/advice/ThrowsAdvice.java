package com.hgh.mvc.aop.advice;

import java.lang.reflect.Method;

/**
 * 异常通知接口
 */
public interface ThrowsAdvice extends Advice {
    /**
     * 异常方法
     * @param clz
     * @param method
     * @param args
     * @param e
     */
    void afterThrowing(Class<?> clz, Method method,Object[] args ,Throwable e);
}
