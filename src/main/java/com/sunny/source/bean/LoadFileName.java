package com.sunny.source.bean;

import com.sunny.source.LoadSource;
import com.sunny.source.file.LoadJson;
import com.sunny.source.file.LoadProperties;
import com.sunny.source.file.LoadXml;
import com.sunny.source.file.LoadYaml;

/**
 * create by zsunny
 * data: 2018/10/20
 **/
public class LoadFileName implements Comparable<LoadFileName>{

    public static final LoadFileName APPLICATION_YAML =
            new LoadFileName("application.yaml",10, LoadYaml.getInstance());
    public static final LoadFileName APPLICATION_YML =
            new LoadFileName("application.yml",100, LoadYaml.getInstance());
    public static final LoadFileName APPLICATION_PROPERTIES =
            new LoadFileName("application.properties",1, LoadProperties.getInstance());
    public static final LoadFileName APPLICATION_XML =
            new LoadFileName("application.xml",1000, LoadXml.getInstance());
    public static final LoadFileName APPLICATION_JSON =
            new LoadFileName("application.json", 1000, LoadJson.getInstance());

    private String fileName;
    private int order;
    private LoadSource loadSource;

    public LoadFileName(String fileName, LoadSource loadSource) {
        this.fileName = fileName;
        this.loadSource = loadSource;
        this.order = 1000;
    }

    public LoadFileName(String fileName, int order, LoadSource loadSource) {
        this.fileName = fileName;
        this.order = order;
        this.loadSource = loadSource;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public LoadSource getLoadSource() {
        return loadSource;
    }

    public void setLoadSource(LoadSource loadSource) {
        this.loadSource = loadSource;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    @Override
    public String toString() {
        return "LoadFileName{" +
                "fileName='" + fileName + '\'' +
                ", order=" + order +
                ", loadSource=" + loadSource +
                '}';
    }

    @Override
    public int compareTo(LoadFileName o) {
        if (this.getOrder() == o.getOrder()) {
            return this.getFileName().compareTo(o.getFileName());
        }
        return o.getOrder() - this.getOrder();
    }
}
