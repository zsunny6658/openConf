package com.sunny.commom.listener;

/**
 * 实现配置读入的监听器，默认包含一个前置策略用于读取自定义配置源文件，不可覆盖
 */
public interface SourceListener {

    void doBefore();

    void doAfter();

}
