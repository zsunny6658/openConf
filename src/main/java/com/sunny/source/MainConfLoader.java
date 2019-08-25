package com.sunny.source;

import com.sunny.commom.utils.NodeUtils;
import com.sunny.commom.utils.ObjectUtils;
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

    private static Map<String, Object> mainConfValues;

    public static Object load() {
        try {
            ConfLoader.loadResult();
            ActiveConfLoader.loadResult(ConfLoader.getSource());
            mergeSources();
            ConfFilter.filter(mainConfValues);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mainConfValues;
    }

    public static void update() {
        try {
            ConfLoader.updateResult();
            ActiveConfLoader.updateResult();
            mergeSources();
            ConfFilter.filter(mainConfValues);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Object getMainConfValues() {
        return mainConfValues;
    }

    @SuppressWarnings("unchecked")
    private static void mergeSources() {
        mainConfValues = new HashMap<>();
        Map<String, Object> confValues = ConfLoader.getSource();
        Map<String, Object> activeConfValues = ActiveConfLoader.getSource();
        NodeUtils.merge(mainConfValues, (Map<String, Object>) ObjectUtils.deepCopy(confValues), true);
        NodeUtils.merge(mainConfValues, (Map<String, Object>) ObjectUtils.deepCopy(activeConfValues), true);
    }

}
