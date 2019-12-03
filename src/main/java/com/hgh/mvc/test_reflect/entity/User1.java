package com.hgh.mvc.test_reflect.entity;

import com.hgh.mvc.test_reflect.Component;

@Component("user1")
public class User1 {
    private String name;
    private String password;
    private String sex;
    private String id;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public User1(String name, String password, String sex, String id) {
        this.name = name;
        this.password = password;
        this.sex = sex;
        this.id = id;
    }

    public User1() {
    }
}
