package com.sunny.source;

import com.sunny.source.file.LoadProperties;
import com.sunny.source.file.LoadXml;
import com.sunny.source.file.LoadYaml;

public class LoadFileName implements Comparable<LoadFileName>{

    public static final LoadFileName APPLICATION_YAML =
            new LoadFileName("application.yaml",10, LoadYaml.getInstance());
    public static final LoadFileName APPLICATION_YML =
            new LoadFileName("application.yml",100, LoadYaml.getInstance());
    public static final LoadFileName APPLICATION_PROPERTIES =
            new LoadFileName("application.properties",10, LoadProperties.getInstance());
    public static final LoadFileName APPLICATION_XML =
            new LoadFileName("application.xml",1, LoadXml.getInstance());

    private String fileName;
    private int order;
    private LoadSource loadSource;

    public LoadFileName(String fileName, int order, LoadSource loadSource) {
        this.fileName = fileName;
        this.order = order;
        this.loadSource = loadSource;
    }

    @Override
    public int compareTo(LoadFileName o) {
        if(this.order == o.order){
            return this.fileName.compareTo(o.fileName);
        }
        return o.order - this.order;
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
}
