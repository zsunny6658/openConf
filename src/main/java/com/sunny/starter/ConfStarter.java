package com.sunny.starter;

import com.sunny.processor.MainProcessor;
import com.sunny.processor.impl.AbstractConfProcessor;

/**
 * @Author zsunny
 * @Date 2018/11/22 11:19
 * @Mail zsunny@yeah.net
 */
public class ConfStarter {

    public static void start() {
        // insert conf sources
        AbstractConfProcessor.init();
        // process conf sources
        MainProcessor.process();
    }

    public static void stop() {
        MainProcessor.stop();
    }

}
