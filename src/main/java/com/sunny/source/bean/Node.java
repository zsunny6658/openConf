package com.sunny.source.bean;

import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;

public class Node {
    private Map<String, Object> res;
    private Map<String, Object> source;

    public Node(Map<String, Object> res, Map<String, Object> source) {
        this.res = res;
        this.source = source;
    }

    public Map<String, Object> getRes() {
        return res;
    }

    public void setRes(Map<String, Object> res) {
        this.res = res;
    }

    public Map<String, Object> getSource() {
        return source;
    }

    public void setSource(Map<String, Object> source) {
        this.source = source;
    }

    @Override
    public String toString() {
        return "Node{" +
                "res=" + res +
                ", source=" + source +
                '}';
    }
}
