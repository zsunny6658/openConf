package com.sunny;

import com.sunny.sample.Sample;
import com.sunny.sample.SampleClass;
import com.sunny.starter.ConfStarter;

/**
 * create by zsunny
 * data: 2018/8/11
 **/
public class Test {

    public static void main(String[] args) {
        ConfStarter.start();
        Sample.printPort(); // 验证结果
        SampleClass.print(); // 验证结果
    }

}
