package com.sunny.commom.listener.impl;

import com.sunny.commom.annotation.ConfSource;
import com.sunny.commom.handler.ClassHandler;
import com.sunny.commom.listener.SourceListener;
import com.sunny.source.loader.ConfLoader;
import com.sunny.source.bean.LoadFileName;
import com.sunny.source.file.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.lang.model.type.UnknownTypeException;
import java.util.Set;

public class DefaultConfSourceListener implements SourceListener{

    private static final String CUSTOM_PREFIX = "classpath: ";

    private static final Logger log = LoggerFactory.getLogger(DefaultConfSourceListener.class);

    private ClassHandler classHandler = ClassHandler.getClassHandler();

    @Override
    public void doBefore() {
        log.info("Start to deal with custom conf source...");
        loadCustomConfSource();
    }

    @Override
    public void doAfter() {
        log.info("All conf sources have been loaded...");
    }

    private void loadCustomConfSource() {
        Set<Class<?>> classSet = classHandler.getClassSet();
        classSet.forEach(this::loadConfSource);
    }

    // 加载自定义的配置文件
    private void loadConfSource(Class<?> clazz) {
        if (!clazz.isAnnotationPresent(ConfSource.class)) {
            return;
        }
        if (clazz.isAnnotationPresent(ConfSource.class)) {
            ConfSource confSource = clazz.getAnnotation(ConfSource.class);
            String filePath = confSource.value();//配置文件名/路径
            String fileName = filePath.substring(filePath.indexOf(CUSTOM_PREFIX) + CUSTOM_PREFIX.length());
            LoadSource loadSource = getLoadSource(fileName);
            LoadFileName loadFile = new LoadFileName(fileName, loadSource);
            ConfLoader.add(loadFile);
        }
    }

    private LoadSource getLoadSource(String fileName) {
        if (fileName.toLowerCase().endsWith(".yaml")
                || fileName.toLowerCase().endsWith(".yml")) {
            return LoadYaml.getInstance();
        } else if (fileName.toLowerCase().endsWith(".properties")) {
            return LoadProperties.getInstance();
        } else if (fileName.toLowerCase().endsWith(".xml")) {
            return LoadXml.getInstance();
        } else if (fileName.toLowerCase().endsWith(".json")) {
            return LoadJson.getInstance();
        } else {
            throw new UnknownTypeException(null, "暂时不支持此种配置文件");
        }
    }
}
