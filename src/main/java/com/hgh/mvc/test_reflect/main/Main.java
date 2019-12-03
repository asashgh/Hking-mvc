package com.hgh.mvc.test_reflect.main;

import com.hgh.mvc.test_reflect.Component;
import com.hgh.mvc.test_reflect.entity.User1;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 测试Main
 */
public class Main {
    public static void main(String[] args) {
        User1 user1 = new User1("张三","123456","男","432221853704110011");
        Class<User1> clazz = (Class<User1>) user1.getClass();
        Field[] fields = clazz.getDeclaredFields();
        Arrays.asList(fields).stream().forEach(field -> System.out.println(field.getType()));
        Arrays.asList(fields).stream().forEach(field -> System.out.println(field.getName()));
        Method[] methods = clazz.getMethods();
        List<String> strings = Arrays.asList(methods).stream().map(method -> method.getName()).filter(method -> {
            if(method.equals("name") || method.equals("password") || method.equals("sex") || method.equals("id")){
                return true;
            }
            return false;
        }).collect(Collectors.toList());
        System.out.println(strings);
        System.out.println(clazz.isAnnotationPresent(Component.class));
        Component component = clazz.getAnnotation(Component.class);
        System.out.println(component.value());
    }
}
