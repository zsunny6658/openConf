package com.sunny.source.loader;

import java.io.File;
import java.util.*;

import com.sunny.commom.handler.ClassHandler;
import com.sunny.commom.constant.LoadFileNameConstant;
import com.sunny.commom.listener.SourceListener;
import com.sunny.commom.listener.impl.DefaultConfSourceListener;
import com.sunny.commom.utils.NodeUtils;
import com.sunny.source.bean.Content;
import com.sunny.source.bean.LoadFileName;
import com.sunny.commom.utils.FileUtils;
import com.sunny.commom.utils.ObjectUtils;

public class ConfLoader extends AbstractConfLoader{

    private static List<SourceListener> sourceListeners;

    private static List<LoadFileName> loadFileNameList =
            new ArrayList<>(Arrays.asList(LoadFileNameConstant.APPLICATION_YML, LoadFileNameConstant.APPLICATION_YAML,
                    LoadFileNameConstant.APPLICATION_PROPERTIES, LoadFileNameConstant.APPLICATION_XML,
                    LoadFileNameConstant.APPLICATION_JSON));
    private static Map<LoadFileName, Content> confMap;
    private static Map<String, Object> confValues;

    private static ClassHandler classHandler = ClassHandler.getClassHandler();

    public static void add(LoadFileName loadFile) {
        loadFileNameList.add(loadFile);
    }

    public static void remove(LoadFileName loadFile) {
        loadFileNameList.remove(loadFile);
    }

    // 首次加载
    public static void loadResult() throws Exception {
        dealWithListener();
        // 前置策略，必包含前置读取自定义conf source
        sourceListeners.forEach(SourceListener::doBefore);
        // 加载各配置源配置内容
        loadSource();
        // 合并到confValues中
        mergeSource();
        // listener后置处理
        sourceListeners.forEach(SourceListener::doAfter);
    }

    public static void updateResult() throws Exception {
        // 加载各配置源配置内容
        loadSource();
        // 合并到confValues中
        mergeSource();
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

    /**
     * 本方法可多次调用，多次调用过程中会判断文件是否变化，可适用于dynamic
     * @throws Exception
     */
    private static void loadSource() throws Exception {
        if (Objects.isNull(confMap)) {
            confMap = new TreeMap<>();
        }
        for (LoadFileName loadFileName : loadFileNameList) {
            Object sourceResult;
            // 判断配置源文件是否有变化
            long recModifyTime = 0;
            if (Objects.nonNull(confMap.get(loadFileName))) {
                recModifyTime = confMap.get(loadFileName).getModifyTime();
            }
            File file = FileUtils.getFile(loadFileName.getFileName());
            // 获取最后一次修改时间
            long modifyTime = file.lastModified();
            if (modifyTime > recModifyTime) {
                // 文件有变化，需要重新读取
                sourceResult = loadFileName.getLoadSource().loadSources(loadFileName.getFileName());
                // 首次读取防止npe
                confMap.putIfAbsent(loadFileName, new Content());
                confMap.get(loadFileName).setModifyTime(modifyTime);
                confMap.get(loadFileName).setContent(sourceResult);
            }
        }
    }

    @SuppressWarnings("unchecked")
    private static void mergeSource() {
        Map<String, Object> res = new HashMap<>();
        confMap.forEach((loadFileName, content) -> {
            // deep copy, otherwise the object is going to be changed by others
            Map<String, Object> soureResult = (Map<String, Object>) ObjectUtils.deepCopy(content.getContent());
            NodeUtils.merge(res, soureResult, false);
        });
        confValues = res;
    }

    public static Map<String, Object> getSource() {
        return confValues;
    }

}
