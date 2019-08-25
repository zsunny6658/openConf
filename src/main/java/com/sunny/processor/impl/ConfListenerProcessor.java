package com.sunny.processor.impl;

import java.util.Map;
import java.util.Objects;

import com.sunny.commom.constant.Constant;
import com.sunny.commom.constant.ListenerConstant;
import com.sunny.commom.listener.ConfProcessListner;
import com.sunny.processor.MainProcessor;
import com.sunny.source.filter.ConfFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 简易listener处理器，后考虑多listener处理
 */
public class ConfListenerProcessor extends AbstractConfProcessor {

    private Logger log = LoggerFactory.getLogger(ConfClassProcessor.class);

    @Override
    public void process() {
        ConfProcessListner confListner = null;
        try {
            confListner = getListener();
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
            log.warn("error create listener: {}", e.getMessage());
        }
        if (Objects.nonNull(confListner)) {
            MainProcessor.addListener(confListner);
        } else {
            MainProcessor.addListener(ListenerConstant.DEFAULT_LISTENER);
        }
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
    private static ConfProcessListner getListener()
            throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        String listenerClass = "";
        String[] confPath = Constant.CONF_LISTENER.split("\\.");
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
        return (ConfProcessListner) Class.forName(listenerClass).newInstance();
    }

}
