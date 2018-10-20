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

    @ConfPath("test")
    private static String test;

    @SystemConfPath("system.conf.active")
    private static String active;

    public void printPort(){

        System.out.println(port);
        System.out.println(test);
        System.out.println(active);

    }

}
