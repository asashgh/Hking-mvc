package com.hgh.mvc.src.controller;

import com.hgh.mvc.ioc.annotation.Autowired;
import com.hgh.mvc.bean.annotation.Controller;
import com.hgh.mvc.src.service.HelloService;

@Controller
public class HelloController {
    @Autowired
    private HelloService helloService;

    public void hello(){
        helloService.sayHello("fuck");
    }

    public void baga(){
        System.out.println("baga");
    }
}
