package com.sunny.source.file;

import com.sunny.source.LoadSource;
import com.sunny.utils.FileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;

import java.io.*;

/**
 * created by zsunny
 * Email zsunny@yeah.net
 * Date on 2018/7/30.
 */
/**
 * 枚举类型实现单例，也可以考虑通过静态内部类等其他放松
 */
//TODO 考虑之后同时获取yml和properties文件并且将属性按策略合并
public enum LoadYaml implements LoadSource{

    loadYaml;

    private static final Logger log = LoggerFactory.getLogger(LoadYaml.class);

    @Override
    public Object loadSources(String path) throws IOException {

        Yaml yaml = new Yaml();

        if(!FileUtil.judgeFileExist(path))
            return null;

        return yaml.load(FileUtil.readFile(path));

    }

}
