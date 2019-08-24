package com.sunny.source;

import java.io.File;
import java.util.*;

import com.sunny.commom.handler.ClassHandler;
import com.sunny.commom.constant.LoadFileNameConstant;
import com.sunny.commom.listener.SourceListener;
import com.sunny.commom.listener.impl.DefaultConfProcessListner;
import com.sunny.commom.listener.impl.DefaultConfSourceListener;
import com.sunny.source.bean.Content;
import com.sunny.source.bean.LoadFileName;
import com.sunny.source.bean.Node;
import com.sunny.source.filter.ConfFilter;
import com.sunny.utils.FileUtil;
import com.sunny.utils.ObjectUtil;

public class LoadResult {

    private static List<SourceListener> sourceListeners;

    private static List<LoadFileName> loadFileNameList =
            new ArrayList<>(Arrays.asList(LoadFileNameConstant.APPLICATION_YML, LoadFileNameConstant.APPLICATION_YAML,
                    LoadFileNameConstant.APPLICATION_PROPERTIES, LoadFileNameConstant.APPLICATION_XML,
                    LoadFileNameConstant.APPLICATION_JSON));
    private static Object source;
    private static Map<LoadFileName, Content> cache = new TreeMap<>();

    private static ClassHandler classHandler = ClassHandler.getClassHandler();

    public static void add(LoadFileName loadFile) {
        loadFileNameList.add(loadFile);
    }

    public static void remove(LoadFileName loadFile) {
        loadFileNameList.remove(loadFile);
    }

    // 主入口
    public static void loadResult() throws Exception {
        dealWithListener();
        // 前置策略，必包含前置读取自定义conf source
        sourceListeners.forEach(SourceListener::doBefore);
        source = getSources(false);
        // listener后置处理
        sourceListeners.forEach(SourceListener::doAfter);
    }

    /**
     * 处理SourceListerner
     */
    private static void dealWithListener() {
        // TODO 后续还会增加部分前置处理策略
        // 前置处理注解@ConfSource，用于获取默认配置之外的配置文件
        sourceListeners = new ArrayList<>();
        sourceListeners.add(new DefaultConfSourceListener());
        classHandler.getClassSet().forEach(clazz -> {
            if (SourceListener.class.isAssignableFrom(clazz) && !clazz.isInterface() &&
                    !clazz.getCanonicalName().equals(DefaultConfSourceListener.class.getCanonicalName())){
                try {
                    sourceListeners.add((SourceListener) clazz.newInstance());
                } catch (InstantiationException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public static void updateResult() throws Exception {
        source = getSources(true);
    }

    public static Object getSource() {
        return source;
    }

    @SuppressWarnings("unchecked")
    private static Object getSources(boolean isUpdate) throws Exception {
        Collections.sort(loadFileNameList);
        Map<String, Object> res = new HashMap<>();
        for (LoadFileName loadFileName : loadFileNameList) {
            Object sourceResult;
            boolean needUpdate;
            if (!isUpdate) {
                // load at the first time
                sourceResult = loadFileName.getLoadSource().loadSources(loadFileName.getFileName());
            } else {
                long recModifyTime = 0;
                if (Objects.nonNull(cache.get(loadFileName))) {
                    recModifyTime = cache.get(loadFileName).getModifyTime();
                }
                File file = FileUtil.getFile(loadFileName.getFileName());
                long modifyTime = file.lastModified();
                needUpdate = (modifyTime > recModifyTime);
                if (needUpdate) {
                    // for dynamic
                    // need to reload, means the file is changed
                    sourceResult = loadFileName.getLoadSource().loadSources(loadFileName.getFileName());
                    cache.get(loadFileName).setModifyTime(modifyTime);
                    cache.get(loadFileName).setContent(sourceResult);
                } else {
                    // do not need to reload, just load from resMap
                    if (Objects.isNull(cache.get(loadFileName)))
                        sourceResult = null;
                    else
                        // deep copy, otherwise the object is going to be changed by others
                        sourceResult = ObjectUtil.deepCopy(cache.get(loadFileName).getContent());
                }
            }
            if (Objects.isNull(sourceResult)) {
                continue;
            }
            if (!isUpdate) {
                cache.put(loadFileName, new Content(sourceResult));
            }
            if (0 == res.size()) {
                res = (Map<String, Object>) sourceResult;
                continue;
            }
            Node.merge(res, (Map<String, Object>) sourceResult, false, cache);
        }
        ConfFilter.filter(res, isUpdate);
        return res;
    }
}
