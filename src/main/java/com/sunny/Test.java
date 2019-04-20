package com.sunny;

import com.sunny.starter.ConfStarter;

/**
 * create by zsunny
 * data: 2018/8/11
 **/
public class Test {

    public static void main(String[] args) {
        System.out.println(System.getProperty("user.dir")+"\\src\\main\\resources");
        ConfStarter.start();
        ExampleClass.print();
        Example.printPort();

    }

}
