package com.sunny.source.file;

import com.sunny.utils.FileUtil;

import java.io.InputStream;
import java.util.Objects;
import java.util.Properties;

/**
 * create by zsunny
 * data: 2018/10/20
 **/
public class LoadXml extends AbstractLoadProperties {

    private LoadXml() {
    }

    private static class LoadXmlHolder {
        private static LoadXml loadXml = new LoadXml();
    }

    public static LoadXml getInstance() {
        return LoadXmlHolder.loadXml;
    }

    @Override
    public Object loadSources(String path) throws Exception {
        Properties properties = new Properties();

        InputStream in = FileUtil.getFileInputStream(path);

        if (Objects.isNull(in))
            return null;

        properties.loadFromXML(in);

        return convertToMap(properties);
    }
}
