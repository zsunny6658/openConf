package com.sunny.source.loader;

import java.io.File;
import java.util.*;

import com.sunny.commom.constant.Constant;
import com.sunny.commom.utils.NodeUtils;
import com.sunny.source.bean.Content;
import com.sunny.source.bean.LoadFileName;
import com.sunny.source.file.LoadProperties;
import com.sunny.source.file.LoadXml;
import com.sunny.source.file.LoadYaml;
import com.sunny.commom.utils.FileUtils;
import com.sunny.commom.utils.ObjectUtils;

public class ActiveConfLoader extends AbstractConfLoader{

    private static List<LoadFileName> loadFileNameList = new ArrayList<>();
    private static Map<LoadFileName, Content> activeConfMap;
    private static Map<String, Object> activeConfValues;

    public static void loadResult(Map<String, Object> confMap) throws Exception {
        getActiveConfFiles(confMap);
        loadSource();
        mergeSource();
    }

    public static void updateResult() throws Exception {
        loadSource();
        mergeSource();
    }

    private static void loadSource() throws Exception {
        if (Objects.isNull(activeConfMap)) {
            activeConfMap = new TreeMap<>();
        }
        for (LoadFileName loadFileName : loadFileNameList) {
            Object sourceResult;
            // 判断文件最近一次更新时间
            long recModifyTime = 0;
            if (Objects.nonNull(activeConfMap.get(loadFileName))) {
                recModifyTime = activeConfMap.get(loadFileName).getModifyTime();
            }
            File file = FileUtils.getFile(loadFileName.getFileName());
            long modifyTime = file.lastModified();
            if (modifyTime > recModifyTime) {
                sourceResult = loadFileName.getLoadSource().loadSources(loadFileName.getFileName());
                activeConfMap.putIfAbsent(loadFileName, new Content());
                activeConfMap.get(loadFileName).setModifyTime(modifyTime);
                activeConfMap.get(loadFileName).setContent(sourceResult);
            }
        }
    }

    @SuppressWarnings("unchecked")
    private static void mergeSource() {
        Map<String, Object> res = new HashMap<>();
        activeConfMap.forEach((loadFileName, content) -> {
            Map<String, Object> soureResult = (Map<String, Object>) ObjectUtils.deepCopy(content.getContent());
            NodeUtils.merge(res, soureResult, false);
        });
        activeConfValues = res;
    }

    public static Map<String, Object> getSource() {
        return activeConfValues;
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

        if (Objects.nonNull(confName)) {
            loadFileNameList.addAll(Arrays.asList(
                    new LoadFileName("application-" + confName + ".properties", LoadProperties.getInstance()),
                    new LoadFileName("application-" + confName + ".yml", LoadYaml.getInstance()),
                    new LoadFileName("application-" + confName + ".yaml", LoadYaml.getInstance()),
                    new LoadFileName("application-" + confName + ".xml", LoadXml.getInstance()),
                    new LoadFileName("application-" + confName + ".json", LoadXml.getInstance())
            ));
        }
    }
}
