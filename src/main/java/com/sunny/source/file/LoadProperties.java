package com.sunny.source.file;

import com.sunny.source.LoadSource;
import com.sunny.utils.FileUtil;

import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Properties;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * created by zsunny
 * Email zsunny@yeah.net
 * Date on 2018/10/5.
 */
/**
 * 静态内部类实现单例
 */
public class LoadProperties extends AbstractLoadProperties{

    private LoadProperties(){}

    private static class LoadPropertiesHolder{
        private static LoadProperties loadProperties = new LoadProperties();
    }

    public static LoadProperties getInstance(){
        return LoadPropertiesHolder.loadProperties;
    }

    @Override
    public Object loadSources(String path) throws Exception {

        Properties properties = new Properties();

        InputStream in = FileUtil.getFileInputStream(path);

        if(null == in)
            return null;

        properties.load(in);

        return convertToMap(properties);
    }



}
