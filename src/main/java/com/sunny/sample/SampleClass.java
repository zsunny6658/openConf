package com.sunny.sample;

import com.sunny.annotation.*;
import sun.font.TrueTypeFont;

@Dynamic
@ConfClass
@ConfClassPrefix("test.")
public class SampleClass {

    private static String a;
    private static String b = "2";

    @ConfClassIgnore
    private static String c;

    @ConfClassDefault("ddddd")
    private static String d;

    @ConfClassAlias("d")
    private static String e;


    public static void print() {
        while (true) {
            System.out.println("class-a:" + a);
            System.out.println("class-b:" + b);
            System.out.println("class-c:" + c);
            System.out.println("class-d:" + d);
            System.out.println("class-e:" + e);
        }
    }
}
