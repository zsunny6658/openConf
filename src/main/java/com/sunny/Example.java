package com.sunny;

import com.sunny.annotation.ConfPath;
import com.sunny.annotation.SystemConfPath;

/**
 * create by zsunny
 * data: 2018/8/11
 **/
public class Example {

    @ConfPath("server.port")
    private static String port;

    @ConfPath("test.b")
    private static String test;

    @SystemConfPath("system.conf.active")
    private static String active;

    public static void printPort(){

        System.out.println("prop-port:" + port);
        System.out.println("prop-test:" + test);
        System.out.println("prop-active:" + active);

    }

}
