package com.sunny;

import com.sunny.annotation.*;

/**
 * create by zsunny
 * data: 2018/8/11
 **/
@ConfSource("classpath: configer.properties")
//@ConfSource()
public class Example {
	@ConfPath("other.file.configer")
	private static String other;

    @ConfPath("server.port")
    private static String port;

    @ConfPath("test.b")
    private static String test;

    @SystemConfPath("system.conf.active")
    private static String active;

    @Dynamic
    @ConfPath("dynamic.test")
    private static String t;

    @Dynamic
    @ConfPath("json.data")
    private static String json;
    @ConfPath("json.test")
    private static boolean isJson;

    public static void printPort(){
        System.out.println("dynamic:" + t);
        System.out.println("other:" + other);
        System.out.println("prop-port:" + port);
        System.out.println("prop-test:" + test);
        System.out.println("prop-active:" + active);

        while (true) {
            System.out.println("prop-json:" + json + " " + isJson);
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

}
