package com.sunny;

import com.sunny.use.ConfValueDeal;
import com.sunny.use.Process;

/**
 * create by zsunny
 * data: 2018/8/11
 **/
public class Test {

    public static void main(String[] args) {

        ConfValueDeal.putAllConf();

        Process process = new Process();

        process.printPort();

    }

}
