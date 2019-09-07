package com.sunny.source.loader;

import com.sunny.commom.utils.FileUtils;
import com.sunny.commom.utils.NodeUtils;
import com.sunny.commom.utils.ObjectUtils;
import com.sunny.source.bean.Content;
import com.sunny.source.bean.LoadFileName;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public abstract class AbstractConfLoader {

    public abstract void loadResult() throws Exception;

    public abstract void updateResult() throws Exception;

    public abstract Map<String, Object> getSource();

    protected void loadSource(Map<LoadFileName, Content> map, List<LoadFileName> loadFileNameList) throws Exception {
        for (LoadFileName loadFileName : loadFileNameList) {
            Object sourceResult;
            // 判断配置源文件是否有变化
            long recModifyTime = 0;
            if (Objects.nonNull(map.get(loadFileName))) {
                recModifyTime = map.get(loadFileName).getModifyTime();
            }
            File file = FileUtils.getFile(loadFileName.getFileName());
            // 获取最后一次修改时间
            long modifyTime = file.lastModified();
            if (modifyTime > recModifyTime) {
                // 文件有变化，需要重新读取
                sourceResult = loadFileName.getLoadSource().loadSources(loadFileName.getFileName());
                // 首次读取防止npe
                map.putIfAbsent(loadFileName, new Content());
                map.get(loadFileName).setModifyTime(modifyTime);
                map.get(loadFileName).setContent(sourceResult);
            }
        }
    }

    @SuppressWarnings("unchecked")
    protected Map<String, Object> mergeSource(Map<LoadFileName, Content> map) {
        Map<String, Object> res = new HashMap<>();
        map.forEach((loadFileName, content) -> {
            Map<String, Object> soureResult = (Map<String, Object>) ObjectUtils.deepCopy(content.getContent());
            NodeUtils.merge(res, soureResult, false);
        });
        return res;
    }
}
