package com.sunny.source.bean;

import com.sunny.utils.ObjectUtil;

import java.io.*;

/**
 * @Author zsunny
 * @Date 2019/4/19 18:27
 * @Mail zsunny@yeah.net
 */
public class Content {

    private long modifyTime;
    private Object content;

    public Content(long modifyTime, Object content) {
        this.modifyTime = modifyTime;
        try {
            this.content = ObjectUtil.deepClone(content);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    public Content(Object content) {
        try {
            this.content = ObjectUtil.deepClone(content);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        this.modifyTime = System.currentTimeMillis();
    }
    public Content() {
    }
    public long getModifyTime() {
        return modifyTime;
    }
    public void setModifyTime(long modifyTime) {
        this.modifyTime = modifyTime;
    }
    public Object getContent() {
        return content;
    }
    public void setContent(Object content) {
        try {
            this.content = ObjectUtil.deepClone(content);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    @Override
    public String toString() {
        return "Content{" +
                "modifyTime=" + modifyTime +
                ", content=" + content +
                '}';
    }

}
