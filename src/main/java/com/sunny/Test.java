package com.sunny;

import com.sunny.use.ConfValueProcessor;

/**
 * create by zsunny
 * data: 2018/8/11
 **/
public class Test {

    public static void main(String[] args) {

        ConfValueProcessor.putAllConf();

        Example example = new Example();

        example.printPort();

    }

}
