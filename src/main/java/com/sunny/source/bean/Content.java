package com.sunny.source.bean;

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
        this.content = content;
    }
    public Content(Object content) {
        this.content = content;
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
        this.content = content;
    }
    @Override
    public String toString() {
        return "Content{" +
                "modifyTime=" + modifyTime +
                ", content=" + content +
                '}';
    }
}
