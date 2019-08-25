package com.sunny.processor.impl;

import com.sunny.commom.handler.ClassHandler;
import com.sunny.commom.constant.Constant;
import com.sunny.source.loader.ConfLoader;
import com.sunny.source.filter.ConfFilter;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public abstract class AbstractConfProcessor {

    private static final ScheduledExecutorService tp = Executors.newScheduledThreadPool(2);

    private static ClassHandler classHandler;
    protected static Set<Class<?>> classSet;
    // dynamic update sets
    protected static Set<Class<?>> dynamicClassSet = new HashSet<>();
    protected static Set<Field> dynamicFieldSet = new HashSet<>();

    protected static Object oo;
    private static int interval = 10;
    private static TimeUnit unit = TimeUnit.SECONDS;

    static {
        init();
    }

    protected static void init() {
        classHandler = ClassHandler.getClassHandler();
        classSet = classHandler.getClassSet();
        dynamicClassSet = classHandler.getDynamicClassSet();
        dynamicFieldSet = classHandler.getDynamicFieldSet();
        oo = ConfLoader.getSource();
        getInitInterval();
    }

    public static void updateConfSource() throws Exception {
        ConfLoader.updateResult();
        oo = ConfLoader.getSource();
    }

    // get dynamic interval
    private static void getInitInterval() {
        Object intv = ConfFilter.getSystemConf(Constant.DYNAMIC_INTERVAL);
        Object u = ConfFilter.getSystemConf(Constant.DYNAMIC_UNIT);
        if (null != intv) {
            try {
                interval = (int) intv;
            } catch (RuntimeException e) {
                e.printStackTrace();
            }
        }
        if (null != u) {
            String su = null;
            try {
                su = (String) u;
            } catch (RuntimeException e) {
                e.printStackTrace();
            }
            switch (su) {
                case "h":
                    unit = TimeUnit.HOURS;
                    break;
                case "m":
                    unit = TimeUnit.MINUTES;
                    break;
                default:
                    unit = TimeUnit.SECONDS;
            }
        }
    }

    public static void stopThreadPool() {
        tp.shutdown();
    }

    public abstract void process();

    public static Set<Class<?>> getDynamicClassSet() {
        return classHandler.getDynamicClassSet();
    }

    public static Set<Field> getDynamicFieldSet() {
        return classHandler.getDynamicFieldSet();
    }

    public static ScheduledExecutorService getTp() {
        return tp;
    }

    public static int getInterval() {
        return interval;
    }

    public static void setInterval(int interval) {
        AbstractConfProcessor.interval = interval;
    }

    public static TimeUnit getUnit() {
        return unit;
    }

    public static void setUnit(TimeUnit unit) {
        AbstractConfProcessor.unit = unit;
    }
}
