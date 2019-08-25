package com.sunny.source.filter;

import com.sunny.commom.constant.Constant;
import com.sunny.source.loader.ActiveConfLoader;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 1.插入active配置文件配置内容
 * 2.过滤系统配置项
 * create by zsunny
 * data: 2018/10/20
 */
public class ConfFilter {

    public static String[] filterConfs = {Constant.CONF_ACTIVE, Constant.CONF_LISTENER, Constant.DYNAMIC_INTERVAL};

    private static Map<String, Object> systemMap = new HashMap<>();

    public static void filter(Map<String, Object> map, boolean isUpdate) throws Exception {
        ActiveConfLoader.loadResult(map, isUpdate);
        filterCore(map);
    }

    /**
     * 执行真正的filter逻辑
     *
     * @param map
     */
    private static void filterCore(Map<String, Object> map) {
        for (String filterConf : filterConfs) {
            String[] filterConfPath = filterConf.split("\\.");
            filterSingle(map, filterConfPath);
        }
    }

    /**
     * 删除单条
     *
     * @param map
     * @param filterConfPath
     */
    private static void filterSingle(Map<String, Object> map, String[] filterConfPath) {
        judgeAndDeleteSinle(map, systemMap, filterConfPath, 0);
    }

    /**
     * 用递归来实现删除和赋值给systemMap
     *
     * @param map
     * @param tmpSystemMap
     * @param filterConfPath
     * @param ind
     * @return
     */
    private static boolean judgeAndDeleteSinle(Map<String, Object> map,
                                               Map<String, Object> tmpSystemMap,
                                               String[] filterConfPath, int ind) {
        if (!map.containsKey(filterConfPath[ind])) {
            return false;
        }
        String key = filterConfPath[ind];
        if (ind == filterConfPath.length - 1) {
            if (!(map.get(key) instanceof String
                    || map.get(key) instanceof Integer
                    || map.get(key) instanceof Double
                    || map.get(key) instanceof Float
                    || map.get(key) instanceof Boolean)) {
                return false;
            }
            tmpSystemMap.put(key, map.get(key));
            map.remove(key);
            return map.size() == 0;
        } else {
            if (map.get(key) instanceof String
                    || map.get(key) instanceof Integer
                    || map.get(key) instanceof Double
                    || map.get(key) instanceof Float
                    || map.get(key) instanceof Boolean) {
                return false;
            }
            if (!tmpSystemMap.containsKey(key)) {
                tmpSystemMap.put(key, new HashMap<String, Object>());
            }
            @SuppressWarnings("unchecked")
            boolean tmpRes = judgeAndDeleteSinle((Map<String, Object>) map.get(key),
                    (Map<String, Object>) tmpSystemMap.get(key), filterConfPath, ind + 1);
            if (tmpRes) {
                map.remove(key);
                return map.size() == 0;
            }
        }

        return false;

    }

    public static Map<String, Object> getSystemMap() {
        return systemMap;
    }

    public static Object getSystemConf(String path) {

        String[] props = path.split("\\.");
        Object o = systemMap;
        int ind = 0;
        while (true) {
            if (ind < props.length && Objects.nonNull(o) && o instanceof Map) {
                o = ((Map<?, ?>) o).get(props[ind]);
            } else {
                break;
            }
            ind++;
        }

        return o;
    }
}
