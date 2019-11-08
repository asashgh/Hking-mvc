package com.hgh.mvc.aop;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Aspect {

//    Class<? extends Annotation> target();

    String pointcut() default "";
}

