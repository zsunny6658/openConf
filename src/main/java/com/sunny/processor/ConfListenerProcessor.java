package com.sunny.processor;

import java.util.Map;
import java.util.Objects;

import com.sunny.processor.main.MainProcessor;
import com.sunny.source.filter.ConfFilter;
import com.sunny.source.listener.ConfListner;

/**
 * 简易listener处理器，后考虑多listener处理
 */
public class ConfListenerProcessor extends AbstractConfProcessor {
    @Override
    public void process() {
        ConfListner confListner = null;
        try {
            confListner = getListener();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        if (null != confListner)
            MainProcessor.addListener(confListner);
    }

    /**
     * 获取监听器
     *
     * @return
     * @throws ClassNotFoundException
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    @SuppressWarnings("unchecked")
    private static ConfListner getListener()
            throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        String listenerClass = "";
        String[] confPath = ConfFilter.CONF_LISTENER.split("\\.");
        Map<String, Object> map = ConfFilter.getSystemMap();
        if (Objects.isNull(map) || 0 == confPath.length)
            return null;
        for (int i = 0; i < confPath.length; i++) {
            if (Objects.isNull(map.get(confPath[i])))
                return null;
            if (i < confPath.length - 1) {
                if (map.get(confPath[i]) instanceof String)
                    return null;
                map = (Map<String, Object>) map.get(confPath[i]);
            } else {
                if (!(map.get(confPath[i]) instanceof String))
                    return null;
                listenerClass = (String) map.get(confPath[i]);
            }
        }
        if ("".equals(listenerClass))
            return null;
        ConfListner confListner = (ConfListner) Class.forName(listenerClass).newInstance();
        return confListner;
    }

}
