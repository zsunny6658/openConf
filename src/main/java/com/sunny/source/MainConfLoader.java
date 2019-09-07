package com.sunny.source;

import com.sunny.commom.utils.NodeUtils;
import com.sunny.commom.utils.ObjectUtils;
import com.sunny.source.bean.Content;
import com.sunny.source.bean.LoadFileName;
import com.sunny.source.filter.ConfFilter;
import com.sunny.source.loader.ActiveConfLoader;
import com.sunny.source.loader.ConfLoader;

import java.util.HashMap;
import java.util.Map;

/**
 * create by zsunny
 * data: 2019/8/25
 **/
public class MainConfLoader {

    private ConfLoader confLoader;
    private ActiveConfLoader activeConfLoader;

    private Map<String, Object> mainConfValues; // confKey -> confValue
    private Map<String, Object> mainConfMap; // fileName -> conf

    private MainConfLoader() {
    }

    private static class MainConfLoaderInner {
        private static MainConfLoader mainConfLoader = new MainConfLoader();
    }

    public static MainConfLoader getLoader() {
        return MainConfLoaderInner.mainConfLoader;
    }

    public Object load() {
        try {
            (confLoader = ConfLoader.getLoader()).loadResult();
            (activeConfLoader = ActiveConfLoader.getLoader()).loadResult();
            mergeSources();
            mergeSourceMap();
            ConfFilter.filter(mainConfValues);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mainConfValues;
    }

    public void update() {
        try {
            confLoader.updateResult();
            activeConfLoader.updateResult();
            mergeSources();
            mergeSourceMap();
            ConfFilter.filter(mainConfValues);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Object getMainConfValues() {
        return mainConfValues;
    }

    public Map<String, Object> getMainConfMap() {
        return mainConfMap;
    }

    public Object getConfByLoadFileName(LoadFileName loadFileName) {
        return getConfByFileName(loadFileName.getFileName());
    }

    @SuppressWarnings("unchecked")
    public Object getConfByFileName(String filename) {
        Object source = mainConfMap.get(filename);
        ConfFilter.filter((Map<String, Object>) source);
        return source;
    }

    @SuppressWarnings("unchecked")
    private void mergeSources() {
        mainConfValues = new HashMap<>();
        Map<String, Object> confValues = ConfLoader.getLoader().getSource();
        Map<String, Object> activeConfValues = ActiveConfLoader.getLoader().getSource();
        NodeUtils.merge(mainConfValues, (Map<String, Object>) ObjectUtils.deepCopy(confValues), true);
        NodeUtils.merge(mainConfValues, (Map<String, Object>) ObjectUtils.deepCopy(activeConfValues), true);
    }

    private void mergeSourceMap() {
        Map<LoadFileName, Content> confMap = ConfLoader.getLoader().getConfMap();
        Map<LoadFileName, Content> activeConfMap = ActiveConfLoader.getLoader().getConfMap();
        mainConfMap = new HashMap<>();
        confMap.forEach(((loadFileName, content) -> mainConfMap.putIfAbsent(loadFileName.getFileName(), content.getContent())));
        activeConfMap.forEach(((loadFileName, content) -> mainConfMap.putIfAbsent(loadFileName.getFileName(), content.getContent())));
    }

}
