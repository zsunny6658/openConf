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

    public static String CONF_LISTENER = "system.conf.listener";

    public static String[] filterConfs = {CONF_ACTIVE, CONF_LISTENER};

    private static HashMap<String, Object> systemMap = new HashMap<>();

    public static void filter(HashMap<String, Object> map) throws Exception {

        ActiveConf.insertActiveConf(map);

        filterCore(map);

    }

    /**
     * 执行真正的filter逻辑
     * @param map
     */
    private static void filterCore(HashMap<String, Object> map){

        for(String filterConf: filterConfs){

            String[] filterConfPath = filterConf.split("\\.");

            filterSingle(map, filterConfPath);

        }

    }

    /**
     * 删除单条
     * @param map
     * @param filterConfPath
     */
    private static void filterSingle(HashMap<String, Object> map, String[] filterConfPath){

        judgeAndDeleteSinle(map, systemMap, filterConfPath, 0);

    }

    /**
     * 用递归来实现删除和赋值给systemMap
     * @param map
     * @param tmpSystemMap
     * @param filterConfPath
     * @param ind
     * @return
     */
    private static boolean judgeAndDeleteSinle(HashMap<String, Object> map, HashMap<String, Object> tmpSystemMap,
                                               String[] filterConfPath, int ind){

        if(!map.containsKey(filterConfPath[ind])){
            return false;
        }

        String key = filterConfPath[ind];
        if(ind == filterConfPath.length-1){
            if(!(map.get(key) instanceof String))
                return false;
            tmpSystemMap.put(key, map.get(key));
            map.remove(key);
            if(map.size() == 0)
                return true;
        }else{
            if(map.get(key) instanceof String){
                return false;
            }
            if(!tmpSystemMap.containsKey(key)){
                tmpSystemMap.put(key, new HashMap<String, Object>());
            }
            boolean tmpRes = judgeAndDeleteSinle((HashMap<String, Object>) map.get(key),
                    (HashMap<String, Object>) tmpSystemMap.get(key),filterConfPath, ind+1);
            if(tmpRes){
                map.remove(key);
                if(map.size() == 0)
                    return true;
            }
        }

        return false;

    }

    public static HashMap<String, Object> getSystemMap() {
        return systemMap;
    }
}
