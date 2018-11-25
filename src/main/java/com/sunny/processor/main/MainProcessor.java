package com.sunny.processor.main;

import com.sunny.annotation.ConfClass;
import com.sunny.processor.ConfClassProcessor;
import com.sunny.processor.ConfListenerProcessor;
import com.sunny.processor.ConfProcessor;
import com.sunny.processor.ConfValueProcessor;
import com.sunny.source.LoadResult;
import com.sunny.source.listener.ConfListner;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

public class MainProcessor {

    private static ArrayList<ConfListner> confListners = new ArrayList<>();
    private static ArrayList<Class<? extends ConfProcessor>> confProcessors = new ArrayList<>();

    //processor处理
    static {
        try {
            LoadResult.LoadResult();
        } catch (Exception e) {
            e.printStackTrace();
        }
        ConfListenerProcessor.process();
        confProcessors.add(ConfValueProcessor.class);
        confProcessors.add(ConfClassProcessor.class);
    }

    public static void addListener(ConfListner confListner){
        confListners.add(confListner);
    }

    public static void addProcessor(Class<? extends ConfProcessor> confProcessor){
        confProcessors.add(confProcessor);
    }

    public static void process(){
        confListners.forEach(ConfListner::doBefore);

        confProcessors.forEach(confProcessor -> {
            try {
                Method method = confProcessor.getDeclaredMethod("process");
                method.invoke(confProcessor);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }

        });

        confListners.forEach(ConfListner::doAfter);

    }

}
