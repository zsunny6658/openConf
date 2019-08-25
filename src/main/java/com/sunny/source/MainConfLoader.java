package com.sunny.source;

import com.sunny.source.filter.ConfFilter;
import com.sunny.source.loader.ActiveConfLoader;
import com.sunny.source.loader.ConfLoader;

import java.util.Map;

/**
 * create by zsunny
 * data: 2019/8/25
 **/
public class MainConfLoader {

    private static Object confs;

    public static Object load() {
        try {
            ConfLoader.loadResult();
            confs = ConfLoader.getSource();
            ActiveConfLoader.loadResult((Map<String, Object>) confs, false);
            ConfFilter.filter((Map<String, Object>) confs);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ConfLoader.getSource();
    }

    public static void update() {
        try {
            ConfLoader.updateResult();
            ActiveConfLoader.loadResult((Map<String, Object>) confs, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Object getConfs() {
        return confs;
    }

}
