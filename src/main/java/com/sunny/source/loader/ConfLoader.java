package com.sunny.source.loader;

import java.util.*;

import com.sunny.commom.handler.ClassHandler;
import com.sunny.commom.constant.LoadFileNameConstant;
import com.sunny.commom.listener.SourceListener;
import com.sunny.commom.listener.impl.DefaultConfSourceListener;
import com.sunny.source.bean.Content;
import com.sunny.source.bean.LoadFileName;

public class ConfLoader extends AbstractConfLoader {

    private List<SourceListener> sourceListeners;

    private List<LoadFileName> loadFileNameList =
            new ArrayList<>(Arrays.asList(LoadFileNameConstant.APPLICATION_YML, LoadFileNameConstant.APPLICATION_YAML,
                    LoadFileNameConstant.APPLICATION_PROPERTIES, LoadFileNameConstant.APPLICATION_XML,
                    LoadFileNameConstant.APPLICATION_JSON));
    private Map<LoadFileName, Content> confMap = new TreeMap<>();
    private Map<String, Object> confValues;

    private ClassHandler classHandler = ClassHandler.getClassHandler();

    public void add(LoadFileName loadFile) {
        loadFileNameList.add(loadFile);
    }

    public void remove(LoadFileName loadFile) {
        loadFileNameList.remove(loadFile);
    }

    private ConfLoader() {
    }

    private static class ConfLoaderInner {
        private static ConfLoader confLoader = new ConfLoader();
    }

    public static ConfLoader getLoader() {
        return ConfLoaderInner.confLoader;
    }

    // 首次加载
    public void loadResult() throws Exception {
        dealWithListener();
        // 前置策略，必包含前置读取自定义conf source
        sourceListeners.forEach(SourceListener::doBefore);
        // 加载各配置源配置内容
        loadSource(confMap, loadFileNameList);
        // 合并到confValues中
        confValues = mergeSource(confMap);
        // listener后置处理
        sourceListeners.forEach(SourceListener::doAfter);
    }

    public void updateResult() throws Exception {
        // 加载各配置源配置内容
        loadSource(confMap, loadFileNameList);
        // 合并到confValues中
        confValues = mergeSource(confMap);
    }

    /**
     * 处理SourceListerner
     */
    private void dealWithListener() {
        // TODO 后续还会增加部分前置处理策略
        // 前置处理注解@ConfSource，用于获取默认配置之外的配置文件
        sourceListeners = new ArrayList<>();
        sourceListeners.add(new DefaultConfSourceListener());
        classHandler.getClassSet().forEach(clazz -> {
            if (SourceListener.class.isAssignableFrom(clazz) && !clazz.isInterface() &&
                    !clazz.getCanonicalName().equals(DefaultConfSourceListener.class.getCanonicalName())) {
                try {
                    sourceListeners.add((SourceListener) clazz.newInstance());
                } catch (InstantiationException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public Map<String, Object> getSource() {
        return confValues;
    }

    public Map<LoadFileName, Content> getConfMap() {
        return confMap;
    }

    public List<LoadFileName> getLoadFileNameList() {
        return loadFileNameList;
    }

}
