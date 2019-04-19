package com.sunny.source.filter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.sunny.source.bean.LoadFileName;
import com.sunny.source.bean.Node;
import com.sunny.source.file.LoadProperties;
import com.sunny.source.file.LoadXml;
import com.sunny.source.file.LoadYaml;

public class ActiveConf {

    static final String CONF_ACTIVE = "system.conf.active";

    private static List<LoadFileName> loadFileNameList = new ArrayList<>();

    static void insertActiveConf(Map<String, Object> map, boolean isUpdate) throws Exception {
        if(!isUpdate)
            getActiveConfFiles(map);
        insertCore(map);
    }

    /**
     * 获取active配置源
     * @param map
     */
    @SuppressWarnings("unchecked")
	private static void getActiveConfFiles(Map<String, Object> map){
    	Map<String, Object> tmpMap = map;
        String[] active = CONF_ACTIVE.split("\\.");
        String confName = null;

        //获取激活的配置文件
        for (int i=0; i < active.length; i++){
            String act = active[i];
            if(!tmpMap.containsKey(act)){
                break;
            }
            if(i < active.length-1){
                if(tmpMap.get(act) instanceof String)
                    break;
                tmpMap = (Map<String, Object>) tmpMap.get(act);
            }
            if(i == active.length-1){
                if(!(tmpMap.get(act) instanceof String))
                    break;
                confName = (String) tmpMap.get(act);
            }
        }

        if(null != confName){
            loadFileNameList.addAll(Arrays.asList(
                    new LoadFileName("application-"+confName+".properties", LoadProperties.getInstance()),
                    new LoadFileName("application-"+confName+".yml", LoadYaml.getInstance()),
                    new LoadFileName("application-"+confName+".yaml",LoadYaml.getInstance()),
                    new LoadFileName("application-"+confName+".xml", LoadXml.getInstance())
            ));
        }
    }

    /**
     * 将设定的active配置项插入
     * @param map
     */
    @SuppressWarnings("unchecked")
	private static void insertCore(Map<String, Object> map) throws Exception {
    	// Map<String, Object> res = new HashMap<>();
        for(LoadFileName loadFileName: loadFileNameList){
            Object sourceResult = loadFileName.getLoadSource().loadSources(loadFileName.getFileName());
            if(null == sourceResult){
                continue;
            }
            Node.merge(map, (Map<String, Object>) sourceResult,true);
        }
    }

}
