package com.hgh.mvc.aop;

import com.hgh.mvc.aop.advice.Advice;
import com.hgh.mvc.aop.advice.AfterReturningAdvice;
import com.hgh.mvc.aop.advice.MethodBeforeAdvice;
import com.hgh.mvc.aop.advice.ThrowsAdvice;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.cglib.proxy.MethodProxy;
import org.aspectj.weaver.tools.PointcutParser;

import java.lang.reflect.Method;

/**
 * 代理通知类
 */
@Slf4j
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ProxyAdvisor {
    /**
     * 通知
     */
    private Advice advice;

    /**
     * AspectJ表达式切点匹配器
     */
    private ProxyPointCut proxyPointCut;

    /**
     * 执行代理方法
     * @param target
     * @param targetClass
     * @param method
     * @param args
     * @param proxy
     * @return
     * @throws Throwable
     */
    public Object doProxy(Object target, Class<?> targetClass, Method method, Object[] args, MethodProxy proxy) throws Throwable{
        if(!proxyPointCut.matches(method)){
            return proxy.invokeSuper(target,args);
        }
        Object result = null;
        if(advice instanceof MethodBeforeAdvice){
            ((MethodBeforeAdvice) advice).before(targetClass,method,args);
        }
        try {
            result = proxy.invokeSuper(target,args);   //执行目标类的方法
            if(advice instanceof AfterReturningAdvice){
                ((AfterReturningAdvice) advice).afterReturning(targetClass,result,method,args);
            }
        }catch (Exception e){
            if (advice instanceof ThrowsAdvice){
                ((ThrowsAdvice) advice).afterThrowing(targetClass,method,args,e);
            }else {
                throw new Exception(e);
            }
        }
        return result;
    }
}
