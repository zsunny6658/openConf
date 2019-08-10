package com.sunny.source.bean;

import com.sunny.source.file.LoadSource;

/**
 * create by zsunny
 * data: 2018/10/20
 **/
public class LoadFileName implements Comparable<LoadFileName> {

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
