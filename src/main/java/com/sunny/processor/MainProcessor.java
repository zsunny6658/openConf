package com.sunny.processor;

import java.util.ArrayList;
import java.util.List;

import com.sunny.commom.listener.ConfProcessListner;
import com.sunny.processor.impl.AbstractConfProcessor;
import com.sunny.processor.impl.ConfClassProcessor;
import com.sunny.processor.impl.ConfListenerProcessor;
import com.sunny.processor.impl.ConfValueProcessor;
import com.sunny.source.MainConfLoader;
import com.sunny.source.loader.ConfLoader;

public class MainProcessor {

    private static List<ConfProcessListner> confListners = new ArrayList<>();
    private static List<Class<? extends AbstractConfProcessor>> confProcessors = new ArrayList<>();

    // get processors
    static {
        try {
            MainConfLoader.load();
        } catch (Exception e) {
            e.printStackTrace();
        }
        new ConfListenerProcessor().process();
        confProcessors.add(ConfValueProcessor.class);
        confProcessors.add(ConfClassProcessor.class);
    }

    public static void addListener(ConfProcessListner confListner) {
        confListners.add(confListner);
    }

    public static void addProcessor(Class<? extends AbstractConfProcessor> confProcessor) {
        confProcessors.add(confProcessor);
    }

    public static void process() {
        // pre listener
        confListners.forEach(ConfProcessListner::doBefore);
        // processor
        confProcessors.forEach(confProcessor -> {
            try {
                confProcessor.newInstance().process();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        // dynamic update
        start();
        // post listener
        confListners.forEach(ConfProcessListner::doAfter);
    }

    // dynamic update
    public static void start() {
        if (AbstractConfProcessor.getDynamicFieldSet().size() > 0
                && AbstractConfProcessor.getDynamicClassSet().size() > 0) {
            // there is dynamic value conf & dynamic class conf
            AbstractConfProcessor.getTp().scheduleAtFixedRate(new Thread(() -> {
                        try {
                            AbstractConfProcessor.updateConfSource();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        ConfClassProcessor.update();
                        ConfValueProcessor.update();
                    }), AbstractConfProcessor.getInterval(),
                    AbstractConfProcessor.getInterval(), AbstractConfProcessor.getUnit());
        } else if (AbstractConfProcessor.getDynamicFieldSet().size() > 0) {
            // there is only dynamic value conf
            AbstractConfProcessor.getTp().scheduleAtFixedRate(new Thread(() -> {
                        try {
                            AbstractConfProcessor.updateConfSource();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        ConfValueProcessor.update();
                    }), AbstractConfProcessor.getInterval(),
                    AbstractConfProcessor.getInterval(), AbstractConfProcessor.getUnit());
        } else if (AbstractConfProcessor.getDynamicClassSet().size() > 0) {
            // there is only dynamic class conf
            AbstractConfProcessor.getTp().scheduleAtFixedRate(new Thread(() -> {
                        try {
                            AbstractConfProcessor.updateConfSource();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        ConfClassProcessor.update();
                    }), AbstractConfProcessor.getInterval(),
                    AbstractConfProcessor.getInterval(), AbstractConfProcessor.getUnit());
        }
    }

    public static void stop() {
        AbstractConfProcessor.stopThreadPool();
    }

}
