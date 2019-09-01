package com.sunny.processor;

import java.util.ArrayList;
import java.util.List;

import com.sunny.commom.listener.ConfProcessListener;
import com.sunny.processor.impl.AbstractConfProcessor;
import com.sunny.processor.impl.ConfClassProcessor;
import com.sunny.processor.impl.ConfListenerProcessor;
import com.sunny.processor.impl.ConfValueProcessor;
import com.sunny.source.MainConfLoader;

public class MainProcessor {

    private static List<ConfProcessListener> confListeners = new ArrayList<>();
    private static List<AbstractConfProcessor> confProcessors = new ArrayList<>();

    // get processors
    static {
        ConfListenerProcessor.getProcessor().process();
        confProcessors.add(ConfValueProcessor.getProcessor());
        confProcessors.add(ConfClassProcessor.getProcessor());
    }

    public static void addListener(ConfProcessListener confListener) {
        confListeners.add(confListener);
    }

    public static void addProcessor(AbstractConfProcessor confProcessor) {
        confProcessors.add(confProcessor);
    }

    public static void process() {
        // pre listener
        confListeners.forEach(ConfProcessListener::doBefore);
        // processor
        confProcessors.forEach(confProcessor -> {
            try {
                confProcessor.process();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        // dynamic update
        start();
        // post listener
        confListeners.forEach(ConfProcessListener::doAfter);
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
                        ConfClassProcessor.getProcessor().update();
                        ConfValueProcessor.getProcessor().update();
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
                        ConfValueProcessor.getProcessor().update();
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
                        ConfClassProcessor.getProcessor().update();
                    }), AbstractConfProcessor.getInterval(),
                    AbstractConfProcessor.getInterval(), AbstractConfProcessor.getUnit());
        }
    }

    public static void stop() {
        AbstractConfProcessor.stopThreadPool();
    }

}
