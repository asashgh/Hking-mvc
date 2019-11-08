package com.hgh.mvc.src;

import com.hgh.mvc.aop.Aspect;
import com.hgh.mvc.aop.advice.AroundAdvice;
import com.hgh.mvc.bean.annotation.Controller;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;

@Slf4j
@Aspect(pointcut = "execution(* com.hgh.mvc.src.controller.HelloController.*(..))")
public class AspectOfController implements AroundAdvice {
    @Override
    public void before(Class<?> clz, Method method, Object[] args) throws Throwable {
        log.info("Before  Aspect ----> class: {}, method: {}", clz.getName(), method.getName());
    }

    @Override
    public void afterReturning(Class<?> clz, Object returnValue, Method method, Object[] args) throws Throwable {
        log.info("After  Aspect ----> class: {}, method: {}", clz, method.getName());
    }

    @Override
    public void afterThrowing(Class<?> clz, Method method, Object[] args, Throwable e) {
        log.error("Error  Aspect ----> class: {}, method: {}, exception: {}", clz, method.getName(), e.getMessage());
    }
}
