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
        Object confSource = MainConfLoader.getLoader().load();
        fillSourceMap(confSource);
    }

    public void updateSource() {
        MainConfLoader.getLoader().update();
        Object confSource = MainConfLoader.getLoader().getMainConfValues();
        fillSourceMap(confSource);
    }

    private void fillSourceMap(Object confSource) {
        Map<Class<?>, LoadFileName> fixedClassMap = ClassHandler.getClassHandler().getFixedClassMap();
        // 首先将fixedClass和它对应的confSource都加入
        fixedClassMap.forEach((clazz, loadFileName) -> {
            sourceMap.putIfAbsent(clazz, MainConfLoader.getLoader().getConfByLoadFileName(loadFileName));
        });
        Set<Class<?>> classSet = ClassHandler.getClassHandler().getClassSet();
        // classSet中包含fixed Class，使用putIfAbsent可以避免覆盖
        classSet.forEach(clazz -> sourceMap.putIfAbsent(clazz, confSource));
    }

}
