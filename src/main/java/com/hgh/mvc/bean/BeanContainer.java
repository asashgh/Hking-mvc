package com.hgh.mvc.bean;

import com.hgh.mvc.aop.Aspect;
import com.hgh.mvc.bean.annotation.Component;
import com.hgh.mvc.bean.annotation.Controller;
import com.hgh.mvc.bean.annotation.Repository;
import com.hgh.mvc.bean.annotation.Service;
import com.hgh.mvc.util.ClassUtil;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Bean容器
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BeanContainer {
    /**
     * 存放所有bean的Map
     */
    private final Map<Class<?>,Object> beanMap = new ConcurrentHashMap<Class<?>, Object>();

    /**
     * 是否加载Bean
     */
    private boolean isLoadBean = false;

    /**
     * 加载bean的注解列表
     */
    private static final List<Class<? extends Annotation>> BEAN_ANNOTATION
            = Arrays.asList(Component.class, Controller.class, Service.class, Repository.class, Aspect.class);

    /**
     * 获取bean实例
     * @param clazz
     * @return
     */
    public Object getBean(Class<?> clazz){
        if (null == clazz){
            return null;
        }
        return beanMap.get(clazz);
    }

    /**
     * 获取所有bean实例
     * @return
     */
    public Set<Object> getBeans(){
        return new HashSet<>(beanMap.values());
    }

    /**
     * 添加一个bean
     * @param clazz
     * @param obj
     * @return
     */
    public Object addBean(Class<?> clazz,Object obj){
        return beanMap.put(clazz,obj);
    }

    /**
     * 移除一个Bean
     * @param clazz
     * @return
     */
    public Object remove(Class<?> clazz){
        return beanMap.remove(clazz);
    }

    /**
     * 获取bean实例数量
     * @return
     */
    public int size(){
        return beanMap.size();
    }

    /**
     * 获取Bean所有的Class集合
     * @return
     */
    public Set<Class<?>> getClasses(){
        return beanMap.keySet();
    }

    /**
     * 通过注解获取该注解所有bean的class集合
     * @param annotation
     * @return
     */
    public Set<Class<?>> getClassesByAnnotation(Class<? extends Annotation> annotation){
        return beanMap.keySet()
                .stream()
                .filter(clazz -> clazz.isAnnotationPresent(annotation))
                .collect(Collectors.toSet());
    }

    /**
     * 通过实现类或者父类获取Bean的Class集合
     */
    public Set<Class<?>> getClassesBySuper(Class<?> superClass){
        return beanMap.keySet()
                .stream()
                .filter(superClass::isAssignableFrom)
                .filter(clz -> !clz.equals(superClass))
                .collect(Collectors.toSet());
    }


    /**
     * 扫描加载所有bean
     * @param basePackage
     */
    public void loadBeans(String basePackage){
        if(isLoadBean()){
            log.warn("bean已经被加载");
            return;
        }
        Set<Class<?>> classSet = ClassUtil.getPackageClass(basePackage);
        classSet.stream()
                .filter(clz -> {
                    for (Class<? extends Annotation> annotation : BEAN_ANNOTATION) {
                        if (clz.isAnnotationPresent(annotation)) {
                            return true;
                        }
                    }
                    return false;
                })
                .forEach(clz -> beanMap.put(clz,ClassUtil.newInstance(clz.getName())));
        isLoadBean = true;//上锁
    }

    public boolean isLoadBean(){
        return isLoadBean;
    }


    /**
     * 使用单例模式，确保整个项目全局唯一beanContrainer。
     */
    public static BeanContainer getInstance(){
        return ContrainerHolder.HOLDER.instance;
    }
    private enum ContrainerHolder{
        HOLDER;
        private BeanContainer instance;

        ContrainerHolder(){
            instance = new BeanContainer();
        }
    }

}
