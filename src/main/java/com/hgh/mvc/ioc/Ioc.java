package com.hgh.mvc.ioc;

import com.hgh.mvc.bean.annotation.Service;
import com.hgh.mvc.ioc.annotation.Autowired;
import com.hgh.mvc.bean.BeanContainer;
import com.hgh.mvc.util.ClassUtil;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.Optional;

@Slf4j
public class Ioc {

    /**
     * Bean容器
     */
    private BeanContainer beanContainer;

    public Ioc() {
        this.beanContainer = BeanContainer.getInstance();
    }

    /**
     * 执行Ioc
     */
    public void doIoc(){
        for(Class<?> clz : beanContainer.getClasses()){//遍历所有bean的class
            final Object tragetBean = beanContainer.getBean(clz);
            Field[] fields = clz.getDeclaredFields();
            for(Field field : fields){//遍历bean中所有属性
                if(field.isAnnotationPresent(Autowired.class)){
                    final Class<?> fieldClazz = field.getType();
                    Object fieldValue = getClassInstance(fieldClazz);
                    if(null != fieldValue){
                        ClassUtil.setField(field,tragetBean,fieldValue,true);
                    }else {
                        throw new RuntimeException("无法注入对应的类，目标类型:" + fieldClazz.getName());
                    }
                }
            }
        }
    }

    /**
     * 根据Value获取实例
     */


    /**
     * 根据class获取其实例或者实现类
     * @param clazz
     * @return
     */
    private Object getClassInstance(final Class<?> clazz){
        return Optional
                .ofNullable(beanContainer.getBean(clazz))
                .orElseGet(() -> {
                    Class<?> implementClass = getImplementClass(clazz);
                    if(null != implementClass){
                        return beanContainer.getBean(implementClass);
                    }
                    return null;
                });
    }

    /**
     * 获取接口实现类
     * @param interfaceClass
     * @return
     */
    private Class<?> getImplementClass(final Class<?> interfaceClass){
        return beanContainer.getClassesBySuper(interfaceClass)
                .stream()
                .findFirst()
                .orElse(null);
    }
}
