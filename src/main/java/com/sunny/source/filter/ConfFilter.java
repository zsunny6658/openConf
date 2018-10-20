package com.sunny.source.filter;

/**
 * create by zsunny
 * data: 2018/10/20
 **/

import java.util.HashMap;

import static com.sunny.source.filter.ActiveConf.CONF_ACTIVE;

/**
 * 1.插入active配置文件配置内容
 * 2.过滤系统配置项
 */
public class ConfFilter {

    public static String[] FilterConfs = {CONF_ACTIVE,"system.conf.test"};

    private HashMap<String, Object> map = new HashMap<>();

    public static void filter(HashMap<String, Object> map) throws Exception {

        ActiveConf.insertActiveConf(map);

    }





}
