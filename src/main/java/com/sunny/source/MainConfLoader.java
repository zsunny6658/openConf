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

    private Map<String, Object> originMainConfValues; // confKey -> confValue
    private Map<String, Object> mainConfValues; // confKey -> confValue
    private Map<String, Object> originMainConfMap; // fileName -> conf
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
            originMainConfValues = mainConfValues = mergeSources();
            originMainConfMap = mainConfMap = mergeSourceMap();
            ConfFilter.filter(mainConfValues);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mainConfValues;
    }

    /**
     * 返回的是动态变化后的
     * @return
     */
    public Object update() {
        try {
            confLoader.updateResult();
            activeConfLoader.updateResult();
            mainConfValues = mergeSources();
            mainConfMap = mergeSourceMap();
            ConfFilter.filter(mainConfValues);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mainConfValues;
    }

    public Object getMainConfValues() {
        return mainConfValues;
    }

    public Object getOriginMainConfValues() {
        return originMainConfValues;
    }

    public Map<String, Object> getMainConfMap() {
        return mainConfMap;
    }

    public Map<String, Object> getOriginMainConfMap() {
        return originMainConfMap;
    }

    public Object getConfByLoadFileName(LoadFileName loadFileName) {
        return getConfByFileName(loadFileName.getFileName());
    }

    public Object getOriginConfByLoadFileName(LoadFileName loadFileName) {
        return getOriginConfByFileName(loadFileName.getFileName());
    }

    @SuppressWarnings("unchecked")
    public Object getConfByFileName(String filename) {
        Object source = mainConfMap.get(filename);
        ConfFilter.filter((Map<String, Object>) source);
        return source;
    }

    public Object getOriginConfByFileName(String filename) {
        Object source = originMainConfMap.get(filename);
        ConfFilter.filter((Map<String, Object>) source);
        return source;
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> mergeSources() {
        Map<String, Object> tmpMainConfValues = new HashMap<>();
        Map<String, Object> confValues = ConfLoader.getLoader().getSource();
        Map<String, Object> activeConfValues = ActiveConfLoader.getLoader().getSource();
        NodeUtils.merge(tmpMainConfValues, (Map<String, Object>) ObjectUtils.deepCopy(confValues), true);
        NodeUtils.merge(tmpMainConfValues, (Map<String, Object>) ObjectUtils.deepCopy(activeConfValues), true);
        return tmpMainConfValues;
    }

    private Map<String, Object>  mergeSourceMap() {
        Map<LoadFileName, Content> confMap = ConfLoader.getLoader().getConfMap();
        Map<LoadFileName, Content> activeConfMap = ActiveConfLoader.getLoader().getConfMap();
        Map<String, Object> tmpMainConfMap = new HashMap<>();
        confMap.forEach(((loadFileName, content) -> tmpMainConfMap.putIfAbsent(loadFileName.getFileName(), content.getContent())));
        activeConfMap.forEach(((loadFileName, content) -> tmpMainConfMap.putIfAbsent(loadFileName.getFileName(), content.getContent())));
        return tmpMainConfMap;
    }

}
