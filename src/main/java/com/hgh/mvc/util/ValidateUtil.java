package com.hgh.mvc.util;

public class ValidateUtil {
    public static Boolean isEmpty(Object o){
        return (o == null || "".equals(o));
    }

    public static Boolean isNotEmpty(Object o){
        return !isEmpty(o);
    }
}
