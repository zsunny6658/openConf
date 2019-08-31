package com.sunny.commom.handler;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

// TODO 线程池精细化管理 使用list队列 分空闲与使用状态
public class TpHandler {

    private static final ScheduledExecutorService tp = Executors.newScheduledThreadPool(2);

    public static ScheduledExecutorService getScheduledTp() {
        return tp;
    }

}
