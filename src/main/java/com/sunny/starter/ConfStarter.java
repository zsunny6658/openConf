package com.sunny.starter;

import com.sunny.processor.ConfValueProcessor;

/**
 * @Author zsunny
 * @Date 2018/11/22 11:19
 * @Mail zsunny@yeah.net
 */
public class ConfStarter {

    public static void start(){
        ConfValueProcessor.putAllConf();
    }

}
