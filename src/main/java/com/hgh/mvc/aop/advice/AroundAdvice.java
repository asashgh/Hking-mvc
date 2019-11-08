package com.hgh.mvc.aop.advice;

import com.hgh.mvc.aop.Aspect;

/**
 * 环绕通知接口
 */
public interface AroundAdvice extends AfterReturningAdvice,MethodBeforeAdvice,ThrowsAdvice {

}
