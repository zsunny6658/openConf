package com.sunny;

import com.sunny.annotation.ConfClass;
import com.sunny.annotation.ConfClassDefault;
import com.sunny.annotation.ConfClassIgnore;
import com.sunny.annotation.ConfClassPrefix;
import com.sunny.annotation.ConfSource;

@ConfClass
@ConfSource()
@ConfClassPrefix("test.")
public class ExampleClass {

    private static String a;
    private static String b = "2";

    @ConfClassIgnore
    private static String c;

    @ConfClassDefault("ddddd")
    private static String d;


    public static void print(){
        System.out.println("class-a:" + a);
        System.out.println("class-b:" + b);
        System.out.println("class-c:" + c);
        System.out.println("class-d:" + d);
    }
}
