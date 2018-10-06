package com.sunny.use;

import com.sunny.annotation.ConfPath;

/**
 * create by zsunny
 * data: 2018/8/11
 **/
public class Process {

    @ConfPath("server.port")
    private static String port;

    @ConfPath("test")
    private static String test;

    public void printPort(){

        System.out.println(port);
        System.out.println(test);

    }

}
