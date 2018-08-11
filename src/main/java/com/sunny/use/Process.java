package com.sunny.use;

import com.sunny.conf.ConfPath;

/**
 * create by zsunny
 * data: 2018/8/11
 **/
public class Process {

    @ConfPath("server.port")
    private static String port;

    public void printPort(){

        System.out.println(port);

    }

}
