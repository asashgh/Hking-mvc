package com.hgh.mvc.src.service.impl;

import com.hgh.mvc.bean.annotation.Service;
import com.hgh.mvc.src.service.HelloService;

@Service
public class HelloServiceImpl implements HelloService {
    @Override
    public void sayHello(String hello) {
        System.out.println("oh my dear! "+hello+" you!");
    }
}
