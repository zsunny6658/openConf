package com.sunny.source.filter;

import java.io.File;
import java.util.*;

import com.sunny.commom.constant.Constant;
import com.sunny.source.bean.Content;
import com.sunny.source.bean.LoadFileName;
import com.sunny.source.bean.Node;
import com.sunny.source.file.LoadProperties;
import com.sunny.source.file.LoadXml;
import com.sunny.source.file.LoadYaml;
import com.sunny.utils.FileUtil;
import com.sunny.utils.ObjectUtil;

public class ActiveConf {

    private static List<LoadFileName> loadFileNameList = new ArrayList<>();
    private static Map<LoadFileName, Content> resMap = new TreeMap<>();

    static void insertActiveConf(Map<String, Object> map, boolean isUpdate) throws Exception {
        if (!isUpdate)
            getActiveConfFiles(map);
        insertCore(map, isUpdate);
    }

    /**
     * 获取active配置源
     *
     * @param map
     */
    @SuppressWarnings("unchecked")
    private static void getActiveConfFiles(Map<String, Object> map) {
        Map<String, Object> tmpMap = map;
        String[] active = Constant.CONF_ACTIVE.split("\\.");
        String confName = null;

        // access active conf file
        for (int i = 0; i < active.length; i++) {
            String act = active[i];
            if (!tmpMap.containsKey(act)) {
                break;
            }
            if (i < active.length - 1) {
                if (tmpMap.get(act) instanceof String)
                    break;
                tmpMap = (Map<String, Object>) tmpMap.get(act);
            }
            if (i == active.length - 1) {
                if (!(tmpMap.get(act) instanceof String))
                    break;
                confName = (String) tmpMap.get(act);
            }
        }

        if (null != confName) {
            loadFileNameList.addAll(Arrays.asList(
                    new LoadFileName("application-" + confName + ".properties", LoadProperties.getInstance()),
                    new LoadFileName("application-" + confName + ".yml", LoadYaml.getInstance()),
                    new LoadFileName("application-" + confName + ".yaml", LoadYaml.getInstance()),
                    new LoadFileName("application-" + confName + ".xml", LoadXml.getInstance())
            ));
        }
    }

    /**
     * 将设定的active配置项插入
     *
     * @param map
     */
    @SuppressWarnings("unchecked")
    private static void insertCore(Map<String, Object> map, boolean isUpdate) throws Exception {
        for (LoadFileName loadFileName : loadFileNameList) {
            Object sourceResult;
            if (!isUpdate) {
                sourceResult = loadFileName.getLoadSource().loadSources(loadFileName.getFileName());
            } else {
                long recModifyTime = 0;
                if (Objects.nonNull(resMap.get(loadFileName))) {
                    recModifyTime = resMap.get(loadFileName).getModifyTime();
                }
                File file = FileUtil.getFile(loadFileName.getFileName());
                long modifyTime = file.lastModified();
                if (modifyTime > recModifyTime) {
                    sourceResult = loadFileName.getLoadSource().loadSources(loadFileName.getFileName());
                    resMap.get(loadFileName).setModifyTime(modifyTime);
                    resMap.get(loadFileName).setContent(sourceResult);
                } else {
                    if (Objects.isNull(resMap.get(loadFileName)))
                        sourceResult = null;
                    else
                        sourceResult = ObjectUtil.deepCopy(resMap.get(loadFileName).getContent());
                }
            }
            if (Objects.isNull(sourceResult)) {
                continue;
            }
            if (!isUpdate)
                resMap.put(loadFileName, new Content(sourceResult));
            Node.merge(map, (Map<String, Object>) sourceResult, true, null);
        }
    }

}
