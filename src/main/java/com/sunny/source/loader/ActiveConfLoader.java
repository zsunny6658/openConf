package com.sunny.source.loader;

import java.util.*;

import com.sunny.commom.constant.Constant;
import com.sunny.commom.utils.CollectionUtils;
import com.sunny.source.bean.Content;
import com.sunny.source.bean.LoadFileName;
import com.sunny.source.file.LoadProperties;
import com.sunny.source.file.LoadXml;
import com.sunny.source.file.LoadYaml;

public class ActiveConfLoader extends AbstractConfLoader {

    private List<LoadFileName> loadFileNameList = new ArrayList<>();
    private Map<LoadFileName, Content> activeConfMap = new TreeMap<>();
    private Map<String, Object> activeConfValues;

    private ActiveConfLoader() {
    }

    private static class ActiveConfLoaderInner {
        private static ActiveConfLoader activeConfLoader = new ActiveConfLoader();
    }

    public static ActiveConfLoader getLoader() {
        return ActiveConfLoaderInner.activeConfLoader;
    }

    public void loadResult() throws Exception {
        getActiveConfFiles();
        loadSource(activeConfMap, loadFileNameList);
        activeConfValues = mergeSource(activeConfMap);
    }

    public void updateResult() throws Exception {
        loadSource(activeConfMap, loadFileNameList);
        activeConfValues = mergeSource(activeConfMap);
    }

    public Map<String, Object> getSource() {
        return activeConfValues;
    }

    /**
     * 获取active配置源
     */
    @SuppressWarnings("unchecked")
    private void getActiveConfFiles() throws Exception {
        Map<String, Object> map = ConfLoader.getLoader().getSource();
        if (CollectionUtils.isEmpty(map)) {
            throw new Exception("need to get conf source first");
        }
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
