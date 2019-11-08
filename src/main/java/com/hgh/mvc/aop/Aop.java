package com.hgh.mvc.aop;

import com.hgh.mvc.aop.advice.Advice;
import com.hgh.mvc.bean.BeanContainer;
import lombok.extern.slf4j.Slf4j;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;

/**
 * Aop执行器
 */
@Slf4j
public class Aop {
    /**
     * Bean容器
     */
    private BeanContainer beanContainer;

    public Aop(){
        beanContainer = BeanContainer.getInstance();
    }

    /**
     * Aop执行器，使用AspectJ
     */
    public void doAop(){
        beanContainer.getClassesBySuper(Advice.class)
                .stream()
                .filter(proxyAdvicor -> proxyAdvicor.isAnnotationPresent(Aspect.class))
                .map(this::createProxyAdvicor)
                .forEach(proxyAdvisor -> beanContainer.getClasses()
                        .stream()
                        .filter(target -> !Advice.class.isAssignableFrom(target))
                        .filter(target -> !target.isAnnotationPresent(Aspect.class))
                        .forEach(target -> {
                            if(proxyAdvisor.getProxyPointCut().matches(target)){
                                Object proxyBean = ProxyCreator.createProxy(target,proxyAdvisor);
                                beanContainer.addBean(target,proxyBean);
                            }
                        })
                );
    }

    /**
     * 创建代理通知
     * @param aspectClass
     * @return
     */
    private ProxyAdvisor createProxyAdvicor(Class<?> aspectClass){
        String expression = aspectClass.getAnnotation(Aspect.class).pointcut();
        ProxyPointCut proxyPointCut = new ProxyPointCut();
        proxyPointCut.setExpression(expression);
        Advice advice = (Advice) beanContainer.getBean(aspectClass);
        return new ProxyAdvisor(advice,proxyPointCut);
    }

    /**
     * Aop执行器，未使用AspectJ
     */
//    public void doAop1(){
//        beanContainer.getClassesBySuper(Advice.class)
//                .stream()
//                .filter(aClass -> aClass.isAnnotationPresent(Aspect.class))
//                .forEach(aClass -> {
//                    final Advice advice = (Advice) beanContainer.getBean(aClass);
//                    Aspect aspect = aClass.getAnnotation(Aspect.class);
//                    beanContainer.getClassesByAnnotation(aspect.target())
//                            .stream()
//                            .filter(target -> !Advice.class.isAssignableFrom(target))
//                            .filter(target -> !target.isAnnotationPresent(Aspect.class))
//                            .forEach(target -> {
//                                ProxyAdvisor advisor = new ProxyAdvisor(advice);
//                                Object proxyBean = ProxyCreator.createProxy(target, advisor);
//                                beanContainer.addBean(target,proxyBean);
//                            });
//                });
//    }



    static final class ProxyCreator{
        private static Object createProxy(Class<?> clz, ProxyAdvisor advisor){
           return Enhancer.create(clz,(MethodInterceptor)(target,method,args,proxy) ->
                   advisor.doProxy(target,clz,method,args,proxy));
        }
    }

}
