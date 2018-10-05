package com.sunny.source;

//todo 考虑线程安全
public interface LoadSource {

    Object loadSources(String path) throws Exception;

}
