package com.sunny.source;

import com.sunny.source.loader.ActiveConfLoader;
import com.sunny.source.loader.ConfLoader;

/**
 * create by zsunny
 * data: 2019/8/25
 **/
public class MainConfLoader {

    public Object load() {
        try {
            ConfLoader.loadResult();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ConfLoader.getSource();
    }

}
