package com.sunny.starter;

import com.sunny.processor.main.MainProcessor;

/**
 * @Author zsunny
 * @Date 2018/11/22 11:19
 * @Mail zsunny@yeah.net
 */
public class ConfStarter {

    public static void start(){
        MainProcessor.process();
    }

    public static void stop(){
        MainProcessor.stop();
    }

}
