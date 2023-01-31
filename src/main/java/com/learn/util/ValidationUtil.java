package com.learn.util;


import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ValidationUtil {

    private ValidationUtil(){
        throw new IllegalStateException("Utility class");
    }
    public static boolean isBlank(Long id){
        return id.describeConstable().isEmpty();
    }
    public static boolean isBlank(String name){
        return name.isBlank();
    }
    public static boolean isBlank(Object input) throws InvocationTargetException, IllegalAccessException {
        Method[] methods = input.getClass().getDeclaredMethods();
        for(Method method:methods){
            if(method.getName().startsWith("get") && StringUtils.isBlank(method.invoke(input).toString())){
                return true;
            }
        }
        return false;
    }

    public static boolean validateEmail(String email) {
        return email.matches("^[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$");
    }

    public static boolean validatePhoneNumber(String phoneNumber) {
        return phoneNumber.matches("^\\d{10}$");
    }
    public static boolean validatePassword(String password){
        return  password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$");
    }
}
