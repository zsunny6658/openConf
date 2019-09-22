package com.sunny.commom.handler;

import com.sunny.source.MainConfLoader;
import com.sunny.source.bean.LoadFileName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * create by zsunny
 * data: 2019/9/7
 **/
public class SourceHandler {

    private static final Logger log = LoggerFactory.getLogger(SourceHandler.class);

    private Map<Class<?>, Object> sourceMap = new HashMap<>();

    private SourceHandler() {
    }

    private static class SourceHandlerInner {
        private static SourceHandler sourceHandler = new SourceHandler();
    }

    public static SourceHandler getSourceHandler() {
        return SourceHandlerInner.sourceHandler;
    }

    public Object getSourceByClass(Class<?> clazz) {
        return sourceMap.get(clazz);
    }

    public Object getMainSource() {
        return MainConfLoader.getLoader().getMainConfValues();
    }

    public void loadSource() {
        MainConfLoader.getLoader().load();
        fillSourceMap();
    }

    public void updateSource() {
        MainConfLoader.getLoader().update();
        fillSourceMap();
    }

    private void fillSourceMap() {
        Map<Class<?>, LoadFileName> fixedClassMap = ClassHandler.getClassHandler().getFixedClassMap();
        Set<Class<?>> dynamicClassSet = ClassHandler.getClassHandler().getDynamicClassSet();

        // 首先将fixedClass和它对应的confSource都加入
        fixedClassMap.forEach((clazz, loadFileName) -> {
            Object tmpSource;
            // 如果是动态的，使用新的读取的配置，如果非动态，则读取初始配置
            if (dynamicClassSet.contains(clazz)) {
                tmpSource = MainConfLoader.getLoader().getConfByLoadFileName(loadFileName);
            } else {
                // 如果非动态，读取初始配置版本
                tmpSource = MainConfLoader.getLoader().getOriginConfByLoadFileName(loadFileName);
            }
            sourceMap.putIfAbsent(clazz, tmpSource);
        });

        Set<Class<?>> classSet = ClassHandler.getClassHandler().getClassSet();
        Object confSource = MainConfLoader.getLoader().getMainConfValues(); // 动态变化后的
        Object originConfSource = MainConfLoader.getLoader().getOriginMainConfValues(); // 初始的
        // classSet中包含fixed Class，使用putIfAbsent可以避免覆盖
        classSet.forEach(clazz -> {
            if (dynamicClassSet.contains(clazz)) {
                sourceMap.putIfAbsent(clazz, confSource);
            } else {
                sourceMap.putIfAbsent(clazz, originConfSource);
            }
        });
    }

}
