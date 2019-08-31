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

    private ConfLoader confLoader;
    private ActiveConfLoader activeConfLoader;

    private Map<String, Object> mainConfValues;

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
            ConfFilter.filter(mainConfValues);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Object getMainConfValues() {
        return mainConfValues;
    }

    @SuppressWarnings("unchecked")
    private void mergeSources() {
        mainConfValues = new HashMap<>();
        Map<String, Object> confValues = ConfLoader.getLoader().getSource();
        Map<String, Object> activeConfValues = ActiveConfLoader.getLoader().getSource();
        NodeUtils.merge(mainConfValues, (Map<String, Object>) ObjectUtils.deepCopy(confValues), true);
        NodeUtils.merge(mainConfValues, (Map<String, Object>) ObjectUtils.deepCopy(activeConfValues), true);
    }

}
