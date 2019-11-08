package com.hgh.mvc.src.service.impl;

import com.hgh.mvc.bean.annotation.Service;
import com.hgh.mvc.src.service.HelloService;

@Service
public class HelloServiceImpl1 implements HelloService {
    @Override
    public void sayHello(String hello) {
        System.out.println("balabala");
    }
}
