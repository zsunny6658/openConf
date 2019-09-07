package com.sunny.commom.utils;

import com.sunny.source.bean.LoadFileName;

import java.util.List;

/**
 * create by zsunny
 * data: 2019/9/7
 **/
public class LoadFileNameUtils {

    // 判断是否以及包含loadFileName
    public static boolean hasLoadFileName(List<LoadFileName> loadFileNameList, LoadFileName loadFileName) {
        for (LoadFileName file : loadFileNameList) {
            if (file.getFileName().equals(loadFileName.getFileName())) {
                return true;
            }
        }
        return false;
    }

}
