package com.sunny.sample;

import com.sunny.commom.annotation.ConfClass;
import com.sunny.commom.annotation.ConfClassAlias;
import com.sunny.commom.annotation.ConfClassPrefix;
import com.sunny.commom.annotation.ConfSource;

/**
 * create by zsunny
 * data: 2019/9/7
 **/
@ConfClass
@ConfSource(value = "classpath: application.properties", fixed = true)
@ConfClassPrefix("server.")
public class SampleFixedClass {

    private static String port;

    public static void print() {
        System.out.println("fixed port: " + port);
    }
}
